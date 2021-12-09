package ru.yandex.practicum.createcouriertests;

import io.qameta.allure.Step;
import io.qameta.allure.internal.shadowed.jackson.core.JsonProcessingException;
import io.qameta.allure.internal.shadowed.jackson.databind.ObjectMapper;
import io.qameta.allure.internal.shadowed.jackson.databind.node.ArrayNode;
import io.qameta.allure.internal.shadowed.jackson.databind.node.ObjectNode;
import io.restassured.response.Response;
import ru.yandex.practicum.Order;

import java.util.List;
import java.util.logging.Logger;

import static io.restassured.RestAssured.given;

/**
 * @author Pekhov A.V.
 * @created 07/12/2021 - 07:42
 */

public class Steps {

    private Logger log = Logger.getLogger(this.getClass().getName());

    @Step
    public Response sendOrderRequest(String request) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(request)
                .when()
                .post("/api/v1/orders");
    }

    public String createRequest(List<String> colorsArray) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode json = mapper.createObjectNode();
        ArrayNode colors = mapper.valueToTree(colorsArray);
        json.put("firstName", "Test777");
        json.put("lastName", "Test777");
        json.put("address", "Moscow");
        json.put("metroStation", "Сокольники");
        json.put("phone", "79156352954");
        json.put("rentTime", 7);
        json.put("deliveryDate", "2021-06-06");
        json.put("comment", "Comment");
        json.putArray("color").addAll(colors);

        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
    }

    public Response getOrderByTrack(int track) {
        return given()
                .header("Content-type", "application/json")
                .queryParam("t", track)
                .when()
                .get("/api/v1/orders/track");

    }
}
