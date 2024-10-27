package com.order_book.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Summary {
    final private Double avg;
    final private int min;
    final private int max;
    final private int numberOrders;
}
