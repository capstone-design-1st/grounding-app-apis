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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                    .evaluationPrice(recentExecutedPrice)
                    .differenceAmount(recentExecutedPrice - inventory.getAverageBuyingPrice())
                    .fluctuationRate((double) (recentExecutedPrice - inventory.getAverageBuyingPrice()) / inventory.getAverageBuyingPrice())
                    .totalBuyingPrice(inventory.getQuantity() * inventory.getAverageBuyingPrice())
                    .build();

            readUserPropertyResponses.add(readUserPropertyResponse);
        }

        return readUserPropertyResponses;
    }
}
