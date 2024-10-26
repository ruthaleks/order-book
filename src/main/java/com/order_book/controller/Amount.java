package com.order_book.controller;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.Getter;

@Schema(description = "Order amount", requiredProperties = {"value", "currency"})
@Data
@Getter
public class Amount {
    @Schema(description = "Order amount in minor units, must be > 0, e.g. $124.50 is represented as 12450", example = "12450")
    @Positive(message = "The order amount must be positive and non-zero")
    private int value;
    private Currency currency;

    public enum Currency {
        SEK,
        USD,
        EUR,
        GBP,
        NOK,
        DKK
    }
}

