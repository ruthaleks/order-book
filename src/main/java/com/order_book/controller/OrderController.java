package com.order_book.controller;

import com.order_book.repository.Order;
import com.order_book.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

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
    public ResponseEntity<Void> createOrder(@RequestBody CreateOrderRequest request) {
        Long id = orderService.saveOrder(new Order(request));
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();
        return ResponseEntity.created(uri).build();
    }

    @Operation(
            summary = "Get a order by ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Order successfully retrieved.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Order not found.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))

            }
    )
    @GetMapping("/order/{id}")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable Long id) {
        Order order = orderService.getOrder(id);
        return ResponseEntity.ok(new OrderResponse(order));
    }
}
