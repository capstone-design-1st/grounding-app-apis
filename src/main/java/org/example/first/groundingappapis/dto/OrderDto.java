package org.example.first.groundingappapis.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OrderDto {
    private UUID id;
    private String type;
    private Integer price;
    private Integer quantity;

    @Builder
    public OrderDto(UUID id, String type, Integer price, Integer quantity) {
        this.id = id;
        this.type = type;
        this.price = price;
        this.quantity = quantity;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class GetResponse{}
    @Data
    @Builder
    @NoArgsConstructor
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class ReadResponse{}
}
