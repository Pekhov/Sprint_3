package ru.yandex.practicum.createcouriertests;

import io.qameta.allure.internal.shadowed.jackson.core.JsonProcessingException;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.Courier;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Pekhov A.V.
 * @created 07/12/2021 - 07:41
 */

public class CreateCourierTest extends BaseTest {

    private Courier courier;

    @Test
    public void createCourierTest() throws JsonProcessingException {
        courier = new Courier();
        courier.register();
        assertTrue(courier.isRegistered());
    }

    @Test
    public void createDuplicateCourierTest() throws JsonProcessingException {
        courier = new Courier();
        courier.register();
        assertTrue(courier.isRegistered());
        Courier courier2 = new Courier(courier.getLogin(), courier.getPassword(), courier.getFirstName());
        Response response = courier2.register();
        assertFalse(courier2.isRegistered());
        assertEquals("Этот логин уже используется. Попробуйте другой.", response.then().extract().body().jsonPath().getString("message"));
    }

    @Test
    public void createDuplicateLoginCourierTest() throws JsonProcessingException {
        courier = new Courier();
        courier.register();
        Courier courier2 = new Courier();
        courier2.setLogin(courier.getLogin());
        Response response = courier2.register();
        assertEquals(409, response.statusCode());
        assertEquals("Этот логин уже используется. Попробуйте другой.", response.then().extract().body().jsonPath().getString("message"));
    }

    @Test
    public void registerStatusCodShouldBe201() throws JsonProcessingException {
        courier = new Courier();
        Response response = courier.register();
        assertEquals(201, response.statusCode());
    }

    @Test
    public void registerAnswerShouldBeTrue() throws JsonProcessingException {
        courier = new Courier();
        Response response = courier.register();
        assertTrue(response.then().extract().body().jsonPath().getBoolean("ok"));
    }

    @Test
    public void registerWithoutLogin() throws JsonProcessingException {
        courier = new Courier();
        courier.setLogin("");
        Response response = courier.register();
        assertEquals(400, response.statusCode());
        assertEquals("Недостаточно данных для создания учетной записи", response.then().extract().body().jsonPath().getString("message"));
    }

    @Test
    public void registerWithoutPassword() throws JsonProcessingException {
        courier = new Courier();
        courier.setPassword("");
        Response response = courier.register();
        assertEquals(400, response.statusCode());
        assertEquals("Недостаточно данных для создания учетной записи", response.then().extract().body().jsonPath().getString("message"));

    }

    @Test
    public void registerWithoutFirstName() throws JsonProcessingException {
        courier = new Courier();
        courier.setFirstName("");
        Response response = courier.register();
        assertEquals(201, response.statusCode());
    }

    @Test
    public void courierOrdersTest() throws JsonProcessingException {
        courier = new Courier();
        courier.register();
        Response response = courier.orders();

    }

    @AfterEach
    public void tearDown() throws JsonProcessingException {
        courier.unregistered();
    }
}
