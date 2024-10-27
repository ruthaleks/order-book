package com.order_book.repository;

import com.order_book.controller.CreateOrderRequest;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "orders")
@NoArgsConstructor(force = true)
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    final private String ticker;
    final private String type;
    final private int volume;
    final private int priceValue;
    final private String priceCurrency;

    public Order(CreateOrderRequest request) {
        this.ticker = request.getTicker().toString();
        this.type = request.getType().toString();
        this.volume = request.getVolume();
        this.priceValue = request.getPrice().getValue();
        this.priceCurrency = request.getPrice().getCurrency().toString();
    }

}
