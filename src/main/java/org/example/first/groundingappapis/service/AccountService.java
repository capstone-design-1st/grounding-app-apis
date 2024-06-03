package org.example.first.groundingappapis.service;

import org.example.first.groundingappapis.dto.AccountDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface AccountService {
    List<AccountDto.ReadUserPropertyResponse> readUserProperty(UUID userID);

    Page<AccountDto.ReadCompletedOrderResponse> readTransactions(UUID userId, Pageable pageable, String startDate, String endDate, String type);

    AccountDto.ReadPresentStatusResponse readPresentStatus(UUID userId);
}
