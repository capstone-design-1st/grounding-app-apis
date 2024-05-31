package org.example.first.groundingappapis.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.first.groundingappapis.dto.AccountDto;
import org.example.first.groundingappapis.security.UserPrincipal;
import org.example.first.groundingappapis.service.AccountService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Slf4j
@RequestMapping("/account")
@RestController
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ResponseEntity<List<AccountDto.ReadUserPropertyResponse>> readUserProperty(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        log.info("readUserProperty");
        final UUID userId = userPrincipal.getUser().getId();
        return ResponseEntity.ok(accountService.readUserProperty(userId));
    }
}
