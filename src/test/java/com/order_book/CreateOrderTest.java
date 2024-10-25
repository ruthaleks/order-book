package com.order_book;

import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CreateOrderTest {
        @Autowired
        private MockMvc mvc;

        @AfterEach
        public void cleanUp() throws IOException {
                Files.deleteIfExists(Paths.get("order-book-test.sqlite"));

        }

        @Test
        public void createOrder() throws Exception {
                String body = Files.readString(Path.of("src/test/resources/create_order_test.json"));

                mvc.perform(MockMvcRequestBuilders.post("/order")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body))
                        .andExpect(status().isCreated());
        }
}

