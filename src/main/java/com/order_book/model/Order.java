package com.order_book.model;

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
    // todo: required args and positive amounts
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String ticker;

    @Column(nullable = false)
    private String type;

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
        this.ticker = request.getTicker().toString();
        this.type = request.getType().toString();
        this.volume = request.getVolume();
        this.priceValue = request.getPrice().getValue();
        this.priceCurrency = request.getPrice().getCurrency().toString();
        this.orderDate = LocalDate.now();
    }

}
