package com.order_book.controller;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
@Schema(requiredProperties = {"ticker", "type", "volume", "price"})
public class CreateOrderRequest {
    private Ticker ticker;
    private Type type;
    @Schema(description = "Number of stocks", example = "100")
    private int volume;
    private Amount price;

    public enum Ticker {
        SAVE,
        GME,
        TSLA
    }

    public enum Type {
        BUY,
        SELL
    }
}
