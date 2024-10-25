package com.order_book.controller;

import com.order_book.repository.BookOrder;
import com.order_book.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {
    @Autowired
    OrderService orderService;

    @Operation(
            summary = "Create a new order",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Order successfully created.", content = @Content)
            }
    )
    @PostMapping("/order")
    public ResponseEntity<HttpStatus> createOrder(@RequestBody CreateOrderRequest request) {
        orderService.saveOrder(new BookOrder(request));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
