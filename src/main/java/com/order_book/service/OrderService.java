package com.order_book.service;

import com.order_book.common.Ticker;
import com.order_book.model.Order;
import com.order_book.model.Summary;
import com.order_book.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public Long saveOrder(Order order) {
        orderRepository.save(order);
        return order.getId();
    }

    public Order getOrder(Long id) {
        return orderRepository.getById(id);
    }

    public Summary getSummary(Ticker ticker, LocalDate date) {
        List<Order> orders = orderRepository.findByTickerAndOrderDate(ticker, date);

        if (orders.isEmpty()) {
            throw new EntityNotFoundException("Could not find any orders for given ticker and date");
        }

        int min = orders.stream().mapToInt(Order::getPriceValue).min().orElseThrow(
                () -> new IllegalStateException("Failed to calculate min price for ticker: " + ticker));
        int max = orders.stream().mapToInt(Order::getPriceValue).max().orElseThrow(
                () -> new IllegalStateException("Failed to calculate max price for ticker: " + ticker));
        Double average = orders.stream().mapToInt(Order::getPriceValue).average().orElseThrow(
                () -> new IllegalStateException("Failed to calculate average price for ticker: " + ticker));
        return new Summary(average, min, max, orders.size());
    }
}
