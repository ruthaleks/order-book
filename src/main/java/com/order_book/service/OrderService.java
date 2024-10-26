package com.order_book.service;

import com.order_book.repository.BookOrder;
import com.order_book.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public Long saveOrder(BookOrder order) {
        orderRepository.save(order);
        return order.getId();
    }
}
