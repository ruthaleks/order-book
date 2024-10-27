package com.order_book;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.order_book.controller.Amount;
import com.order_book.controller.OrderResponse;
import com.order_book.controller.Ticker;
import com.order_book.controller.Type;
import com.order_book.repository.Order;
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

        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andReturn();

        String locationHeader = result.getResponse().getHeader("Location");
        assertThat(locationHeader).isNotNull();

        Long orderId = Long.parseLong(locationHeader.substring(locationHeader.lastIndexOf('/') + 1));

        Order savedOrder = orderRepository.findById(orderId).orElse(null);
        assertThat(savedOrder).isNotNull();

        assertThat(savedOrder.getTicker()).isEqualTo("SAVE");
        assertThat(savedOrder.getType()).isEqualTo("BUY");
        assertThat(savedOrder.getVolume()).isEqualTo(100);
        assertThat(savedOrder.getPriceValue()).isEqualTo(101);
        assertThat(savedOrder.getPriceCurrency()).isEqualTo("SEK");
    }

    @Test
    public void getOrder() throws Exception {
        String body = Files.readString(Path.of("src/test/resources/create_order_test.json"));

        MvcResult postResult = mvc.perform(MockMvcRequestBuilders.post("/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andReturn();

        String location = postResult.getResponse().getHeader("Location");
        assertThat(location).isNotNull();

        MvcResult getResult = mvc.perform(MockMvcRequestBuilders.get(location)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

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
        mvc.perform(MockMvcRequestBuilders.get("/order/999999999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        mvc.perform(MockMvcRequestBuilders.get("/order/intentional404")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}

