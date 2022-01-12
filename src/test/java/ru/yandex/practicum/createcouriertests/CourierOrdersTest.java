package ru.yandex.practicum.createcouriertests;

import io.qameta.allure.internal.shadowed.jackson.core.JsonProcessingException;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.Courier;
import ru.yandex.practicum.Order;
import static org.hamcrest.Matchers.*;

import java.util.Collections;

/**
 * @author Pekhov A.V.
 * @created 09/12/2021 - 09:49
 */

public class CourierOrdersTest extends BaseTest {

    private Courier courier;
    private Order order;
    private Steps steps;

    @BeforeEach
    public void setUp() {
        courier = new Courier();
        order = new Order();
        steps = new Steps();
    }

    @Test
    public void courierOrdersTest() throws JsonProcessingException {
        String request =  steps.createRequest(Collections.emptyList());
        Response createOrderResponse = steps.sendOrderRequest(request);
        order.setTrack(createOrderResponse.then().extract().body().jsonPath().getInt("track"));

        Response orderResponse = steps.getOrderByTrack(order.getTrack());
        order.setId(orderResponse.then().extract().body().jsonPath().getInt("order.id"));

        courier.register();
        courier.acceptOrder(order);
        courier.orders()
                .then().assertThat().body("orders", is(not(empty())));

    }

    @AfterEach
    public void tearDown() throws JsonProcessingException {
        courier.unregistered();
    }
}
