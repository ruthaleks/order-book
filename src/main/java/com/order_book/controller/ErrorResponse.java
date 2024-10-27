package com.order_book.controller;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@Schema
@NoArgsConstructor
public class ErrorResponse {
    @Schema(example = "404")
    private int status;
    @Schema(example = "Order not found")
    private String message;
}

