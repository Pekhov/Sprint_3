package ru.yandex.practicum.createcouriertests;

import io.qameta.allure.internal.shadowed.jackson.core.JsonProcessingException;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.Courier;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Pekhov A.V.
 * @created 08/12/2021 - 12:26
 */

public class LoginCourierTest extends BaseTest {

    private Courier courier;

    @Test
    public void successLoginTest() throws JsonProcessingException {
        courier = new Courier();
        courier.register();
        Response response = courier.login();
        response.then().assertThat()
                .body("id", notNullValue())
                .and()
                .statusCode(200);
    }

    @Test
    public void loginCourierWithoutLoginTest() throws JsonProcessingException {
        courier = new Courier();
        courier.register();
        String realLogin = courier.getLogin();
        courier.setLogin("");
        Response response = courier.login();
        courier.setLogin(realLogin);
        assertEquals(400, response.statusCode());
        assertEquals("Недостаточно данных для входа", response.then().extract().body().jsonPath().getString("message"));
    }

    @Test
    public void loginCourierWithoutPasswordTest() throws JsonProcessingException {
        courier = new Courier();
        courier.register();
        String realPassword = courier.getPassword();
        courier.setPassword("");
        Response response = courier.login();
        courier.setPassword(realPassword);
        assertEquals(400, response.statusCode());
        assertEquals("Недостаточно данных для входа", response.then().extract().body().jsonPath().getString("message"));
    }

    @Test
    public void loginCourierWithBadLoginTest() throws JsonProcessingException {
        courier = new Courier();
        courier.register();
        courier.setLogin("badLogin");
        Response response = courier.login();
        assertEquals(404, response.statusCode());
        assertEquals("Учетная запись не найдена", response.then().extract().body().jsonPath().getString("message"));
    }

    @Test
    public void loginCourierWithBadPasswordTest() throws JsonProcessingException {
        courier = new Courier();
        courier.register();
        courier.setPassword("badPassword");
        Response response = courier.login();
        assertEquals(404, response.statusCode());
        assertEquals("Учетная запись не найдена", response.then().extract().body().jsonPath().getString("message"));
    }

    @Test
    public void loginWithUnregisteredCourier() throws JsonProcessingException {
        courier = new Courier();
        Response response = courier.login();
        assertEquals(404, response.statusCode());
        assertEquals("Учетная запись не найдена", response.then().extract().body().jsonPath().getString("message"));
    }

    @AfterEach
    public void tearDown() throws JsonProcessingException {
        courier.unregistered();
    }
}
