package com.order_book.controller;

import com.order_book.common.Currency;
import io.swagger.v3.oas.annotations.media.Schema;


import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Schema(description = "Order amount", requiredProperties = {"value", "currency"})
@Getter
@AllArgsConstructor
public class Amount {
    @Schema(description = "Order amount in minor units, must be > 0, e.g. $124.50 is represented as 12450", example = "12450")
    @Positive(message = "The order amount must be positive and non-zero")
    final private int value;
    final private Currency currency;
}

