package org.example.first.groundingappapis.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.first.groundingappapis.dto.FundraiseDto;
import org.example.first.groundingappapis.entity.User;
import org.example.first.groundingappapis.security.UserPrincipal;
import org.example.first.groundingappapis.service.FundraiseService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RequestMapping("/fundraise")
@RestController
@RequiredArgsConstructor
public class FundraiseController {

    private final FundraiseService fundraiseService;
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{propertyId}")
    public ResponseEntity<FundraiseDto.FundraiseResponse> fundraiseProperty(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable String propertyId,
            @RequestBody FundraiseDto.FundraiseRequest fundraiseRequest) {

        log.info("fundraise called with propertyId: {}", propertyId);

        final UUID userId = userPrincipal.getUser().getId();

        return ResponseEntity.ok(fundraiseService.fundraiseProperty(propertyId, fundraiseRequest, userId));
    }
}

