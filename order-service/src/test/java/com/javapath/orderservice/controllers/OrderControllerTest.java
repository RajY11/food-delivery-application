package com.javapath.orderservice.controllers;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javapath.orderservice.models.Order;
import com.javapath.orderservice.service.OrderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@WebMvcTest(value=OrderController.class)
public class OrderControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private OrderService orderService;
    @Test
    public void createOrderTest() throws Exception {
        Order order = new Order();
        order.setOrderId(1111);
        order.setCustomerId(1122);
        order.setStatus("created");
        order.setTotalAmount(1000);
        order.setItemName("Item name");

        String jsonInput = this.mapToJson(order);
        String URI = "/api/v1/create";

        Mockito.when(orderService.createOrder(Mockito.any(Order.class))).thenReturn(order);
        Mockito.when(orderService.getOrderById(Mockito.anyInt())).thenReturn(Optional.of(order));
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                                            .post(URI)
                                            .accept(MediaType.APPLICATION_JSON).content(jsonInput)
                                            .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String outputJson = response.getContentAsString();
        assertThat(outputJson).isEqualTo(jsonInput);
        assertEquals(HttpStatus.OK.value(),response.getStatus());
    }

    @Test
    public void getOrderById() throws Exception {
        Order order = new Order();
        order.setOrderId(1111);
        order.setCustomerId(1122);
        order.setStatus("created");
        order.setTotalAmount(1000);
        order.setItemName("Item name");

        String jsonInput = this.mapToJson(order);
        String URI = "/api/v1/order/1111";

        Mockito.when(orderService.getOrderById(Mockito.anyInt())).thenReturn(Optional.of(order));
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String outputJson = response.getContentAsString();
        assertThat(outputJson).isEqualTo(jsonInput);
        assertEquals(HttpStatus.OK.value(),response.getStatus());
    }


    private String mapToJson(Object object) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return  objectMapper.writeValueAsString(object);
    }
}
