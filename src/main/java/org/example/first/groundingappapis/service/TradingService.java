package org.example.first.groundingappapis.service;

import org.example.first.groundingappapis.dto.OrderDto;
import org.example.first.groundingappapis.dto.PropertyDto;
import org.example.first.groundingappapis.dto.QuoteDto;
import org.example.first.groundingappapis.dto.TradingDto;
import org.example.first.groundingappapis.entity.User;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface TradingService {

    void uploadSellingOrderOnQuote(User user, UUID propertyId, TradingDto.SellRequest sellRequest);

    void uploadBuyingOrderOnQuote(User user, UUID propertyId, TradingDto.BuyRequest buyRequest);

    OrderDto.GetTotalPriceResponse getTotalOrderPrice(UUID propertyId, int quantity);

    OrderDto.GetQuantityResponse getQuantity(UUID propertyId, int amount);

    Double getFluctuationRate(int openingPrice, int executedPrice);

    OrderDto.GetQuantityOfInventoryResponse getQuantityOfInventory(User user, UUID propertyId, int division);

    Page<QuoteDto.ReadResponse> readUpperQuotes(UUID propertyId, int basePrice, int page, int size);

    Page<QuoteDto.ReadResponse> readDownQuotes(UUID propertyId, int basePrice, int page, int size);
}
