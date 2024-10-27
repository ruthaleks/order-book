package com.order_book.service;

import com.order_book.repository.Order;
import com.order_book.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
