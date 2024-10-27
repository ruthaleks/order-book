package com.order_book.controller;

import com.order_book.model.Summary;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Schema(description = "Summary given a ticker and a date")
@Data
@NoArgsConstructor
public class SummaryResponse {
    private Double avg;
    private int min;
    private int max;
    private int numberOrders;

    public SummaryResponse(Summary summary) {
        this.avg = summary.getAvg();
        this.min = summary.getMin();
        this.max = summary.getMax();
        this.numberOrders = summary.getNumberOrders();
    }
}

