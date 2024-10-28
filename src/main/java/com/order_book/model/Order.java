package com.order_book.model;

import com.order_book.common.Ticker;
import com.order_book.common.Type;
import com.order_book.controller.CreateOrderRequest;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Entity
@Table(name = "orders")
@NoArgsConstructor()
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Ticker ticker;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Type type;

    @Column(nullable = false)
    @Positive
    private int volume;

    @Column(nullable = false)
    @Positive
    private int priceValue;

    @Column(nullable = false)
    private String priceCurrency;

    @Column(nullable = false)
    private LocalDate orderDate;

    public Order(CreateOrderRequest request) {
        this.ticker = request.getTicker();
        this.type = request.getType();
        this.volume = request.getVolume();
        this.priceValue = request.getPrice().getValue();
        this.priceCurrency = request.getPrice().getCurrency().toString();
        this.orderDate = LocalDate.now();
    }

}
