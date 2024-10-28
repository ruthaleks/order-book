package com.order_book.repository;

import com.order_book.common.Ticker;
import com.order_book.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByTickerAndOrderDate(Ticker ticker, LocalDate orderDate);
    List<Order> findByTicker(Ticker ticker);

}
