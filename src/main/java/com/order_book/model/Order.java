package com.order_book.model;

import com.order_book.common.Currency;
import com.order_book.common.Ticker;
import com.order_book.common.Type;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
    @Enumerated(EnumType.STRING)
    private Currency priceCurrency;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public Order(Ticker ticker, Type type, int volume, int priceValue, Currency priceCurrency) {
        this.ticker = ticker;
        this.type = type;
        this.volume = volume;
        this.priceValue = priceValue;
        this.priceCurrency = priceCurrency;
        this.createdAt = LocalDateTime.now();
    }

}
