package org.example.first.groundingappapis.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class TradingDto {
    @Data
    @NoArgsConstructor
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class BuyRequest {
        private int quantity;
        private int price;

        @Builder
        public BuyRequest(String propertyId, int quantity, int price) {
            this.quantity = quantity;
            this.price = price;
        }
    }

    @Data
    @NoArgsConstructor
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class SellRequest {
        private int quantity;
        private int price;

        @Builder
        public SellRequest(int quantity, int price) {
            this.quantity = quantity;
            this.price = price;
        }
    }

    @Data
    @NoArgsConstructor
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class BuyResponse {
        private String buyerId;
        private String walletAddress;

        private String propertyId;
        private Integer executedQuantity;
        private Integer orderedPrice;

        private List<PurchasedSellerQuoteInfoDto> purchasedSellQuotesInfoList;

        @Builder
        public BuyResponse(String buyerId, String walletAddress, String propertyId, Integer executedQuantity, Integer orderedPrice, List<PurchasedSellerQuoteInfoDto> purchasedSellQuotesInfoList) {
            this.buyerId = buyerId;
            this.walletAddress = walletAddress;
            this.propertyId = propertyId;
            this.executedQuantity = executedQuantity;
            this.orderedPrice = orderedPrice;
            this.purchasedSellQuotesInfoList = purchasedSellQuotesInfoList;
        }
    }

    @Data
    @NoArgsConstructor
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class PurchasedSellerQuoteInfoDto{
        private String sellerId;
        private String sellerWalletAddress;
        private Integer executedQuantity;
        @Builder
        public PurchasedSellerQuoteInfoDto(String sellerId, String sellerWalletAddress, Integer executedQuantity) {
            this.sellerId = sellerId;
            this.sellerWalletAddress = sellerWalletAddress;
            this.executedQuantity = executedQuantity;
        }
    }

    @Data
    @NoArgsConstructor
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class SellResponse {
        private String SellerId;
        private String walletAddress;

        private String propertyId;
        private Integer executedQuantity;
        private Integer orderedPrice;

        private List<SoldBuyerQuoteInfoDto> soldBuyerQuotesInfoList;


        @Builder
        public SellResponse(String sellerId, String walletAddress, String propertyId, Integer executedQuantity, Integer orderedPrice, List<SoldBuyerQuoteInfoDto> soldBuyerQuotesInfoList) {
            SellerId = sellerId;
            this.walletAddress = walletAddress;
            this.propertyId = propertyId;
            this.executedQuantity = executedQuantity;
            this.orderedPrice = orderedPrice;
            this.soldBuyerQuotesInfoList = soldBuyerQuotesInfoList;
        }
    }


    @Data
    @NoArgsConstructor
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class SoldBuyerQuoteInfoDto{
        private String buyerId;
        private String buyerWalletAddress;
        private Integer executedQuantity;
        @Builder
        public SoldBuyerQuoteInfoDto(String buyerId, String buyerWalletAddress, Integer executedQuantity) {
            this.buyerId = buyerId;
            this.buyerWalletAddress = buyerWalletAddress;
            this.executedQuantity = executedQuantity;
        }
    }
}
