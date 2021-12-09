package ru.yandex.practicum.createcouriertests;

import io.qameta.allure.internal.shadowed.jackson.core.JsonProcessingException;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.yandex.practicum.Order;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.notNullValue;

/**
 * @author Pekhov A.V.
 * @created 09/12/2021 - 07:39
 */

public class OrderTest extends BaseTest {

    private final Steps steps = new Steps();
    private Order order;

    @BeforeEach
    public void setUp() {
        order = new Order();
    }

    static Stream<Arguments> colorProvider() {

        return Stream.of(
                Arguments.arguments(Arrays.asList("BLACK", "GREY")),
                Arguments.arguments(Arrays.asList("BLACK")),
                Arguments.arguments(Collections.emptyList())
        );
    }

    @ParameterizedTest
    @MethodSource("colorProvider")
    public void createOrderWithSomeColors(List<String> colors) throws JsonProcessingException {
        String request =  steps.createRequest(colors);
        Response response = steps.sendOrderRequest(request);
        response.then().assertThat()
                .body("track", notNullValue())
                .and()
                .statusCode(201);
        order.setTrack(response.then().extract().body().jsonPath().getInt("track"));
    }
}
