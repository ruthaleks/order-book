package com.order_book.controller;

import com.order_book.common.Ticker;
import com.order_book.common.Type;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Schema(requiredProperties = {"ticker", "type", "volume", "price"})
@NoArgsConstructor
public class CreateOrderRequest {
    private Ticker ticker;
    private Type type;
    @Schema(description = "Number of stocks", example = "100")
    @Positive(message = "The order volume must be positive and non-zero")
    private int volume;
    private Amount price;

}
