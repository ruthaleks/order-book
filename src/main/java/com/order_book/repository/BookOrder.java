package com.order_book.repository;

import com.order_book.controller.CreateOrderRequest;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Entity
public class BookOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    final private String ticker;
    final private String type;
    final private int volume;
    final private int priceValue;
    final private String priceCurrency;

    public BookOrder(CreateOrderRequest request) {
        this.ticker = request.getTicker().toString();
        this.type = request.getType().toString();
        this.volume = request.getVolume();
        this.priceValue = request.getPrice().getValue();
        this.priceCurrency = request.getPrice().getCurrency().toString();
    }

}
