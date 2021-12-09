package ru.yandex.practicum;

import io.qameta.allure.internal.shadowed.jackson.core.JsonProcessingException;
import io.qameta.allure.internal.shadowed.jackson.databind.ObjectMapper;
import io.qameta.allure.internal.shadowed.jackson.databind.node.ObjectNode;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.logging.Logger;

import static io.restassured.RestAssured.*;

/**
 * @author Pekhov A.V.
 * @created 08/12/2021 - 07:16
 */


public class Courier {

    private String login;
    private String password;
    private String firstName;
    private int courierId;
    private boolean isLoggined = false;
    private boolean isRegistered= false;
    private Logger log = Logger.getLogger(this.getClass().getName());

    public Courier() {
        this(
                RandomStringUtils.randomAlphabetic(10),
                RandomStringUtils.randomAlphabetic(10),
                RandomStringUtils.randomAlphabetic(10)
        );
    }

    public Courier(String login, String password, String firstName) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
    }

    public boolean isLoggined() {
        return isLoggined;
    }

    public boolean isRegistered() { return isRegistered; }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    private int getCourierId() throws JsonProcessingException {
        if(!isLoggined) { login(); }
        return courierId;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public Response register() throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode json = mapper.createObjectNode();
        if(!login.isEmpty()) { json.put("login", login); }
        if(!password.isEmpty()) { json.put("password", password); }
        if(!firstName.isEmpty()) { json.put("firstName", firstName); }

        String registerRequestBody = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(registerRequestBody)
                .when()
                .post("/api/v1/courier");

        isRegistered = response.statusCode() == 201;
        return response;
    }

    public Response login() throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode json = mapper.createObjectNode();
        if(!login.isEmpty()) { json.put("login", login); }
        if(!password.isEmpty()) { json.put("password", password); }

        String loginRequestBody = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);

        Response response = given().header("Content-type", "application/json").and().body(loginRequestBody)
                .when().post("/api/v1/courier/login");
        if(response.statusCode() == 200) {
            isLoggined = true;
            courierId = response.then().extract().body().jsonPath().getInt("id");
        } else {
            isLoggined = false;
        }
        return response;
    }

    public Response orders() throws JsonProcessingException {
        return given()
                .header("Content-type", "application/json")
                .queryParam("courierId", getCourierId())
                .when().get("/api/v1/orders");
    }

    public Response acceptOrder(Order order) throws JsonProcessingException {

        return given()
                .header("Content-type", "application/json")
                .queryParam("courierId", getCourierId())
                .when().put("/api/v1/orders/accept/{id}", order.getId());
    }

    public boolean unregistered() throws JsonProcessingException {
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .when().delete("/api/v1/courier/{id}", getCourierId());
        if (response.statusCode() == 200) {
            return response.then().extract().body().jsonPath().getBoolean("ok");
        } else return false;
    }
}
