package ru.yandex.practicum.createcouriertests;

import io.restassured.RestAssured;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.apache.http.params.CoreConnectionPNames;

import static io.restassured.RestAssured.*;

/**
 * @author Pekhov A.V.
 * @created 09/12/2021 - 08:30
 */

public class BaseTest {

    public BaseTest() {
        useRelaxedHTTPSValidation();
        baseURI = "https://qa-scooter.praktikum-services.ru";
        enableLoggingOfRequestAndResponseIfValidationFails();
        filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }
}
