package org.example.first.groundingappapis.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.first.groundingappapis.dto.TradingDto;
import org.example.first.groundingappapis.entity.*;
import org.example.first.groundingappapis.exception.*;
import org.example.first.groundingappapis.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class RefactorTradingService {
    private final OrderRepository orderRepository;
    private final QuoteRepository quoteRepository;
    private final DepositLogRepository depositLogRepository;
    private final RealTimeTransactionLogRepository realTimeTransactionLogRepository;
    private final DayTransactionLogRepository dayTransactionLogRepository;
    private final AccountRepository accountRepository;
    private final PropertyRepository propertyRepository;
    private final InventoryRepository inventoryRepository;
    private final UserRepository userRepository;

    @Transactional
    public void validateBuyOrder(User buyer, Property property, int quantity, int price) {
        final Account buyerAccount = buyer.getAccount();

        if(!isEnoughFundraise(property)){
            throw new TradingException(TradingErrorResult.NOT_ENOUGH_FUNDRAISE);
        }
        if(buyerAccount.getDeposit() < quantity * price){
            throw new TradingException(TradingErrorResult.NOT_ENOUGH_DEPOSIT);
        }

    }
    @Transactional
    public void validateSellOrder(User seller, Property property, int quantity, int price) {
        final Account sellerAccount = seller.getAccount();

        if(!isEnoughFundraise(property)){
            throw new TradingException(TradingErrorResult.NOT_ENOUGH_FUNDRAISE);
        }
        //TODO: addInventory to account when buy

        final Inventory inventory = inventoryRepository.findByAccountAndProperty(sellerAccount, property)
                .orElseThrow(() -> new TradingException(TradingErrorResult.INVENTORY_NOT_FOUND));

        if(quantity > inventory.getSellableQuantity()){
            throw new TradingException(TradingErrorResult.NOT_ENOUGH_INVENTORY);
        }
    }
    public Double getFluctuationRate(int openingPrice, int executedPrice) {
        if (openingPrice == 0) {
            return 0.0;
        }
        return ((double) (executedPrice - openingPrice) / openingPrice) * 100;
    }
    @Transactional(readOnly = true)
    public boolean isEnoughFundraise(Property property){
        return property.getFundraise().getProgressRate() >= 100.0;
    }
    @Transactional
    public void updateMinMaxForDayAndCreateRealTimeTransactionLog(Property property, int executedQuantity, int executedPrice, DayTransactionLog dayTransactionLog, List<RealTimeTransactionLog> realTimeTransactionLogs) {
        RealTimeTransactionLog realTimeTransactionLog = RealTimeTransactionLog.builder()
                .quantity(executedQuantity)
                .executedPrice(executedPrice)
                .executedAt(LocalDateTime.now())
                .fluctuationRate(getFluctuationRate(dayTransactionLog.getOpeningPrice(), executedPrice))
                .build();

        realTimeTransactionLog.updateProperty(property);
        realTimeTransactionLogs.add(realTimeTransactionLog);

        if (executedPrice > dayTransactionLog.getMaxPrice()) {
            dayTransactionLog.updateMaxPrice(executedPrice);
        } else if (executedPrice < dayTransactionLog.getMinPrice()) {
            dayTransactionLog.updateMinPrice(executedPrice);
        }

        dayTransactionLog.increaseVolumeCount(executedQuantity);
    }


    @Transactional
    public TradingDto.BuyResponse executeBuyTransaction(UUID userId, UUID propertyId, TradingDto.BuyRequest buyRequest) {
        final User buyer = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorResult.USER_NOT_FOUND));
        final Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new UserException(UserErrorResult.USER_NOT_FOUND));
        int quantity = buyRequest.getQuantity();
        int price = buyRequest.getPrice();

        validateBuyOrder(buyer, property, quantity, price);

        //매수 호가를 올려두기 전에 예수금에서 차감
        final Account buyerAccount = buyer.getAccount();
        buyerAccount.minusDeposit(Long.valueOf(quantity * price));

        //매수 주문을 내고 호가를 올림
        uploadBuyOrderAndQuote(buyer, property, quantity, price);

        //겹치는 호가에 대한 실제 거래를 진행
        TradingDto.BuyResponse buyResponse = executeBuyTransaction(buyer, property, quantity, price);

        //실시간 체결 로그 flucutation_rate에 따른 해당 종목의 inventory 모든 수익률 업데이트 및
        //실시간 체결 로그 flucutation_rate에 따른 해당 종목의 inventory를 가지는 모든 유저 계정의 수익률 업데이트
        updateEarningsRateForAllAccountsByProperty(property);

        accountRepository.save(buyerAccount);
        return buyResponse;
    }
    @Transactional
    public void updateEarningsRateForAllAccountsByProperty(Property property) {
        List<Inventory> inventories = inventoryRepository.findAllByProperty(property);

        Optional<RealTimeTransactionLog> realTimeTransactionLogOptional = realTimeTransactionLogRepository.findFirstByPropertyIdOrderByExecutedAtDesc(property.getId());
        if (realTimeTransactionLogOptional.isPresent()) {
            RealTimeTransactionLog realTimeTransactionLog = realTimeTransactionLogOptional.get();
            Map<Account, List<Inventory>> accountInventoryMap = new HashMap<>();

            for (Inventory inventory : inventories) {
                inventory.setEarningsRate(Double.valueOf((realTimeTransactionLog.getExecutedPrice() - inventory.getAverageBuyingPrice()) / realTimeTransactionLog.getExecutedPrice()) *100);
                inventoryRepository.save(inventory);

                Account account = inventory.getAccount();
                accountInventoryMap.computeIfAbsent(account, k -> new ArrayList<>()).add(inventory);
            }

            for (Map.Entry<Account, List<Inventory>> entry : accountInventoryMap.entrySet()) {
                Account account = entry.getKey();
                account.setAverageEarningRate(accountRepository.getAverageEarningRateByAccount(account.getId()));
                accountRepository.save(account);
            }
        }
    }

    @Transactional
    public void uploadBuyOrderAndQuote(User buyer, Property property, int quantity, int price) {
        Order buyOrder = Order.builder()
                .type("매수")
                .quantity(Long.valueOf(quantity))
                .price(price)
                .status("체결 대기중")
                .createdAt(LocalDateTime.now())
                .build();

        buyOrder.updateUser(buyer);
        buyOrder.updateProperty(property);


        Quote buyQuote = Quote.builder()
                .quantity(quantity)
                .price(price)
                .type("매수")
                .build();

        buyQuote.updateProperty(property);

        orderRepository.save(buyOrder);
        buyQuote.updateOrder(buyOrder);
        quoteRepository.save(buyQuote);
    }

    @Transactional
    public void uploadSellOrderAndQuote(User seller, Property property, int quantity, int price) {
        Order sellOrder = Order.builder()
                .type("매도")
                .quantity(Long.valueOf(quantity))
                .price(price)
                .status("체결 대기중")
                .createdAt(LocalDateTime.now())
                .build();

        sellOrder.updateUser(seller);
        sellOrder.updateProperty(property);

        orderRepository.save(sellOrder);

        Quote sellQuote = Quote.builder()
                .quantity(quantity)
                .price(price)
                .type("매도")
                .build();

        sellQuote.updateOrder(sellOrder);
        sellQuote.updateProperty(property);

        quoteRepository.save(sellQuote);
    }

    @Transactional
    public TradingDto.BuyResponse executeBuyTransaction(User buyer, Property property, int quantity, int price) {
        /*
        이 메서드에서 해야 할 책임
        1. 매수 호가의 가격보다 적거나 같은 매도 호가가 있다면 낮은 가격의 매도 호가부터 우선순위로 체결을 진행한다.
        2. 언제까지? 매수 주문의 수량이 0이 될 때까지.
        3. 체결이 이루어질 때마다 체결된 수량과 가격을 기록한다. (실시간 거래 로그) -> updateMinMaxForDayAndCreateRealTimeTransactionLog
        4. 체결이 이루어질 때마다 매수자/매도자 각각 보유 중인 매물의 수량을 업데이트한다.
        5. 체결이 이루어질 때마다 매도자가 보유 중인 예수금을 업데이트한다.(매수자는 이미 매수 주문을 내기 전에 예수금을 차감했으므로)
        6. 체결이 이루어질 때마다 매수자/매도자 각각 보유 중인 매물의 수량이 0이 되면 매도/매수 호가를 삭제한다.
        7. 체결이 이루어질 때마다 매수자/매도자 각각 보유 중인 매물의 수량이 0이 되면 매도/매수 주문을 체결 완료로 변경한다.
        8. 체결이 이루어질 때마다 체결이 되지 않고 남은 수량은 매수 주문을 낸 가격과 남은 수량으로 매수 호가를 올린다.
        9. 체결 된 매도 호가의 경우 BuyResponse의 List<PurchasedSellerQuoteInfoDto> purchasedSellQuotesInfoList;에 추가한다.
         */
        //1. 매수 호가의 가격보다 적거나 같은 매도 호가가 있다면 낮은 가격의 매도 호가부터 우선순위로 체결을 진행한다.
        List<Quote> sellQuotes = quoteRepository.findByPropertyAndTypeAndPriceLessThanEqualOrderByPriceAsc(property, "매도", price);

        //2. 언제까지? 매수 주문의 수량이 0이 될 때까지.
        int remainingQuantity = quantity;
        Optional<DayTransactionLog> dayTransactionLogOptional = dayTransactionLogRepository.findByPropertyIdAndDate(property.getId(), LocalDateTime.now().toLocalDate());
        DayTransactionLog dayTransactionLog = dayTransactionLogOptional.orElse(null);

        if (dayTransactionLog == null) {
            dayTransactionLog = DayTransactionLog.builder()
                    .date(LocalDateTime.now().toLocalDate())
                    .openingPrice(price)
                    .maxPrice(price)
                    .minPrice(price)
                    .build();
            dayTransactionLog.updateProperty(property);
        }
        List<RealTimeTransactionLog> realTimeTransactionLogs = new ArrayList<>();
        List<TradingDto.PurchasedSellerQuoteInfoDto> purchasedSellQuotesInfoList = new ArrayList<>();

        Inventory buyerInventory = null;
        for(Quote sellQuote : sellQuotes) {
            if (remainingQuantity == 0)
                break;
            int sellQuantity = sellQuote.getQuantity();
            int sellPrice = sellQuote.getPrice();
            int executedQuantity = Math.min(remainingQuantity, sellQuantity);
            remainingQuantity -= executedQuantity;
            User seller = sellQuote.getOrder().getUser();

            //3. 체결이 이루어질 때마다 체결된 수량과 가격을 기록한다. (실시간 거래 로그) -> updateMinMaxForDayAndCreateRealTimeTransactionLog
            updateMinMaxForDayAndCreateRealTimeTransactionLog(property, executedQuantity, sellPrice, dayTransactionLog, realTimeTransactionLogs);

            //4. 체결이 이루어질 때마다 매수자/매도자 각각 보유 중인 매물의 수량을 업데이트한다.
            buyerInventory = inventoryRepository.findByAccountAndProperty(buyer.getAccount(), property).orElseGet(
                    () -> Inventory.builder()
                            .quantity(0)
                            .averageBuyingPrice(0)
                            .sellableQuantity(0)
                            .earningsRate(0.0)
                            .build()
            );
            buyerInventory.increaseQuantity(executedQuantity);
            buyerInventory.increaseSellableQuantity(executedQuantity);
            buyerInventory.setEarningsRate(getFluctuationRate(buyerInventory.getAverageBuyingPrice(), sellPrice));

            Inventory sellerInventory = inventoryRepository.findByAccountAndProperty(seller.getAccount(), property)
                    .orElseThrow(() -> new TradingException(TradingErrorResult.INVENTORY_NOT_FOUND));
            sellerInventory.decreaseQuantity(executedQuantity);
            if(sellerInventory.getQuantity() <= 0){
                sellerInventory.freeInventory();
                inventoryRepository.delete(sellerInventory);
            }else {
                inventoryRepository.save(sellerInventory);
            }
            //5. 체결이 이루어질 때마다 매도자가 보유 중인 예수금을 업데이트한다.(매수자는 이미 매수 주문을 내기 전에 예수금을 차감했으므로)
            Account sellerAccount = sellQuote.getOrder().getUser().getAccount();
            sellerAccount.plusDeposit(Long.valueOf(executedQuantity * sellPrice));
            accountRepository.save(sellerAccount);

            //6. 체결이 이루어질 때마다 매수자/매도자 각각 보유 중인 매물의 수량이 0이 되면 매도/매수 호가를 삭제한다.
            if(sellQuantity == executedQuantity){
                Order sellOrder = sellQuote.getOrder();
                sellOrder.updateStatus("체결 완료");
                orderRepository.save(sellOrder);

                sellQuote.freeQuote();
                quoteRepository.delete(sellQuote);
            }else {
                Order newSellOrder = Order.builder()
                        .type("매도")
                        .quantity(Long.valueOf(executedQuantity))
                        .price(sellPrice)
                        .status("체결 완료")
                        .createdAt(LocalDateTime.now())
                        .build();
                newSellOrder.updateUser(sellQuote.getOrder().getUser());
                newSellOrder.updateProperty(property);
                orderRepository.save(newSellOrder);

                Order presentOrder = sellQuote.getOrder();
                presentOrder.updateQuantity(Long.valueOf(sellQuantity - executedQuantity));
                orderRepository.save(presentOrder);

                sellQuote.decreaseQuantity(executedQuantity);
                quoteRepository.save(sellQuote);
            }

            //8. 체결 된 매도 호가의 경우 BuyResponse의 List<PurchasedSellerQuoteInfoDto> purchasedSellQuotesInfoList;에 추가한다.
            purchasedSellQuotesInfoList.add(TradingDto.PurchasedSellerQuoteInfoDto.builder()
                    .sellerId(seller.getId().toString())
                    .sellerWalletAddress(seller.getWalletAddress())
                    .executedQuantity(executedQuantity)
                    .build());
        }

        //8. 체결이 되지 않고 남은 수량은 매수 주문을 낸 가격과 남은 수량으로 매수 호가를 올린다.
//        if(remainingQuantity > 0 && !sellQuotes.size() == 0) {
//            uploadBuyOrderAndQuote(buyer, property, remainingQuantity, price);
//        }

        if(remainingQuantity == 0){
            Order buyOrder = orderRepository.findByUserAndPropertyAndTypeAndPriceAndStatus(buyer, property, "매수", price, "체결 대기중");
            buyOrder.updateStatus("체결 완료");
            orderRepository.save(buyOrder);

            Quote buyQuote = buyOrder.getQuote();
            buyQuote.freeQuote();
            quoteRepository.delete(buyQuote);
        }

        //체결이 됐을 때만 저장
        if(!realTimeTransactionLogs.isEmpty()){
            dayTransactionLogRepository.save(dayTransactionLog);
            realTimeTransactionLogRepository.saveAll(realTimeTransactionLogs);
            if (buyerInventory != null)
                inventoryRepository.save(buyerInventory);
        }

        TradingDto.BuyResponse buyResponse = TradingDto.BuyResponse.builder()
                .buyerId(buyer.getId().toString())
                .walletAddress(buyer.getWalletAddress())
                .propertyId(property.getId().toString())
                .executedQuantity(quantity - remainingQuantity)
                .orderedPrice(price)
                .purchasedSellQuotesInfoList(purchasedSellQuotesInfoList)
                .build();
        return buyResponse;
    }
    @Transactional
    public TradingDto.SellResponse executeSellTransaction(UUID userId, UUID propertyId, TradingDto.SellRequest sellRequest) {
        final User seller = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorResult.USER_NOT_FOUND));
        final Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new UserException(UserErrorResult.USER_NOT_FOUND));
        int quantity = sellRequest.getQuantity();
        int price = sellRequest.getPrice();

        validateSellOrder(seller, property, quantity, price);

        //매도 주문을 내고 호가를 올림
        uploadSellOrderAndQuote(seller, property, quantity, price);

        TradingDto.SellResponse sellResponse = executeSellTransaction(seller, property, quantity, price);

        //실시간 체결 로그 flucutation_rate에 따른 해당 종목의 inventory 모든 수익률 업데이트 및
        //실시간 체결 로그 flucutation_rate에 따른 해당 종목의 inventory를 가지는 모든 유저 계정의 수익률 업데이트
        updateEarningsRateForAllAccountsByProperty(property);

        return sellResponse;
    }

    private TradingDto.SellResponse executeSellTransaction(User seller, Property property, int quantity, int price) {
        /*
        이 메서드에서 해야 할 책임
        1. 매도 호가의 가격보다 크거나 같은 매수 호가가 있다면 높은 가격의 매수 호가부터 우선순위로 체결을 진행한다.
        2. 언제까지? 매도 주문의 수량이 0이 될 때까지.
        3. 체결이 이루어질 때마다 체결된 수량과 가격을 기록한다. (실시간 거래 로그) -> updateMinMaxForDayAndCreateRealTimeTransactionLog
        4. 체결이 이루어질 때마다 매수자/매도자 각각 보유 중인 매물의 수량을 업데이트한다.
        5. 체결이 이루어질 때마다 매수자/매도자 각각 보유 중인 매물의 수량이 0이 되면 매도/매수 호가를 삭제한다.
        6. 체결이 이루어질 때마다 매수자/매도자 각각 보유 중인 매물의 수량이 0이 되면 매도/매수 주문을 체결 완료로 변경한다.
        7. 체결이 이루어질 때마다 체결이 되지 않고 남은 수량은 매도 주문을 낸 가격과 남은 수량으로 매도 호가를 올린다.
        8. 체결 된 매수 호가의 경우 SellResponse의 List<PurchasedSellerQuoteInfoDto> purchasedSellQuotesInfoList;에 추가한다.
         */

        //1. 매도 호가의 가격보다 크거나 같은 매수 호가가 있다면 높은 가격의 매수 호가부터 우선순위로 체결을 진행한다.
        List<Quote> buyQuotes = quoteRepository.findByPropertyAndTypeAndPriceGreaterThanEqualOrderByPriceDesc(property, "매수", price);

        //2. 언제까지? 매도 주문의 수량이 0이 될 때까지.
        int remainingQuantity = quantity;
        Optional<DayTransactionLog> dayTransactionLogOptional = dayTransactionLogRepository.findByPropertyIdAndDate(property.getId(), LocalDateTime.now().toLocalDate());
        DayTransactionLog dayTransactionLog = dayTransactionLogOptional.orElse(null);

        if (dayTransactionLog == null) {
            dayTransactionLog = DayTransactionLog.builder()
                    .date(LocalDateTime.now().toLocalDate())
                    .openingPrice(price)
                    .maxPrice(price)
                    .minPrice(price)
                    .build();
            dayTransactionLog.updateProperty(property);
        }
        List<RealTimeTransactionLog> realTimeTransactionLogs = new ArrayList<>();
        List<TradingDto.SoldBuyerQuoteInfoDto> soldBuyerQuoteInfoDtos = new ArrayList<>();
        Inventory sellerInventory = inventoryRepository.findByAccountAndProperty(seller.getAccount(), property)
                .orElseThrow(() -> new TradingException(TradingErrorResult.INVENTORY_NOT_FOUND));
        //판매자의 매물
        sellerInventory.decreaseSellableQuantity(quantity);
        if(sellerInventory.getSellableQuantity() < 0){
            throw new TradingException(TradingErrorResult.NOT_ENOUGH_INVENTORY);
        }
        inventoryRepository.save(sellerInventory);

        for(Quote buyQuote : buyQuotes) {
            if (remainingQuantity == 0)
                break;
            int buyQuantity = buyQuote.getQuantity();
            int buyPrice = buyQuote.getPrice();
            int executedQuantity = Math.min(remainingQuantity, buyQuantity);
            remainingQuantity -= executedQuantity;

            User buyer = buyQuote.getOrder().getUser();

            //3. 체결이 이루어질 때마다 체결된 수량과 가격을 기록한다. (실시간 거래 로그) -> updateMinMaxForDayAndCreateRealTimeTransactionLog
            updateMinMaxForDayAndCreateRealTimeTransactionLog(property, executedQuantity, buyPrice, dayTransactionLog, realTimeTransactionLogs);

            //4. 체결이 이루어질 때마다 매수자/매도자 각각 보유 중인 매물의 수량을 업데이트한다.
            sellerInventory.decreaseQuantity(executedQuantity);
            sellerInventory.setEarningsRate(getFluctuationRate(sellerInventory.getAverageBuyingPrice(), buyPrice));

            Inventory buyerInventory = inventoryRepository.findByAccountAndProperty(buyer.getAccount(), property)
                    .orElseThrow(() -> new TradingException(TradingErrorResult.INVENTORY_NOT_FOUND));
            buyerInventory.decreaseQuantity(executedQuantity);

            if (sellerInventory.getQuantity() <= 0) {
                sellerInventory.freeInventory();
                inventoryRepository.delete(sellerInventory);
            } else {
                inventoryRepository.save(sellerInventory);
            }

            //5. 체결이 이루어질 때마다 매도자가 보유 중인 예수금을 업데이트한다.(매수자는 이미 매수 주문을 내기 전에 예수금을 차감했으므로)
            Account sellerAccount = seller.getAccount();
            sellerAccount.plusDeposit(Long.valueOf(executedQuantity * buyPrice));
            accountRepository.save(sellerAccount);

            //6. 체결이 이루어질 때마다 매수자/매도자 각각 보유 중인 매물의 수량이 0이 되면 매도/매수 호가를 삭제한다.
            if (buyQuantity == executedQuantity) {
                Order buyOrder = buyQuote.getOrder();
                buyOrder.updateStatus("체결 완료");
                orderRepository.save(buyOrder);

                buyQuote.freeQuote();
                quoteRepository.delete(buyQuote);
            } else {
                //체결한 수량만큼 체결 완료, 남은 수량은 매수 호가의 수량을 업데이트
                Order newBuyOrder = Order.builder()
                        .type("매수")
                        .quantity(Long.valueOf(executedQuantity))
                        .price(buyPrice)
                        .status("체결 완료")
                        .createdAt(LocalDateTime.now())
                        .build();
                newBuyOrder.updateUser(buyQuote.getOrder().getUser());
                newBuyOrder.updateProperty(property);
                orderRepository.save(newBuyOrder);

                Order presentOrder = buyQuote.getOrder();
                presentOrder.updateQuantity(Long.valueOf(buyQuantity - executedQuantity));
                orderRepository.save(presentOrder);

                buyQuote.decreaseQuantity(executedQuantity);
                quoteRepository.save(buyQuote);
            }

            //8. 체결 된 매수 호가의 경우 SellResponse의 List<SoldBuyerQuoteInfoDto> soldBuyerQuoteInfoDtos;에 추가한다.
            soldBuyerQuoteInfoDtos.add(TradingDto.SoldBuyerQuoteInfoDto.builder()
                    .buyerId(buyer.getId().toString())
                    .buyerWalletAddress(buyer.getWalletAddress())
                    .executedQuantity(executedQuantity)
                    .build());

        }

        //7. 체결이 되지 않고 남은 수량은 매도 주문을 낸 가격과 남은 수량으로 매도 호가를 올린다.
//        if(remainingQuantity > 0 && !buyQuotes.size() == 0) {
//            uploadSellOrderAndQuote(seller, property, remainingQuantity, price);
//        }

        //체결이 됐을 때만 저장
        if(!realTimeTransactionLogs.isEmpty()){
            dayTransactionLogRepository.save(dayTransactionLog);
            realTimeTransactionLogRepository.saveAll(realTimeTransactionLogs);
            inventoryRepository.save(sellerInventory);
        }
        if(remainingQuantity == 0){
            Order sellOrder = orderRepository.findByUserAndPropertyAndTypeAndPriceAndStatus(seller, property, "매도", price, "체결 대기중");
            sellOrder.updateStatus("체결 완료");
            orderRepository.save(sellOrder);

            Quote sellQuote = sellOrder.getQuote();
            sellQuote.freeQuote();
            quoteRepository.delete(sellQuote);
        }
        TradingDto.SellResponse sellResponse = TradingDto.SellResponse.builder()
                .sellerId(seller.getId().toString())
                .walletAddress(seller.getWalletAddress())
                .propertyId(property.getId().toString())
                .executedQuantity(quantity - remainingQuantity)
                .orderedPrice(price)
                .soldBuyerQuotesInfoList(soldBuyerQuoteInfoDtos)
                .build();

        return sellResponse;
    }
}
