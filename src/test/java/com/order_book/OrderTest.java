package com.order_book;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.order_book.common.Ticker;
import com.order_book.common.Type;
import com.order_book.controller.*;
import com.order_book.model.Order;
import com.order_book.repository.OrderRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc

public class OrderTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    @AfterEach
    public void setUp() {
        orderRepository.deleteAll();
        orderRepository.flush();
    }


    @Test
    public void createOrder() throws Exception {
        String body = Files.readString(Path.of("src/test/resources/create_order_test.json"));

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .post("/order")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isCreated())
                .andReturn();

        String locationHeader = result.getResponse().getHeader("Location");
        assertThat(locationHeader).isNotNull();

        Long orderId = Long.parseLong(locationHeader.substring(locationHeader.lastIndexOf('/') + 1));

        Order savedOrder = orderRepository.findById(orderId).orElse(null);
        assertThat(savedOrder).isNotNull();

        assertThat(savedOrder.getTicker()).isEqualTo(Ticker.SAVE);
        assertThat(savedOrder.getType()).isEqualTo(Type.BUY);
        assertThat(savedOrder.getVolume()).isEqualTo(100);
        assertThat(savedOrder.getPriceValue()).isEqualTo(101);
        assertThat(savedOrder.getPriceCurrency()).isEqualTo("SEK");
    }

    @Test
    public void createZeroPriceOrder() throws Exception {
        String body = Files.readString(Path.of("src/test/resources/create_invalid_price_order_test.json"));

        mvc.perform(MockMvcRequestBuilders
                .post("/order")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createInvalidTickerOrder() throws Exception {
        String body = Files.readString(Path.of("src/test/resources/create_invalid_ticker_order_test.json"));

        mvc.perform(MockMvcRequestBuilders
                        .post("/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getOrder() throws Exception {
        String body = Files.readString(Path.of("src/test/resources/create_order_test.json"));

        MvcResult postResult = mvc.perform(MockMvcRequestBuilders.post("/order").contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isCreated()).andReturn();

        String location = postResult.getResponse().getHeader("Location");
        assertThat(location).isNotNull();

        MvcResult getResult = mvc.perform(MockMvcRequestBuilders.get(location).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();

        String responseBody = getResult.getResponse().getContentAsString();

        OrderResponse order = objectMapper.readValue(responseBody, OrderResponse.class);

        assertThat(order.getTicker()).isEqualTo(Ticker.SAVE);
        assertThat(order.getType()).isEqualTo(Type.BUY);
        assertThat(order.getVolume()).isEqualTo(100);
        assertThat(order.getPrice().getValue()).isEqualTo(101);
        assertThat(order.getPrice().getCurrency()).isEqualTo(Amount.Currency.SEK);
    }

    @Test
    public void getNonExistingOrder() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                        .get("/order/999999999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        mvc.perform(MockMvcRequestBuilders
                        .get("/order/intentional404")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getSummary() throws Exception {
        String firstOrder = Files.readString(Path.of("src/test/resources/create_order_test.json"));
        String secondOrder = Files.readString(Path.of("src/test/resources/create_another_order_test.json"));
        String date = LocalDate.now().toString();

        mvc.perform(MockMvcRequestBuilders
                        .post("/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(firstOrder))
                .andExpect(status().isCreated());
        mvc.perform(MockMvcRequestBuilders
                        .post("/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(secondOrder))
                .andExpect(status().isCreated());

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                        .get("/summary/SAVE?date=" + date)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        SummaryResponse summary = objectMapper.readValue(responseBody, SummaryResponse.class);

        assertThat(summary.getMin()).isEqualTo(10);
        assertThat(summary.getMax()).isEqualTo(101);
        assertThat(summary.getAvg()).isEqualTo(70.66666666666667);
        assertThat(summary.getNumberOrders()).isEqualTo(2);
    }

    public void getSummaryNoDate() throws Exception {
        String firstOrder = Files.readString(Path.of("src/test/resources/create_order_test.json"));
        String secondOrder = Files.readString(Path.of("src/test/resources/create_another_order_test.json"));
        String date = LocalDate.now().toString();

        mvc.perform(MockMvcRequestBuilders
                        .post("/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(firstOrder))
                .andExpect(status().isCreated());
        mvc.perform(MockMvcRequestBuilders
                        .post("/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(secondOrder))
                .andExpect(status().isCreated());

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                        .get("/summary/SAVE")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        SummaryResponse summary = objectMapper.readValue(responseBody, SummaryResponse.class);

        assertThat(summary.getMin()).isEqualTo(10);
        assertThat(summary.getMax()).isEqualTo(101);
        assertThat(summary.getAvg()).isEqualTo(55.5);
        assertThat(summary.getNumberOrders()).isEqualTo(2);
    }

    @Test
    public void getSummaryForCorrectTickerAndDate() throws Exception {
        String firstOrder = Files.readString(Path.of("src/test/resources/create_order_test.json"));
        String secondOrder = Files.readString(Path.of("src/test/resources/create_another_tsla_order_test.json"));

        String date = LocalDate.now().toString();

        mvc.perform(MockMvcRequestBuilders
                        .post("/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(firstOrder))
                .andExpect(status().isCreated());

        mvc.perform(MockMvcRequestBuilders
                        .post("/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(secondOrder))
                .andExpect(status().isCreated());

        mvc.perform(MockMvcRequestBuilders
                        .get("/summary/SAVE?date=1994-04-08")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        mvc.perform(MockMvcRequestBuilders
                        .get("/summary/GME?date=" + date)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();


        MvcResult result = mvc.perform(MockMvcRequestBuilders
                        .get("/summary/TSLA?date=" + date)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        SummaryResponse summary = objectMapper.readValue(responseBody, SummaryResponse.class);

        assertThat(summary.getMin()).isEqualTo(10);
        assertThat(summary.getMax()).isEqualTo(10);
        assertThat(summary.getAvg()).isEqualTo(10);
        assertThat(summary.getNumberOrders()).isEqualTo(1);
    }

}

