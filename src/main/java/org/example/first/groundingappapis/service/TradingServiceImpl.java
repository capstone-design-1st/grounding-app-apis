package org.example.first.groundingappapis.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.first.groundingappapis.dto.OrderDto;
import org.example.first.groundingappapis.dto.QuoteDto;
import org.example.first.groundingappapis.dto.RealTimeTransactionLogDto;
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
import java.util.Optional;
import java.util.UUID;
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
    public void uploadBuyingOrderOnQuote(User buyer, UUID propertyId, TradingDto.BuyRequest buyRequest) {
        final Account buyerAccount = accountRepository.findByUser(buyer).orElseThrow(() ->
                new TradingException(TradingErrorResult.ACCOUNT_NOT_FOUND));
        final Property property = propertyRepository.findById(propertyId).orElseThrow(() ->
                new PropertyException(PropertyErrorResult.PROPERTY_NOT_FOUND));
        if (buyRequest.getPrice() * buyRequest.getQuantity() > buyerAccount.getDeposit()) {
            throw new TradingException(TradingErrorResult.NOT_ENOUGH_DEPOSIT);
        }
        if(!isEnoughFundraise(property)){
            throw new TradingException(TradingErrorResult.NOT_ENOUGH_FUNDRAISE);
        }

        DayTransactionLog dayTransactionLog = dayTransactionLogRepository.findRecentDayTransactionLogByProperty(property).orElseGet(() -> {
            DayTransactionLog newDayTransactionLog = DayTransactionLog.builder()
                    .date(LocalDate.now())
                    .openingPrice(buyRequest.getPrice())
                    .closingPrice(null)
                    .maxPrice(buyRequest.getPrice())
                    .minPrice(buyRequest.getPrice())
                    .build();
            newDayTransactionLog.updateProperty(property);
            dayTransactionLogRepository.save(newDayTransactionLog);
            return newDayTransactionLog;
        });

        int executedQuantityOfOrder = 0;

        if (quoteRepository.existsByPropertyAndPriceLessThanEqual(property, buyRequest.getPrice())) {
            executedQuantityOfOrder = executeBuyTransaction(property, buyer, buyRequest, dayTransactionLog);
        } else {
            Quote quote = Quote.builder()
                    .price(buyRequest.getPrice())
                    .quantity(buyRequest.getQuantity())
                    .createdAt(LocalDateTime.now())
                    .build();
            quote.updateProperty(property);
            quote.updateAccount(buyerAccount);
            quoteRepository.save(quote);
        }

        final int finalExecutedQuantityOfOrder = executedQuantityOfOrder;

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
        inventory.setSellableQuantity(inventory.getSellableQuantity() + finalExecutedQuantityOfOrder);
        inventory.setQuantity(inventory.getQuantity() + finalExecutedQuantityOfOrder);
        inventory.setAverageBuyingPrice((inventory.getAverageBuyingPrice() + buyRequest.getPrice()) / (inventory.getQuantity() + finalExecutedQuantityOfOrder));

        inventoryRepository.save(inventory);

        saveBuyOrder(buyer, buyRequest, executedQuantityOfOrder, property);

        buyerAccount.minusDeposit(Long.valueOf(buyRequest.getPrice() * buyRequest.getQuantity()));
        accountRepository.save(buyerAccount);
    }

    @Transactional
    public void saveBuyOrder(User buyer, TradingDto.BuyRequest buyRequest, int executedQuantityOfOrder, Property property) {
        saveOrder(buyer, buyRequest.getPrice(), executedQuantityOfOrder, buyRequest.getQuantity(), "매수", property);
    }

    @Transactional
    public void saveOrder(User user, int price, int executedQuantity, int totalQuantity, String type, Property property) {
        if (executedQuantity > 0 && executedQuantity < totalQuantity) {
            Order orderCompleted = Order.builder()
                    .type(type)
                    .price(price)
                    .quantity(executedQuantity)
                    .status("체결 완료")
                    .build();

            orderCompleted.updateProperty(property);
            orderCompleted.updateUser(user);
            orderRepository.save(orderCompleted);

            Order orderPending = Order.builder()
                    .type(type)
                    .price(price)
                    .quantity(totalQuantity - executedQuantity)
                    .status("체결 대기중")
                    .build();

            orderPending.updateProperty(property);
            orderPending.updateUser(user);
            orderRepository.save(orderPending);
        } else if (executedQuantity == 0) {
            Order order = Order.builder()
                    .type(type)
                    .price(price)
                    .quantity(totalQuantity)
                    .status("체결 대기중")
                    .build();

            order.updateProperty(property);
            order.updateUser(user);
            orderRepository.save(order);
        } else if (executedQuantity == totalQuantity){
            Order order = Order.builder()
                    .type(type)
                    .price(price)
                    .quantity(totalQuantity)
                    .status("체결 완료")
                    .build();

            order.updateProperty(property);
            order.updateUser(user);
            orderRepository.save(order);
        }
    }

    @Transactional
    @Override
    public void uploadSellingOrderOnQuote(User user, UUID propertyId, TradingDto.SellRequest sellRequest) {
        final Account sellerAccount = accountRepository.findByUser(user).orElseThrow(() ->
                new TradingException(TradingErrorResult.ACCOUNT_NOT_FOUND));
        final Property property = propertyRepository.findById(propertyId).orElseThrow(() ->
                new PropertyException(PropertyErrorResult.PROPERTY_NOT_FOUND));
        if(!isEnoughFundraise(property)){
            throw new TradingException(TradingErrorResult.NOT_ENOUGH_FUNDRAISE);
        }
        LocalDate date = LocalDate.now();

        DayTransactionLog dayTransactionLog = dayTransactionLogRepository.findRecentDayTransactionLogByPropertyAndToday(property, date).orElseGet(() -> {
            RealTimeTransactionLog realTimeTransactionLog = realTimeTransactionLogRepository.findFirstByPropertyIdOrderByExecutedAtDesc(propertyId).orElseThrow(() ->
                    new TradingException(TradingErrorResult.TRADING_NOT_FOUND));

            DayTransactionLog newDayTransactionLog = DayTransactionLog.builder()
                    .date(LocalDate.now())
                    .openingPrice(sellRequest.getPrice())
                    .closingPrice(null) //TODO
                    .maxPrice(sellRequest.getPrice())
                    .minPrice(sellRequest.getPrice())
                    .build();
            newDayTransactionLog.updateProperty(property);
            dayTransactionLogRepository.save(newDayTransactionLog);
            return newDayTransactionLog;
        });

        int executedQuantityOfOrder = 0;

        if (quoteRepository.existsByPropertyAndPriceGreaterThanEqual(property, sellRequest.getPrice())) {
            executedQuantityOfOrder = executeSellTransaction(property, user, sellRequest, dayTransactionLog);
        } else {
            Quote quote = Quote.builder()
                    .price(sellRequest.getPrice())
                    .quantity(sellRequest.getQuantity())
                    .createdAt(LocalDateTime.now())
                    .build();
            quote.updateProperty(property);
            quote.updateAccount(sellerAccount);
            quoteRepository.save(quote);
        }

        saveOrder(user, sellRequest.getPrice(), executedQuantityOfOrder, sellRequest.getQuantity(), "매도", property);

        Inventory inventory = inventoryRepository.findByAccountAndProperty(sellerAccount, property).orElseThrow(() ->
                new TradingException(TradingErrorResult.INVENTORY_NOT_FOUND));

        inventory.setSellableQuantity(inventory.getSellableQuantity() - sellRequest.getQuantity());

        if(inventory.getQuantity() == executedQuantityOfOrder){
            inventoryRepository.delete(inventory);
        }else{
            inventory.setQuantity(inventory.getQuantity() - executedQuantityOfOrder);
            inventoryRepository.save(inventory);
        }

        sellerAccount.minusDeposit(Long.valueOf(sellRequest.getPrice() * sellRequest.getQuantity()));
        accountRepository.save(sellerAccount);

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

        Inventory inventory = inventoryRepository.findByAccountAndProperty(account, property).orElseThrow(() ->
                new TradingException(TradingErrorResult.INVENTORY_NOT_FOUND));

        OrderDto.GetQuantityOfInventoryResponse response = OrderDto.GetQuantityOfInventoryResponse.builder()
                .propertyId(propertyId.toString())
                .quantity(inventory.getQuantity())
                .createdAt(LocalDateTime.now())
                .build();

        return response;
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

    @Transactional
    public int executeBuyTransaction(Property property, User buyer, TradingDto.BuyRequest buyRequest, DayTransactionLog dayTransactionLog) {
        int executedQuantityOfOrder = 0;

        while (buyRequest.getQuantity() > 0) {
            Optional<Quote> sellQuoteOptional = quoteRepository.findFirstByPropertyAndPriceLessThanEqualOrderByPriceAsc(property, buyRequest.getPrice());

            if (!sellQuoteOptional.isPresent()) {
                break;
            }

            final Quote sellQuote = sellQuoteOptional.get();
            final User seller = sellQuote.getAccount().getUser();
            final Account sellerAccount = seller.getAccount();

            int executedQuantity = Math.min(buyRequest.getQuantity(), sellQuote.getQuantity());
            int executedPrice = sellQuote.getPrice();
            buyRequest.setQuantity(buyRequest.getQuantity() - executedQuantity);
            sellQuote.setQuantity(sellQuote.getQuantity() - executedQuantity);
            executedQuantityOfOrder += executedQuantity;


            if (sellQuote.getQuantity() == 0) {
                quoteRepository.delete(sellQuote);
                updateOrderStatus(seller, property, executedPrice, executedQuantity, "매도");
            } else {
                quoteRepository.save(sellQuote);
                updateOrderQuantity(seller, property, executedPrice, executedQuantity, "매도");
            }

            saveTransactionLog(property, executedQuantity, executedPrice, buyer, dayTransactionLog);

            sellerDeposit(sellerAccount, executedQuantity, executedPrice);

            //매도자측 inventory 업데이트
            Inventory inventory = inventoryRepository.findByAccountAndProperty(sellerAccount, property).orElseGet(() -> {
                Inventory newInventory = Inventory.builder()
                        .quantity(0)
                        .sellableQuantity(0)
                        .averageBuyingPrice(0)
                        .build();
                newInventory.updateAccount(sellerAccount);
                newInventory.updateProperty(property);
                return newInventory;
            });

            inventory.setQuantity(inventory.getQuantity() - executedQuantity);

            RealTimeTransactionLog realTimeTransactionLog = RealTimeTransactionLog.builder()
                    .quantity(executedQuantity)
                    .executedPrice(executedPrice)
                    .executedAt(LocalDateTime.now())
                    .fluctuationRate(getFluctuationRate(dayTransactionLog.getOpeningPrice(), executedPrice))
                    .build();

            realTimeTransactionLogRepository.save(realTimeTransactionLog);
        }

        saveOrder(buyer, buyRequest.getPrice(), executedQuantityOfOrder, executedQuantityOfOrder, "매수", property);

        return executedQuantityOfOrder;
    }

    @Transactional
    public int executeSellTransaction(Property property, User seller, TradingDto.SellRequest sellRequest, DayTransactionLog dayTransactionLog) {
        int executedQuantityOfOrder = 0;

        while (sellRequest.getQuantity() > 0) {
            Optional<Quote> buyQuoteOptional = quoteRepository.findFirstByPropertyAndPriceGreaterThanEqualOrderByPriceDesc(property, sellRequest.getPrice());

            if (!buyQuoteOptional.isPresent()) {
                break;
            }

            final Quote buyQuote = buyQuoteOptional.get();
            final User buyer = buyQuote.getAccount().getUser();
            final Account buyerAccount = buyer.getAccount();

            int executedQuantity = Math.min(sellRequest.getQuantity(), buyQuote.getQuantity());
            int executedPrice = buyQuote.getPrice();
            sellRequest.setQuantity(sellRequest.getQuantity() - executedQuantity);
            buyQuote.setQuantity(buyQuote.getQuantity() - executedQuantity);
            executedQuantityOfOrder += executedQuantity;

            if (buyQuote.getQuantity() == 0) {
                quoteRepository.delete(buyQuote);
                updateOrderStatus(buyer, property, executedPrice, executedQuantity, "매수");
            } else {
                quoteRepository.save(buyQuote);
                updateOrderQuantity(buyer, property, executedPrice, executedQuantity, "매수");
            }

            saveTransactionLog(property, executedQuantity, executedPrice, seller, dayTransactionLog);

            buyerWithdraw(buyerAccount, executedQuantity, executedPrice);

            //매수자측 inventory 업데이트
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

            inventory.setQuantity(inventory.getQuantity() + executedQuantity);
            inventoryRepository.save(inventory);
        }

        saveOrder(seller, sellRequest.getPrice(), executedQuantityOfOrder, executedQuantityOfOrder, "매도", property);

        return executedQuantityOfOrder;
    }

    @Transactional
    public void updateOrderStatus(User user, Property property, int price, int quantity, String type) {
        Order order = orderRepository.findByUserAndPropertyAndPriceAndQuantityAndType(user, property, price, quantity, "체결 대기중").orElseThrow(() ->
                new TradingException(TradingErrorResult.ORDER_NOT_FOUND));
        order.setStatus("체결 완료");
        orderRepository.save(order);
    }

    @Transactional
    public void updateOrderQuantity(User user, Property property, int price, int quantity, String type) {
        Order order = orderRepository.findByUserAndPropertyAndPriceAndQuantityAndType(user, property, price, quantity, "체결 대기중").orElseThrow(() ->
                new TradingException(TradingErrorResult.ORDER_NOT_FOUND));
        order.setQuantity(order.getQuantity() - quantity);
        orderRepository.save(order);
    }

    @Transactional
    public void saveTransactionLog(Property property, int executedQuantity, int executedPrice, User user, DayTransactionLog dayTransactionLog) {
        RealTimeTransactionLog realTimeTransactionLog = RealTimeTransactionLog.builder()
                .quantity(executedQuantity)
                .executedPrice(executedPrice)
                .executedAt(LocalDateTime.now())
                .fluctuationRate(getFluctuationRate(dayTransactionLog.getOpeningPrice(), executedPrice))
                .build();
        realTimeTransactionLogRepository.save(realTimeTransactionLog);

        realTimeTransactionLog.updateProperty(property);
        realTimeTransactionLog.updateUser(user);

        if(executedPrice > dayTransactionLog.getMaxPrice())
            dayTransactionLog.updateMaxPrice(executedPrice);
        else if(executedPrice < dayTransactionLog.getMinPrice())
            dayTransactionLog.updateMinPrice(executedPrice);

        dayTransactionLog.increaseVolume(executedQuantity);

        dayTransactionLogRepository.save(dayTransactionLog);
    }

    @Transactional
    public void sellerDeposit(Account sellerAccount, int executedQuantity, int executedPrice) {
        DepositLog sellerDepositLog = DepositLog.builder()
                .createdAt(LocalDateTime.now())
                .build();
        sellerDepositLog.setLogOfDeposit(executedQuantity * executedPrice);
        depositLogRepository.save(sellerDepositLog);
        sellerAccount.plusDeposit(Long.valueOf(executedQuantity * executedPrice));
        sellerDepositLog.updateAccount(sellerAccount);
    }

    @Transactional
    public void buyerWithdraw(Account buyerAccount, int executedQuantity, int executedPrice) {
        DepositLog buyerDepositLog = DepositLog.builder()
                .createdAt(LocalDateTime.now())
                .build();
        buyerDepositLog.setLogOfWithdraw(executedQuantity * executedPrice);
        depositLogRepository.save(buyerDepositLog);
        buyerAccount.minusDeposit(Long.valueOf(executedQuantity * executedPrice));
        buyerDepositLog.updateAccount(buyerAccount);
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
        int quantity;

        if(realTimeTransactionLogRepository.existsByPropertyId(propertyId)) {
            RealTimeTransactionLog realTimeTransactionLog = realTimeTransactionLogRepository.findFirstByPropertyIdOrderByExecutedAtDesc(propertyId).orElseThrow(() ->
                    new TradingException(TradingErrorResult.TRADING_NOT_FOUND));
            quantity = (int) (deposit / Long.valueOf(realTimeTransactionLog.getExecutedPrice()));
        }else{
            Property property = propertyRepository.findById(propertyId).orElseThrow(() ->
                    new PropertyException(PropertyErrorResult.PROPERTY_NOT_FOUND));
            quantity = (int) (deposit / Long.valueOf(property.getFundraise().getIssuePrice()));
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
}
