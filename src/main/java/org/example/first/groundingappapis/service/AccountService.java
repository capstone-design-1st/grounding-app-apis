package org.example.first.groundingappapis.service;

import org.example.first.groundingappapis.dto.AccountDto;

import java.util.List;
import java.util.UUID;

public interface AccountService {
    List<AccountDto.ReadUserPropertyResponse> readUserProperty(UUID userID);
}
