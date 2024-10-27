package com.order_book.controller;

import com.order_book.model.Order;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Schema(description = "Order details")
@Data
@NoArgsConstructor
public class OrderResponse {
    private Ticker ticker;
    private Type type;
    @Schema(description = "Number of stocks", example = "100")
    private int volume;
    private Amount price;

    public OrderResponse(Order order) {
        this.ticker = Ticker.valueOf(order.getTicker());
        this.type = Type.valueOf(order.getType());
        this.volume = order.getVolume();
        this.price = new Amount(order.getPriceValue(), Amount.Currency.valueOf(order.getPriceCurrency()));
    }
}
