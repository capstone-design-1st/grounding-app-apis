package org.example.first.groundingappapis.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.first.groundingappapis.dto.OrderDto;
import org.example.first.groundingappapis.dto.QuoteDto;
import org.example.first.groundingappapis.dto.ResponseDto;
import org.example.first.groundingappapis.dto.TradingDto;
import org.example.first.groundingappapis.entity.User;
import org.example.first.groundingappapis.security.UserPrincipal;
import org.example.first.groundingappapis.service.RefactorTradingService;
import org.example.first.groundingappapis.service.TradingService;
import org.springframework.data.domain.Page;
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
    private final RefactorTradingService refactorTradingService;
    //Post, buying
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{propertyId}/buying")
    public ResponseEntity<TradingDto.BuyResponse> buyOrder(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                                @PathVariable UUID propertyId,
                                                                @RequestBody TradingDto.BuyRequest buyRequest) {
        UUID userId = userPrincipal.getUser().getId();
        //TradingDto.BuyResponse response = tradingService.uploadBuyingOrderOnQuote(user, propertyId, buyRequest);

        TradingDto.BuyResponse response = refactorTradingService.executeBuyTransaction(userId, propertyId, buyRequest);

        return ResponseEntity.ok(response);
    }
    //Post, Selling
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{propertyId}/selling")
    public ResponseEntity<TradingDto.SellResponse> sellOrder(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                      @PathVariable UUID propertyId,
                                                      @RequestBody TradingDto.SellRequest sellRequest) {
        UUID userId = userPrincipal.getUser().getId();
        TradingDto.SellResponse response = refactorTradingService.executeSellTransaction(userId, propertyId, sellRequest);

        return ResponseEntity.ok(response);
    }

    //get total price by quantity and propertyId response
//    @Operation(summary = "구매 가능한 총 금액 조회")
//    @PreAuthorize("isAuthenticated()")
//    @GetMapping("/{propertyId}/total-price-by-quantity")
//    public ResponseEntity<OrderDto.GetTotalPriceResponse> getTotalPrice(@PathVariable UUID propertyId,
//                                                                        @RequestParam int quantity) {
//
//        return ResponseEntity.ok(tradingService.getTotalPrice(propertyId, quantity));
//    }
    //get quantity by amount of money and propertyId response
    @Operation(summary = "구매 가능한 수량 조회")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{propertyId}/buyable-quantity")
    public ResponseEntity<OrderDto.GetQuantityResponse> getTotalBuyableQuantity(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable UUID propertyId) {

        UUID userId = userPrincipal.getUser().getId();

        return ResponseEntity.ok(tradingService.getQuantity(userId, propertyId));
    }

    //get quantity of inventory by propertyId
    @Operation(summary = "보유중인 매물 총 수량 조회")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{propertyId}/inventory/quantity")
    public ResponseEntity<OrderDto.GetQuantityOfInventoryResponse> getInventory(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                                      @PathVariable UUID propertyId) {
        User user = userPrincipal.getUser();
        return ResponseEntity.ok(tradingService.getQuantityOfInventory(user, propertyId));
    }


    @GetMapping("/{propertyId}/quotes/upper")
    public ResponseEntity<Page<QuoteDto.ReadResponse>> getUpperQuotes(
            @PathVariable UUID propertyId,
            //기준가
            @RequestParam(defaultValue="0") int basePrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size) {

        return ResponseEntity.ok(tradingService.readUpperQuotes(propertyId, basePrice, page, size));
    }

    @GetMapping("/{propertyId}/quotes/down")
    public ResponseEntity<Page<QuoteDto.ReadResponse>> getDownQuotes(
            @PathVariable UUID propertyId,
            //기준가
            @RequestParam(defaultValue="0") int basePrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size) {

        return ResponseEntity.ok(tradingService.readDownQuotes(propertyId, basePrice, page, size));
    }
}
