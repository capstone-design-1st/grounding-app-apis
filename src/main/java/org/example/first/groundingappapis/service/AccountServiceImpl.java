package org.example.first.groundingappapis.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.first.groundingappapis.dto.AccountDto;
import org.example.first.groundingappapis.entity.*;
import org.example.first.groundingappapis.exception.TradingErrorResult;
import org.example.first.groundingappapis.exception.TradingException;
import org.example.first.groundingappapis.exception.UserErrorResult;
import org.example.first.groundingappapis.exception.UserException;
import org.example.first.groundingappapis.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final RealTimeTransactionLogRepository realTimeTransactionLogRepository;
    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;
    private final InventoryRepository inventoryRepository;
    private final OrderRepository orderRepository;

    @Transactional(readOnly = true)
    @Override
    public List<AccountDto.ReadUserPropertyResponse> readUserProperty(UUID userID) {
        final User user = userRepository.findById(userID)
                .orElseThrow(() -> new UserException(UserErrorResult.USER_NOT_FOUND));

        final Account account = accountRepository.findByUser(user)
                .orElseThrow(() -> new TradingException(TradingErrorResult.ACCOUNT_NOT_FOUND));

        final List<Inventory> inventories = inventoryRepository.findByAccount(account);

        List<AccountDto.ReadUserPropertyResponse> readUserPropertyResponses = new ArrayList<>();


        for (Inventory inventory : inventories) {
            final Property property = inventory.getProperty();

            int recentExecutedPrice = 0;
            if(realTimeTransactionLogRepository.existsByPropertyId(property.getId())) {
                Optional<RealTimeTransactionLog> realTimeTransactionLog = realTimeTransactionLogRepository.findFirstByPropertyIdOrderByExecutedAtDesc(property.getId());
                if(realTimeTransactionLog.isPresent())
                    recentExecutedPrice = realTimeTransactionLog.get().getExecutedPrice();
                else
                    recentExecutedPrice = property.getFundraise().getIssuePrice();
            }


            final AccountDto.ReadUserPropertyResponse readUserPropertyResponse = AccountDto.ReadUserPropertyResponse.builder()
                    .propertyId(property.getId().toString())
                    .propertyName(property.getName())
                    .type(property.getType())
                    .quantity(inventory.getQuantity())
                    .averageBuyingPrice(inventory.getAverageBuyingPrice())
                    .presentPrice(recentExecutedPrice)
                    .evaluationPrice(recentExecutedPrice * inventory.getQuantity())
                    //.evaluationPrice(recentExecutedPrice)
                    .differenceAmount(recentExecutedPrice - inventory.getAverageBuyingPrice())
                    .fluctuationRate((double) (recentExecutedPrice - inventory.getAverageBuyingPrice()) / inventory.getAverageBuyingPrice())
                    .totalBuyingPrice(inventory.getQuantity() * inventory.getAverageBuyingPrice())
                    .build();

            readUserPropertyResponses.add(readUserPropertyResponse);
        }

        return readUserPropertyResponses;
    }

    @Transactional(readOnly = true)
    @Override
    public Page<AccountDto.ReadCompletedOrderResponse> readTransactions(UUID userId, Pageable pageable, String startDate, String endDate, String type) {

        final User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorResult.USER_NOT_FOUND));

        if(type != null && !type.equals("매수") && !type.equals("매도") && !type.equals(""))
            throw new TradingException(TradingErrorResult.INVALID_TYPE);

        LocalDateTime parsedStartDate = LocalDateTime.parse(startDate + "T00:00:00");
        LocalDateTime parsedEndDate = LocalDateTime.parse(endDate + "T23:59:59");

        if (type.equals(""))
            return orderRepository.findByUserAndCreatedAtBetweenAndCompleted(user, parsedStartDate, parsedEndDate, pageable);
        else
            return orderRepository.findByUserAndCreatedAtBetweenAndTypeAndCompleted(user, parsedStartDate, parsedEndDate, pageable, type);


    }

    @Override
    public AccountDto.ReadPresentStatusResponse readPresentStatus(UUID userId) {
        final User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorResult.USER_NOT_FOUND));
        final Account account = accountRepository.findByUser(user)
                .orElseThrow(() -> new TradingException(TradingErrorResult.ACCOUNT_NOT_FOUND));

        List<Inventory> inventories = inventoryRepository.findByAccount(account);
        Long totalBuyingPrice;
        Integer evaluationPrice;
        Integer evaluationEarning;

        if(inventories.isEmpty()){
            totalBuyingPrice = 0L;
            evaluationPrice = 0;
            evaluationEarning = 0;
        }else{
            totalBuyingPrice = inventories.stream().mapToLong(inventory ->
                    inventory.getQuantity() * inventory.getAverageBuyingPrice()).sum();
            evaluationPrice = inventories.stream().mapToInt(inventory -> {

                int recentExecutedPrice = 0;
                if(realTimeTransactionLogRepository.existsByPropertyId(inventory.getProperty().getId())) {
                    Optional<RealTimeTransactionLog> realTimeTransactionLog = realTimeTransactionLogRepository.findFirstByPropertyIdOrderByExecutedAtDesc(inventory.getProperty().getId());
                    if(realTimeTransactionLog.isPresent())
                        recentExecutedPrice = realTimeTransactionLog.get().getExecutedPrice();
                    else
                        recentExecutedPrice = inventory.getProperty().getFundraise().getIssuePrice();
                }

                return recentExecutedPrice * inventory.getQuantity();
            }).sum();
            evaluationEarning = inventories.stream().mapToInt(inventory -> {

                int recentExecutedPrice = 0;
                if(realTimeTransactionLogRepository.existsByPropertyId(inventory.getProperty().getId())) {
                    Optional<RealTimeTransactionLog> realTimeTransactionLog = realTimeTransactionLogRepository.findFirstByPropertyIdOrderByExecutedAtDesc(inventory.getProperty().getId());
                    if(realTimeTransactionLog.isPresent())
                        recentExecutedPrice = realTimeTransactionLog.get().getExecutedPrice();
                    else
                        recentExecutedPrice = inventory.getProperty().getFundraise().getIssuePrice();
                }

                return (recentExecutedPrice - inventory.getAverageBuyingPrice()) * inventory.getQuantity();
            }).sum();
        }


        Long fundrasingPrice = orderRepository.sumPriceByStatus("청약중") != null ? orderRepository.sumPriceByStatus("청약중") : 0L;

        Long deposit = account.getDeposit() != null ? account.getDeposit() : 0L;

        Double averageEarningRate = account.getAverageEarningRate() != null ? account.getAverageEarningRate() : 0.0;

        AccountDto.ReadPresentStatusResponse readPresentStatusResponse = AccountDto.ReadPresentStatusResponse.builder()
                .userId(userId)
                .totalBuyingPrice(totalBuyingPrice)
                .fundraisingPrice(fundrasingPrice)
                .deposit(deposit)
                .averageEarningRate(averageEarningRate)
                .evaluationPrice(evaluationPrice)
                .evaluationEarning(evaluationEarning)
                .build();

        return readPresentStatusResponse;
    }
}
