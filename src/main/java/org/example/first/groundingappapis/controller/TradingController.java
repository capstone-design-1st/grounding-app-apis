package org.example.first.groundingappapis.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.first.groundingappapis.dto.OrderDto;
import org.example.first.groundingappapis.dto.PropertyDto;
import org.example.first.groundingappapis.dto.ResponseDto;
import org.example.first.groundingappapis.dto.TradingDto;
import org.example.first.groundingappapis.entity.User;
import org.example.first.groundingappapis.security.UserPrincipal;
import org.example.first.groundingappapis.service.TradingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RequestMapping("/trading")
@RestController
@RequiredArgsConstructor
public class TradingController {

    private final TradingService tradingService;

    //Post, buying
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{propertyId}/buying")
    public ResponseEntity<ResponseDto> buyOrder(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                                @PathVariable UUID propertyId,
                                                                @RequestBody TradingDto.BuyRequest buyRequest) {

        User user = userPrincipal.getUser();
        tradingService.uploadBuyingOrderOnQuote(user, propertyId, buyRequest);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    //Post, Selling
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{propertyId}/selling")
    public ResponseEntity<ResponseDto> sellOrder(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                      @PathVariable UUID propertyId,
                                                      @RequestBody TradingDto.SellRequest sellRequest) {
        User user = userPrincipal.getUser();
        tradingService.uploadSellingOrderOnQuote(user, propertyId, sellRequest);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    //get total price by quantity and propertyId response
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{propertyId}/total-price")
    public ResponseEntity<OrderDto.GetTotalPriceResponse> getTotalPrice(@PathVariable UUID propertyId,
                                                                        @RequestParam int quantity) {

        return ResponseEntity.ok(tradingService.getTotalOrderPrice(propertyId, quantity));
    }
    //get quantity by amount of money and propertyId response
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{propertyId}/quantity")
    public ResponseEntity<OrderDto.GetQuantityResponse> getQuantity(@PathVariable UUID propertyId,
                                               @RequestParam int amount) {

        return ResponseEntity.ok(tradingService.getQuantity(propertyId, amount));
    }
}
