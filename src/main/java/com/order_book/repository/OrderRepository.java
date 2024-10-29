package com.order_book.repository;

import com.order_book.common.Ticker;
import com.order_book.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o WHERE o.ticker= :ticker AND o.createdAt >= :startOfDay AND o.createdAt < :endOfDay")
    List<Order> findByTickerAndDate(@Param("ticker") Ticker ticker,
                                    @Param("startOfDay") LocalDateTime startOfDay,
                                    @Param("endOfDay") LocalDateTime endOfDay);
    List<Order> findByTicker(Ticker ticker);

}
