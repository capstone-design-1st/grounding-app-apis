package org.example.first.groundingappapis.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.first.groundingappapis.dto.OrderDto;
import org.example.first.groundingappapis.dto.TradingDto;
import org.example.first.groundingappapis.entity.*;
import org.example.first.groundingappapis.exception.*;
import org.example.first.groundingappapis.repository.*;
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
            quoteRepository.save(quote);
        }

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

    @Override
    public void uploadSellingOrderOnQuote(User user, UUID propertyId, TradingDto.SellRequest sellRequest) {
        final Account sellerAccount = accountRepository.findByUser(user).orElseThrow(() ->
                new TradingException(TradingErrorResult.ACCOUNT_NOT_FOUND));
        final Property property = propertyRepository.findById(propertyId).orElseThrow(() ->
                new PropertyException(PropertyErrorResult.PROPERTY_NOT_FOUND));

        DayTransactionLog dayTransactionLog = dayTransactionLogRepository.findRecentDayTransactionLogByProperty(property).orElseGet(() -> {
            DayTransactionLog newDayTransactionLog = DayTransactionLog.builder()
                    .date(LocalDate.now())
                    .openingPrice(sellRequest.getPrice())
                    .closingPrice(null)
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
            quoteRepository.save(quote);
        }

        saveOrder(user, sellRequest.getPrice(), executedQuantityOfOrder, sellRequest.getQuantity(), "매도", property);

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
        }

        saveOrder(seller, sellRequest.getPrice(), executedQuantityOfOrder, executedQuantityOfOrder, "매도", property);

        return executedQuantityOfOrder;
    }

    private void updateOrderStatus(User user, Property property, int price, int quantity, String type) {
        Order order = orderRepository.findByUserAndPropertyAndPriceAndQuantityAndType(user, property, price, quantity, "체결 대기중").orElseThrow(() ->
                new TradingException(TradingErrorResult.TRADING_NOT_FOUND));
        order.setStatus("체결 완료");
        orderRepository.save(order);
    }

    private void updateOrderQuantity(User user, Property property, int price, int quantity, String type) {
        Order order = orderRepository.findByUserAndPropertyAndPriceAndQuantityAndType(user, property, price, quantity, "체결 대기중").orElseThrow(() ->
                new TradingException(TradingErrorResult.TRADING_NOT_FOUND));
        order.setQuantity(order.getQuantity() - quantity);
        orderRepository.save(order);
    }

    private void saveTransactionLog(Property property, int executedQuantity, int executedPrice, User user, DayTransactionLog dayTransactionLog) {
        RealTimeTransactionLog realTimeTransactionLog = RealTimeTransactionLog.builder()
                .quantity(executedQuantity)
                .executedPrice(executedPrice)
                .executedAt(LocalDateTime.now())
                .fluctuationRate(getFluctuationRate(dayTransactionLog.getOpeningPrice(), executedPrice))
                .build();
        realTimeTransactionLogRepository.save(realTimeTransactionLog);

        realTimeTransactionLog.updateProperty(property);
        realTimeTransactionLog.updateUser(user);
    }

    private void sellerDeposit(Account sellerAccount, int executedQuantity, int executedPrice) {
        DepositLog sellerDepositLog = DepositLog.builder()
                .createdAt(LocalDateTime.now())
                .build();
        sellerDepositLog.setLogOfDeposit(executedQuantity * executedPrice);
        depositLogRepository.save(sellerDepositLog);
        sellerAccount.plusDeposit(Long.valueOf(executedQuantity * executedPrice));
        sellerDepositLog.updateAccount(sellerAccount);
    }

    private void buyerWithdraw(Account buyerAccount, int executedQuantity, int executedPrice) {
        DepositLog buyerDepositLog = DepositLog.builder()
                .createdAt(LocalDateTime.now())
                .build();
        buyerDepositLog.setLogOfWithdraw(executedQuantity * executedPrice);
        depositLogRepository.save(buyerDepositLog);
        buyerAccount.minusDeposit(Long.valueOf(executedQuantity * executedPrice));
        buyerDepositLog.updateAccount(buyerAccount);
    }

    @Override
    public OrderDto.GetTotalPriceResponse getTotalOrderPrice(UUID propertyId, int quantity) {
        int totalPrice = 0;
        //realtime transaction log가 있으면 해당 가격을 가져와서 계산, 없으면 property의 fundraise의 securityprice 가격을 가져와서 계산
        //property의 fundraise의 securityprice 가격을 가져와서 계산

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

    @Override
    public OrderDto.GetQuantityResponse getQuantity(UUID propertyId, int totalOrderPrice) {
        int quantity = 0;
        //realtime transaction log가 있으면 해당 가격을 가져와서 계산, 없으면 property의 fundraise의 securityprice 가격을 가져와서 계산
        //property의 fundraise의 securityprice 가격을 가져와서 계산

        if(realTimeTransactionLogRepository.existsByPropertyId(propertyId)) {
            RealTimeTransactionLog realTimeTransactionLog = realTimeTransactionLogRepository.findFirstByPropertyIdOrderByExecutedAtDesc(propertyId).orElseThrow(() ->
                    new TradingException(TradingErrorResult.TRADING_NOT_FOUND));
            quantity = totalOrderPrice / realTimeTransactionLog.getExecutedPrice();
        }else{
            Property property = propertyRepository.findById(propertyId).orElseThrow(() ->
                    new PropertyException(PropertyErrorResult.PROPERTY_NOT_FOUND));
            quantity = totalOrderPrice / property.getFundraise().getIssuePrice();
        }

        OrderDto.GetQuantityResponse response = OrderDto.GetQuantityResponse.builder()
                .propertyId(propertyId.toString())
                .quantity(quantity)
                .createdAt(LocalDateTime.now())
                .build();

        return response;
    }

}
