package com.order_book.controller;

import com.order_book.model.Order;
import com.order_book.model.Summary;
import com.order_book.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;

@RestController
public class OrderController {
    @Autowired
    OrderService orderService;

    @Operation(
            summary = "Create a new order",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Order successfully created.", content = @Content),
                    @ApiResponse(responseCode = "400", description = "Invalid request",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            }
    )
    @PostMapping("/order")
    public ResponseEntity<Void> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        Long id = orderService.saveOrder(new Order(request));
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();
        return ResponseEntity.created(uri).build();
    }

    @Operation(
            summary = "Get a order by id",
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

    @Operation(
            summary = "Summary given a ticker and a date",
            description = "Average/min/max price for a given ticker and a date",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Summary successfully calculated.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = SummaryResponse.class))),
                    @ApiResponse(responseCode = "404", description = "No entries for this ticker on the specified date",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))

            }
    )
    @GetMapping("/order/summary/{ticker}")
    public ResponseEntity<SummaryResponse> getOrder(@PathVariable Ticker ticker,
                                                    @Parameter(description = "Order date in format YYYY-MM-DD", example = "2024-10-27")
                                                    @RequestParam(value = "date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        Summary summary = orderService.getSummary(ticker.toString(), date);
        return ResponseEntity.ok(new SummaryResponse(summary));
    }

}
