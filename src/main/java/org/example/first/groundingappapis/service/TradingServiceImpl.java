package org.example.first.groundingappapis.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.first.groundingappapis.dto.OrderDto;
import org.example.first.groundingappapis.dto.QuoteDto;
import org.example.first.groundingappapis.dto.TradingDto;
import org.example.first.groundingappapis.entity.*;
import org.example.first.groundingappapis.exception.*;
import org.example.first.groundingappapis.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class TradingServiceImpl implements TradingService {

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
    @Override
    public TradingDto.BuyResponse uploadBuyingOrderOnQuote(User buyer, UUID propertyId, TradingDto.BuyRequest buyRequest) {
        final Account buyerAccount = accountRepository.findByUser(buyer).orElseThrow(() ->
                new TradingException(TradingErrorResult.ACCOUNT_NOT_FOUND));
        final Property property = propertyRepository.findById(propertyId).orElseThrow(() ->
                new PropertyException(PropertyErrorResult.PROPERTY_NOT_FOUND));
        TradingDto.BuyResponse response;

        if (buyRequest.getPrice() * buyRequest.getQuantity() > buyerAccount.getDeposit()) {
            throw new TradingException(TradingErrorResult.NOT_ENOUGH_DEPOSIT);
        }
        if(!isEnoughFundraise(property)){
            throw new TradingException(TradingErrorResult.NOT_ENOUGH_FUNDRAISE);
        }
        List<TradingDto.PurchasedSellerQuoteInfoDto> purchasedSellQuotesInfoList;

        //매수자 예수금 차감
        buyerWithdraw(buyerAccount, buyRequest.getPrice(), buyRequest.getQuantity());
        accountRepository.save(buyerAccount);

        if (quoteRepository.existsByPropertyAndPriceLessThanEqualWithOrderStatus(property, buyRequest.getPrice())) {
            LocalDate today = LocalDate.now();

            DayTransactionLog dayTransactionLog = dayTransactionLogRepository.findRecentDayTransactionLogByPropertyAndToday(property.getId(), today).orElseGet(() -> {
                RealTimeTransactionLog realTimeTransactionLog = realTimeTransactionLogRepository.findFirstByPropertyIdOrderByExecutedAtDesc(propertyId).orElseThrow(() ->
                        new TradingException(TradingErrorResult.TRADING_NOT_FOUND));

                DayTransactionLog newDayTransactionLog = DayTransactionLog.builder()
                        .date(LocalDate.now())
                        .openingPrice(buyRequest.getPrice())
                        .closingPrice(realTimeTransactionLog.getExecutedPrice()) //TODO
                        .maxPrice(buyRequest.getPrice())
                        .minPrice(buyRequest.getPrice())
                        .build();
                newDayTransactionLog.updateProperty(property);
                dayTransactionLogRepository.save(newDayTransactionLog);
                return newDayTransactionLog;
            });

            //체결 진행
            TradingDto.ExecuteBuyTransactionResponse executeBuyTransactionResponse = executeBuyTransaction(property, buyer, buyRequest, dayTransactionLog);

            purchasedSellQuotesInfoList = executeBuyTransactionResponse.getPurchasedSellerQuoteInfoList();

             //체결 후작업
            int totalExecutedQuantity = executeBuyTransactionResponse.getTotalExecutedQuantity();

            buyerAccount.setAverageEarningRate(inventoryRepository.getAverageEarningRateByAccount(buyerAccount.getId()));
            accountRepository.save(buyerAccount);

            property.increaseTotalVolume(totalExecutedQuantity);
            propertyRepository.save(property);

            response = TradingDto.BuyResponse.builder()
                    .buyerId(buyer.getId().toString())
                    .walletAddress(buyer.getWalletAddress())
                    .propertyId(propertyId.toString())
                    .executedQuantity(totalExecutedQuantity)
                    .orderedPrice(buyRequest.getPrice())
                    .purchasedSellQuotesInfoList(purchasedSellQuotesInfoList)
                    .build();

            //해당 종목의 inventory 모든 수익률 업데이트
            inventoryRepository.findAllByProperty(property).stream().forEach(inventory -> {
                inventory.setEarningsRate(Double.valueOf((inventory.getAverageBuyingPrice() - buyRequest.getPrice()) / buyRequest.getPrice() * 100));
                inventoryRepository.save(inventory);
            });

        } else {
            Quote quote = Quote.builder()
                    .type("매수")
                    .price(buyRequest.getPrice())
                    .quantity(buyRequest.getQuantity())
                    .createdAt(LocalDateTime.now())
                    .build();
            quote.updateProperty(property);
            quote.updateAccount(buyerAccount);
            quoteRepository.save(quote);

            saveOrderForUploader(buyer, buyRequest.getPrice(), 0,  "매수", property);

            Order order = Order.builder()
                    .type("매수")
                    .price(buyRequest.getPrice())
                    .quantity(Long.valueOf(buyRequest.getQuantity()))
                    .status("체결 대기중")
                    .createdAt(LocalDateTime.now())
                    .build();

            order.updateProperty(property);
            order.updateUser(buyer);
            orderRepository.save(order);

            response = TradingDto.BuyResponse.builder()
                    .buyerId(buyer.getId().toString())
                    .walletAddress(buyer.getWalletAddress())
                    .propertyId(propertyId.toString())
                    .executedQuantity(0)
                    .orderedPrice(buyRequest.getPrice())
                    .purchasedSellQuotesInfoList(new ArrayList<>())
                    .build();
        }

        return response;
    }
    @Transactional
    public TradingDto.ExecuteBuyTransactionResponse executeBuyTransaction(Property property, User buyer, TradingDto.BuyRequest buyRequest, DayTransactionLog dayTransactionLog) {
        List<TradingDto.PurchasedSellerQuoteInfoDto> purchasedSellQuotesInfoList = new ArrayList<>();

        Quote sellQuoteOptional = null;

        Account buyerAccount = buyer.getAccount();

        int totalExecutedQuantity = 0;

        while (buyRequest.getQuantity() > 0) {
            sellQuoteOptional = quoteRepository.findFirstByPropertyAndPriceLessThanEqualAndTypeOrderByPriceAsc
                    (property.getId(), buyRequest.getPrice(), "매도");

            if (sellQuoteOptional == null) {
                break;
            }

            final Quote sellQuote = sellQuoteOptional;
            final User seller = sellQuote.getAccount().getUser();
            final Account sellerAccount = seller.getAccount();

            int executedQuantity = Math.min(buyRequest.getQuantity(), sellQuote.getQuantity());
            totalExecutedQuantity += executedQuantity;

            int executedPrice = sellQuote.getPrice();

            int remainingQuantity = buyRequest.getQuantity() - executedQuantity;

            buyRequest.setQuantity(remainingQuantity);
            sellQuote.setQuantity(remainingQuantity);

            if (remainingQuantity > 0)
                quoteRepository.save(sellQuote);
            else
                quoteRepository.delete(sellQuote);

            //실시간 체결 로그 저장 및 일일 로그 업데이트
            saveTransactionLog(property, executedQuantity, executedPrice, buyer, dayTransactionLog);
            sellerDeposit(sellerAccount, executedQuantity, executedPrice);

            //매도자측 보유종목 업데이트, 매수자쪽 보유종목 업데이트는 바깥쪽에서.
            Inventory sellerInventory = inventoryRepository.findByAccountAndProperty(sellerAccount, property).orElseThrow(() ->
                    new TradingException(TradingErrorResult.INVENTORY_NOT_FOUND));

            sellerInventory.setQuantity(sellerInventory.getQuantity() - executedQuantity);

            if(sellerInventory.getQuantity() == 0) {
                sellerInventory.setAverageBuyingPrice(0);
            } else {
                sellerInventory.setAverageBuyingPrice((sellerInventory.getAverageBuyingPrice() * sellerInventory.getQuantity() + executedPrice * executedQuantity) / (sellerInventory.getQuantity() + executedQuantity));
            }

            if(sellerInventory.getQuantity() <= 0)
                inventoryRepository.delete(sellerInventory);
            else
                inventoryRepository.save(sellerInventory);

            TradingDto.PurchasedSellerQuoteInfoDto purchasedSellerQuoteInfoDto = TradingDto.PurchasedSellerQuoteInfoDto.builder()
                    .sellerId(seller.getId().toString())
                    .sellerWalletAddress(seller.getWalletAddress())
                    .executedQuantity(executedQuantity)
                    .build();

            purchasedSellQuotesInfoList.add(purchasedSellerQuoteInfoDto);

            //update min, max price of day transaction log
            if(executedPrice > dayTransactionLog.getMaxPrice())
                dayTransactionLog.updateMaxPrice(executedPrice);
            else if(executedPrice < dayTransactionLog.getMinPrice())
                dayTransactionLog.updateMinPrice(executedPrice);
        }

        int remaininQuantity = buyRequest.getQuantity() - totalExecutedQuantity;

        //마지막 매도호가에 대해
        final Quote sellQuote = sellQuoteOptional;
        if (sellQuote.getQuantity() <= 0) {
            quoteRepository.delete(sellQuote);
            if(sellQuote.getQuantity() < 0){
                Quote newBuyingQuote = Quote.builder()
                        .type("매수")
                        .price(buyRequest.getPrice())
                        .quantity(remaininQuantity)
                        .createdAt(LocalDateTime.now())
                        .build();

                newBuyingQuote.updateProperty(property);
                newBuyingQuote.updateAccount(buyer.getAccount());
                quoteRepository.save(newBuyingQuote);
            }
        }

        //매도자 주문 업데이트
        purchasedSellQuotesInfoList.stream().forEach(purchasedSellerQuoteInfoDto -> {
            final User seller = userRepository.findById(UUID.fromString(purchasedSellerQuoteInfoDto.getSellerId())).orElseThrow(() ->
                    new UserException(UserErrorResult.USER_NOT_FOUND));
            saveOrder(seller, property, buyRequest.getPrice(), purchasedSellerQuoteInfoDto.getExecutedQuantity(), "매도");
        });

        //매수자 주문 업데이트
        saveOrderForUploader(buyer, buyRequest.getPrice(), totalExecutedQuantity, "매수", property);

        if(buyRequest.getQuantity() > totalExecutedQuantity){
            Quote quote = Quote.builder()
                    .type("매수")
                    .price(buyRequest.getPrice())
                    .quantity(buyRequest.getQuantity() - totalExecutedQuantity)
                    .createdAt(LocalDateTime.now())
                    .build();
            quote.updateProperty(property);
            quote.updateAccount(buyer.getAccount());
            quoteRepository.save(quote);
        }

        //매수자 보유종목 업데이트
        Inventory inventory = inventoryRepository.findByAccountAndProperty(buyerAccount, property).orElseGet(() -> {
            Inventory newInventory = Inventory.builder()
                    .quantity(0)
                    .sellableQuantity(0)
                    .averageBuyingPrice(0)
                    .build();
            newInventory.updateAccount(buyerAccount);
            newInventory.updateProperty(property);
            return newInventory;
        });

        //매수자쪽 inventory 업데이트는 한번에
        inventory.setSellableQuantity(inventory.getSellableQuantity() + totalExecutedQuantity);
        inventory.setQuantity(inventory.getQuantity() + totalExecutedQuantity);

        //0 보정
        if(inventory.getQuantity() == 0) {
            inventory.setAverageBuyingPrice(0);
        }else{
            inventory.setAverageBuyingPrice((inventory.getAverageBuyingPrice() * inventory.getQuantity() + buyRequest.getPrice() * totalExecutedQuantity) / (inventory.getQuantity() + totalExecutedQuantity));
        }
        inventoryRepository.save(inventory);

        TradingDto.ExecuteBuyTransactionResponse executeBuyTransactionResponse = TradingDto.ExecuteBuyTransactionResponse.builder()
                .totalExecutedQuantity(totalExecutedQuantity)
                .purchasedSellerQuoteInfoList(purchasedSellQuotesInfoList)
                .build();

        return executeBuyTransactionResponse;
    }
    @Transactional
    public void saveOrderForUploader(User user, int price, int totalExecutedQuantity, String type, Property property) {
        List<Optional<Order>> orders = orderRepository.findByUserAndPropertyAndPriceAndTypeAndStatus(user, property, price, type, "체결 대기중");
        if(orders.isEmpty()){
            Order newOrder = Order.builder()
                    .type(type)
                    .price(price)
                    .quantity(Long.valueOf(totalExecutedQuantity))
                    .status("체결 대기중")
                    .createdAt(LocalDateTime.now())
                    .build();

            newOrder.updateProperty(property);
            newOrder.updateUser(user);
            orderRepository.save(newOrder);
        } else {
            int idx = 0;
            while (totalExecutedQuantity > 0) {
                //체결 대기중인 주문들
                Order order = orders.get(idx).orElseThrow(() -> new TradingException(TradingErrorResult.ORDER_NOT_FOUND));
                long remainingQuantity = order.getQuantity() - totalExecutedQuantity;

                if (remainingQuantity <= 0) {
                    Order completedOrder = Order.builder()
                            .type(type)
                            .price(price)
                            .quantity(Long.valueOf(order.getQuantity()))
                            .status("체결 완료")
                            .createdAt(LocalDateTime.now())
                            .build();
                    completedOrder.updateProperty(property);
                    completedOrder.updateUser(user);
                    orderRepository.save(completedOrder);

                    orderRepository.delete(order);

                    totalExecutedQuantity -= order.getQuantity();
                } else {
                    Order completedOrder = Order.builder()
                            .type(type)
                            .price(price)
                            .quantity(Long.valueOf(remainingQuantity * -1))
                            .status("체결 완료")
                            .createdAt(LocalDateTime.now())
                            .build();
                    orderRepository.save(completedOrder);

                    order.updateQuantity(remainingQuantity);
                    orderRepository.save(order);

                    totalExecutedQuantity = 0;
                }
                idx++;
            }

//            int T = totalExecutedQuantity;
//            while (T > 0) {
//                //체결 대기중인 주문들
//                Order order = orders.poll().orElseThrow(() -> new TradingException(TradingErrorResult.ORDER_NOT_FOUND));
//                long remainingQuantity = order.getQuantity() - T;
//
//                if (remainingQuantity <= 0) {
//                    Order completedOrder = Order.builder()
//                            .type(type)
//                            .price(price)
//                            .quantity(Long.valueOf(totalExecutedQuantity))
//                            .status("체결 완료")
//                            .createdAt(LocalDateTime.now())
//                            .build();
//                    completedOrder.updateProperty(property);
//                    completedOrder.updateUser(user);
//                    orderRepository.save(completedOrder);
//
//                    orderRepository.delete(order);
//
//                    T -= order.getQuantity();
//                } else {
//                    Order completedOrder = Order.builder()
//                            .type(type)
//                            .price(price)
//                            .quantity(Long.valueOf(totalExecutedQuantity))
//                            .status("체결 완료")
//                            .createdAt(LocalDateTime.now())
//                            .build();
//                    orderRepository.save(completedOrder);
//
//                    order.updateQuantity(remainingQuantity);
//                    orderRepository.save(order);
//
//                    T -=
//                }
//            }
        }

//        if (totalExecutedQuantity > 0) {
//            long remainingQuantity = order.getQuantity() - totalExecutedQuantity;
//
//            if (remainingQuantity <= 0) {
//                Order completedOrder = Order.builder()
//                        .type(type)
//                        .price(price)
//                        .quantity(Long.valueOf(totalExecutedQuantity))
//                        .status("체결 완료")
//                        .createdAt(LocalDateTime.now())
//                        .build();
//                completedOrder.updateProperty(property);
//                completedOrder.updateUser(user);
//                orderRepository.save(completedOrder);
//
//                orderRepository.delete(order);
//            } else {
//                Order completedOrder = Order.builder()
//                        .type(type)
//                        .price(price)
//                        .quantity(Long.valueOf(totalExecutedQuantity))
//                        .status("체결 완료")
//                        .createdAt(LocalDateTime.now())
//                        .build();
//                orderRepository.save(completedOrder);
//
//                order.updateQuantity(remainingQuantity);
//                orderRepository.save(order);
//            }
//        }

    }
    @Transactional
    public void saveOrder(User user, Property property, int price, int executedQuantity, String type) {
        // 기존 주문 조회 TODO: 여기 index out of range 뜸 조건문 몇개 더 넣어야할듯? 이거 호출하는 부분부터 잘못됐을 확률 높음
        List<Optional<Order>> orders = orderRepository.findByUserAndPropertyAndPriceAndTypeAndStatus(user, property, price, type, "체결 대기중");

        if(orders.size() == 0){
            Order newOrder = Order.builder()
                    .type(type)
                    .price(price)
                    .quantity(Long.valueOf(executedQuantity))
                    .status("체결 대기중")
                    .createdAt(LocalDateTime.now())
                    .build();
            newOrder.updateProperty(property);
            newOrder.updateUser(user);
            orderRepository.save(newOrder);
        } else {
            Deque<Optional<Order>> orderDeque = new ArrayDeque<>(orders);
            while (!orderDeque.isEmpty()){
                //체결 대기중인 주문들
                Order order = orderDeque.poll().orElseThrow(() -> new TradingException(TradingErrorResult.ORDER_NOT_FOUND));
                long remainingQuantity = order.getQuantity() - executedQuantity;

                if (remainingQuantity <= 0) {
                    Order completedOrder = Order.builder()
                            .type(type)
                            .price(price)
                            .quantity(Long.valueOf(order.getQuantity()))
                            .status("체결 완료")
                            .createdAt(LocalDateTime.now())
                            .build();
                    completedOrder.updateProperty(property);
                    completedOrder.updateUser(user);
                    orderRepository.save(completedOrder);

                    orderRepository.delete(order);

                    executedQuantity -= order.getQuantity();
                } else {
                    Order completedOrder = Order.builder()
                            .type(type)
                            .price(price)
                            .quantity(Long.valueOf(executedQuantity))
                            .status("체결 완료")
                            .createdAt(LocalDateTime.now())
                            .build();
                    orderRepository.save(completedOrder);

                    order.updateQuantity(remainingQuantity);
                    orderRepository.save(order);

                    executedQuantity = 0;
                }
            }
        }
    }

    @Transactional
    @Override
    public TradingDto.SellResponse uploadSellingOrderOnQuote(User user, UUID propertyId, TradingDto.SellRequest sellRequest) {
        final Account sellerAccount = accountRepository.findByUser(user).orElseThrow(() ->
                new TradingException(TradingErrorResult.ACCOUNT_NOT_FOUND));
        final Property property = propertyRepository.findById(propertyId).orElseThrow(() ->
                new PropertyException(PropertyErrorResult.PROPERTY_NOT_FOUND));
        if(!isEnoughFundraise(property)){
            throw new TradingException(TradingErrorResult.NOT_ENOUGH_FUNDRAISE);
        }
        final Inventory inventory = inventoryRepository.findByAccountAndProperty(sellerAccount, property).orElseThrow(() ->
                new TradingException(TradingErrorResult.INVENTORY_NOT_FOUND));

        if(sellRequest.getQuantity() > inventory.getSellableQuantity()){
            throw new TradingException(TradingErrorResult.NOT_ENOUGH_SELLABLE_QUANTITY);
        }

        TradingDto.SellResponse response;

        //매수 호가 정보 리스트
        List<TradingDto.SoldBuyerQuoteInfoDto> soldBuyerQuotesInfoList;

        if (quoteRepository.existsByPropertyAndPriceGreaterThanEqual(property, sellRequest.getPrice())) {
            LocalDate today = LocalDate.now();

            //오늘자 dayTransactionLog가 없으면 생성
            DayTransactionLog dayTransactionLog = dayTransactionLogRepository.findRecentDayTransactionLogByPropertyAndToday(property.getId(), today).orElseGet(() -> {
                RealTimeTransactionLog realTimeTransactionLog = realTimeTransactionLogRepository.findFirstByPropertyIdOrderByExecutedAtDesc(propertyId).orElseThrow(() ->
                        new TradingException(TradingErrorResult.TRADING_NOT_FOUND));

                DayTransactionLog newDayTransactionLog = DayTransactionLog.builder()
                        .date(LocalDate.now())
                        .openingPrice(sellRequest.getPrice())
                        .closingPrice(realTimeTransactionLog.getExecutedPrice())
                        .maxPrice(sellRequest.getPrice())
                        .minPrice(sellRequest.getPrice())
                        .build();

                newDayTransactionLog.updateProperty(property);
                dayTransactionLogRepository.save(newDayTransactionLog);
                return newDayTransactionLog;
            });
            //체결 진행
            TradingDto.ExecuteSellTransactionResponse executeSellTransactionResponse = executeSellTransaction(property, user, sellRequest, dayTransactionLog);
            soldBuyerQuotesInfoList = executeSellTransactionResponse.getSoldBuyerQuoteInfoList();

            //체결 후 작업
            int totalExecutedQuantityOfOrder = executeSellTransactionResponse.getTotalExecutedQuantity();

            sellerAccount.setAverageEarningRate(inventoryRepository.getAverageEarningRateByAccount(sellerAccount.getId()));
            //매도자 예수금 증가
            sellerDeposit(sellerAccount, totalExecutedQuantityOfOrder, sellRequest.getPrice());
            accountRepository.save(sellerAccount);

            property.increaseTotalVolume(totalExecutedQuantityOfOrder);
            propertyRepository.save(property);

            response = TradingDto.SellResponse.builder()
                    .sellerId(user.getId().toString())
                    .walletAddress(user.getWalletAddress())
                    .propertyId(propertyId.toString())
                    .executedQuantity(totalExecutedQuantityOfOrder)
                    .orderedPrice(sellRequest.getPrice())
                    .soldBuyerQuotesInfoList(soldBuyerQuotesInfoList)
                    .build();

            //해당 종목의 inventory 모든 수익률 업데이트
            inventoryRepository.findAllByProperty(property).stream().forEach(it -> {
                it.setEarningsRate(Double.valueOf((it.getAverageBuyingPrice() - sellRequest.getPrice()) / sellRequest.getPrice() * 100));
                inventoryRepository.save(it);
            });
            return response;
        } else {
            Quote quote = Quote.builder()
                    .type("매도")
                    .price(sellRequest.getPrice())
                    .quantity(sellRequest.getQuantity())
                    .createdAt(LocalDateTime.now())
                    .build();
            quote.updateProperty(property);
            quote.updateAccount(sellerAccount);
            quoteRepository.save(quote);

            saveOrderForUploader(user, 0, 0, "매도", property);

            Order order = Order.builder()
                    .type("매도")
                    .price(sellRequest.getPrice())
                    .quantity(Long.valueOf(sellRequest.getQuantity()))
                    .status("체결 대기중")
                    .createdAt(LocalDateTime.now())
                    .build();

            response = TradingDto.SellResponse.builder()
                    .sellerId(user.getId().toString())
                    .walletAddress(user.getWalletAddress())
                    .propertyId(propertyId.toString())
                    .executedQuantity(0)
                    .orderedPrice(sellRequest.getPrice())
                    .soldBuyerQuotesInfoList(new ArrayList<>())
                    .build();

        }
        return response;
    }
    @Override
    public Double getFluctuationRate(int openingPrice, int executedPrice) {
        if (openingPrice == 0) {
            return 0.0;
        }
        return ((double) (executedPrice - openingPrice) / openingPrice) * 100;
    }

    @Transactional(readOnly = true)
    @Override
    public OrderDto.GetQuantityOfInventoryResponse getQuantityOfInventory(User user, UUID propertyId) {
        final Account account = accountRepository.findByUser(user).orElseThrow(() ->
                new TradingException(TradingErrorResult.ACCOUNT_NOT_FOUND));
        final Property property = propertyRepository.findById(propertyId).orElseThrow(() ->
                new PropertyException(PropertyErrorResult.PROPERTY_NOT_FOUND));

        Optional<Inventory> inventory = inventoryRepository.findByAccountAndProperty(account, property);

        int quantity = 0;

        if(inventory.isPresent()){
            quantity = inventory.get().getQuantity();
        }

        OrderDto.GetQuantityOfInventoryResponse response = OrderDto.GetQuantityOfInventoryResponse.builder()
                .propertyId(propertyId.toString())
                .quantity(quantity)
                .createdAt(LocalDateTime.now())
                .build();

        return response;
    }

    @Transactional
    public TradingDto.ExecuteSellTransactionResponse executeSellTransaction(Property property, User seller, TradingDto.SellRequest sellRequest, DayTransactionLog dayTransactionLog) {
        List<TradingDto.SoldBuyerQuoteInfoDto> soldBuyerQuotesInfoList = new ArrayList<>();
        Quote buyQuoteOptional = null;

        Account sellerAccount = seller.getAccount();

        int totalExecutedQuantity = 0;
        while (sellRequest.getQuantity() > 0) {
            buyQuoteOptional = quoteRepository.findFirstByPropertyAndPriceGreaterThanEqualOrderByPriceDesc
                    (property.getId(), sellRequest.getPrice(), "매수");

            if (buyQuoteOptional == null) {
                break;
            }

            final Quote buyQuote = buyQuoteOptional;
            final User buyer = buyQuote.getAccount().getUser();
            final Account buyerAccount = buyer.getAccount();

            int executedQuantity = Math.min(sellRequest.getQuantity(), buyQuote.getQuantity());
            totalExecutedQuantity += executedQuantity;

            int executedPrice = buyQuote.getPrice();

            int remainingQuantity = sellRequest.getQuantity() - executedQuantity;

            sellRequest.setQuantity(remainingQuantity);
            buyQuote.setQuantity(remainingQuantity);

            if (remainingQuantity > 0)
                quoteRepository.save(buyQuote);
            else
                quoteRepository.delete(buyQuote);

            //실시간 체결 로그 저장 및 일일 로그 업데이트
            saveTransactionLog(property, executedQuantity, executedPrice, seller, dayTransactionLog);

            //매수자측 inventory 업데이트, 매도자쪽 보유종목 업데이트는 메소드 바깥쪽에서
            Inventory buyerInventory = inventoryRepository.findByAccountAndProperty(buyerAccount, property).orElseGet(() -> {
                Inventory newInventory = Inventory.builder()
                        .quantity(0)
                        .sellableQuantity(0)
                        .averageBuyingPrice(0)
                        .build();
                newInventory.updateAccount(buyerAccount);
                newInventory.updateProperty(property);
                return newInventory;
            });

            buyerInventory.setQuantity(buyerInventory.getQuantity() + executedQuantity);
            //매수자쪽은 sellable도 수량 늘려야함
            buyerInventory.setSellableQuantity(buyerInventory.getSellableQuantity() + executedQuantity);
            inventoryRepository.save(buyerInventory);

            TradingDto.SoldBuyerQuoteInfoDto soldBuyerQuoteInfoDto = TradingDto.SoldBuyerQuoteInfoDto.builder()
                    .buyerId(buyer.getId().toString())
                    .buyerWalletAddress(buyer.getWalletAddress())
                    .executedQuantity(executedQuantity)
                    .build();

            soldBuyerQuotesInfoList.add(soldBuyerQuoteInfoDto);
        }

        int remaininQuantity = sellRequest.getQuantity() - totalExecutedQuantity;

        //마지막 매수호가에 대해
        final Quote buyQuote = buyQuoteOptional;
        if (buyQuote.getQuantity() <= 0) {
            quoteRepository.delete(buyQuote);
            if(buyQuote.getQuantity() < 0){
                Quote newSellingQuote = Quote.builder()
                        .type("매도")
                        .price(sellRequest.getPrice())
                        .quantity(remaininQuantity)
                        .createdAt(LocalDateTime.now())
                        .build();

                newSellingQuote.updateProperty(property);
                newSellingQuote.updateAccount(seller.getAccount());
                quoteRepository.save(newSellingQuote);
            }
        }

        //매수자 주문 업데이트 from soldBuyerQuotesInfoList
        soldBuyerQuotesInfoList.stream().forEach(soldBuyerQuoteInfoDto -> {
            final User buyer = userRepository.findById(UUID.fromString(soldBuyerQuoteInfoDto.getBuyerId())).orElseThrow(() ->
                    new UserException(UserErrorResult.USER_NOT_FOUND));
            saveOrder(buyer, property, sellRequest.getPrice(), soldBuyerQuoteInfoDto.getExecutedQuantity(),"매수");
        });

        //매도자 주문 업데이트
        saveOrderForUploader(seller, sellRequest.getPrice(), totalExecutedQuantity, "매도", property);

        if(sellRequest.getQuantity() > totalExecutedQuantity){
            Quote quote = Quote.builder()
                    .type("매도")
                    .price(sellRequest.getPrice())
                    .quantity(sellRequest.getQuantity() - totalExecutedQuantity)
                    .createdAt(LocalDateTime.now())
                    .build();
            quote.updateProperty(property);
            quote.updateAccount(seller.getAccount());
            quoteRepository.save(quote);
        }

        //매도자 보유종목 업데이트
        Inventory inventory = inventoryRepository.findByAccountAndProperty(sellerAccount, property).orElseThrow(() ->
                new TradingException(TradingErrorResult.INVENTORY_NOT_FOUND));

        //매도자쪽 inventory 업데이트는 한번에
        inventory.setSellableQuantity(inventory.getSellableQuantity() - totalExecutedQuantity);
        inventory.setQuantity(inventory.getQuantity() - totalExecutedQuantity);

        if(inventory.getQuantity() <= 0)
            inventoryRepository.delete(inventory);
        else
            inventoryRepository.save(inventory);
        inventoryRepository.save(inventory);

        TradingDto.ExecuteSellTransactionResponse executeSellTransactionResponse = TradingDto.ExecuteSellTransactionResponse.builder()
                .totalExecutedQuantity(totalExecutedQuantity)
                .soldBuyerQuoteInfoList(soldBuyerQuotesInfoList)
                .build();

        return executeSellTransactionResponse;
    }

    @Transactional
    public void saveTransactionLog(Property property, int executedQuantity, int executedPrice, User user, DayTransactionLog dayTransactionLog) {
        RealTimeTransactionLog realTimeTransactionLog = RealTimeTransactionLog.builder()
                .quantity(executedQuantity)
                .executedPrice(executedPrice)
                .executedAt(LocalDateTime.now())
                .fluctuationRate(getFluctuationRate(dayTransactionLog.getOpeningPrice(), executedPrice))
                .build();

        realTimeTransactionLog.updateProperty(property);
        realTimeTransactionLogRepository.save(realTimeTransactionLog);

        if(executedPrice > dayTransactionLog.getMaxPrice())
            dayTransactionLog.updateMaxPrice(executedPrice);
        else if(executedPrice < dayTransactionLog.getMinPrice())
            dayTransactionLog.updateMinPrice(executedPrice);

        dayTransactionLog.increaseVolumeCount(executedQuantity);

        dayTransactionLogRepository.save(dayTransactionLog);
    }

    @Transactional
    public void sellerDeposit(Account sellerAccount, int executedQuantity, int executedPrice) {
        sellerAccount.plusDeposit(Long.valueOf(executedQuantity * executedPrice));
    }

    @Transactional
    public void buyerWithdraw(Account buyerAccount, int executedQuantity, int executedPrice) {
        buyerAccount.minusDeposit(Long.valueOf(executedQuantity * executedPrice));
    }

    @Transactional(readOnly = true)
    @Override
    public OrderDto.GetTotalPriceResponse getTotalPrice(UUID propertyId, int quantity) {
        int totalPrice = 0;
        //realtime transaction log가 있으면 해당 가격을 가져와서 계산, 없으면 property의 fundraise의 securityprice 가격을 가져와서 계산

        if(realTimeTransactionLogRepository.existsByPropertyId(propertyId)) {
            RealTimeTransactionLog realTimeTransactionLog = realTimeTransactionLogRepository.findFirstByPropertyIdOrderByExecutedAtDesc(propertyId).orElseThrow(() ->
                    new TradingException(TradingErrorResult.TRADING_NOT_FOUND));
            totalPrice = realTimeTransactionLog.getExecutedPrice() * quantity;
        }else{
            Property property = propertyRepository.findById(propertyId).orElseThrow(() ->
                    new PropertyException(PropertyErrorResult.PROPERTY_NOT_FOUND));
            totalPrice = property.getFundraise().getIssuePrice() * quantity;
        }

        OrderDto.GetTotalPriceResponse response = OrderDto.GetTotalPriceResponse.builder()
                .propertyId(propertyId.toString())
                .totalOrderPrice(totalPrice)
                .createdAt(LocalDateTime.now())
                .build();

        return response;
    }

    @Transactional(readOnly = true)
    @Override
    public OrderDto.GetQuantityResponse getQuantity(UUID userId, UUID propertyId) {
        //realtime transaction log가 있으면 해당 가격을 가져와서 계산, 없으면 property의 fundraise의 securityprice 가격을 가져와서 계산
        //property의 fundraise의 securityprice 가격을 가져와서 계산

        final User user = userRepository.findById(userId).orElseThrow(() ->
                new UserException(UserErrorResult.USER_NOT_FOUND));

        final Account account = accountRepository.findByUser(user).orElseThrow(() ->
                new TradingException(TradingErrorResult.ACCOUNT_NOT_FOUND));

        final Long deposit = account.getDeposit();
        Long quantity;

        if(realTimeTransactionLogRepository.existsByPropertyId(propertyId)) {
            RealTimeTransactionLog realTimeTransactionLog = realTimeTransactionLogRepository.findFirstByPropertyIdOrderByExecutedAtDesc(propertyId).orElseThrow(() ->
                    new TradingException(TradingErrorResult.TRADING_NOT_FOUND));

            quantity = (deposit / Long.valueOf(realTimeTransactionLog.getExecutedPrice()));

            log.info("quantity : {}, deposit : {}, executedPrice : {} ", quantity, deposit, realTimeTransactionLog.getExecutedPrice());
        }else{
            Property property = propertyRepository.findById(propertyId).orElseThrow(() ->
                    new PropertyException(PropertyErrorResult.PROPERTY_NOT_FOUND));
            quantity = (deposit / Long.valueOf(property.getFundraise().getIssuePrice()));
        }


        OrderDto.GetQuantityResponse response = OrderDto.GetQuantityResponse.builder()
                .propertyId(propertyId.toString())
                .quantity(quantity)
                .createdAt(LocalDateTime.now())
                .build();

        return response;
    }

    @Transactional(readOnly = true)
    public boolean isEnoughFundraise(Property property){
        return property.getFundraise().getProgressRate() >= 100.0;
    }


    @Transactional(readOnly = true)
    @Override
    public Page<QuoteDto.ReadResponse> readUpperQuotes(UUID propertyId, int basePrice, int page, int size) {
        final Property property = propertyRepository.findById(propertyId).orElseThrow(() ->
                new PropertyException(PropertyErrorResult.PROPERTY_NOT_FOUND));

        if(!isEnoughFundraise(property)){
            throw new TradingException(TradingErrorResult.NOT_ENOUGH_FUNDRAISE);
        }

        if(basePrice == 0){
            if(realTimeTransactionLogRepository.existsByPropertyId(propertyId)){
                RealTimeTransactionLog realTimeTransactionLog = realTimeTransactionLogRepository.findFirstByPropertyIdOrderByExecutedAtDesc(propertyId).orElseThrow(() ->
                        new TradingException(TradingErrorResult.TRADING_NOT_FOUND));
                basePrice = realTimeTransactionLog.getExecutedPrice();
            }else{
                basePrice = property.getFundraise().getIssuePrice();
            }
        }

        Pageable pageable = PageRequest.of(page, size);

        return quoteRepository.findByPropertyIdAndPriceGreaterThanEqualOrderByPriceDesc(propertyId, basePrice, pageable);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<QuoteDto.ReadResponse> readDownQuotes(UUID propertyId, int basePrice, int page, int size) {
        final Property property = propertyRepository.findById(propertyId).orElseThrow(() ->
                new PropertyException(PropertyErrorResult.PROPERTY_NOT_FOUND));

        if(!isEnoughFundraise(property)){
            throw new TradingException(TradingErrorResult.NOT_ENOUGH_FUNDRAISE);
        }

        if(basePrice == 0){
            if(realTimeTransactionLogRepository.existsByPropertyId(propertyId)){
                RealTimeTransactionLog realTimeTransactionLog = realTimeTransactionLogRepository.findFirstByPropertyIdOrderByExecutedAtDesc(propertyId).orElseThrow(() ->
                        new TradingException(TradingErrorResult.TRADING_NOT_FOUND));
                basePrice = realTimeTransactionLog.getExecutedPrice();
            }else{
                basePrice = property.getFundraise().getIssuePrice();
            }
        }

        Pageable pageable = PageRequest.of(page, size);

        return quoteRepository.findByPropertyIdAndPriceLessOrderByPriceAsc(propertyId, basePrice, pageable);
    }

}
