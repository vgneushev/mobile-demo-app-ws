package com.devdemo.app.ws.restassured;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;

public class BaseRestAssuredTest {
    protected static final int randomStringLength = 10;

    protected final String CONTEXT_PATH = "/mobile-app-ws";
    @BeforeAll
    static void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8888;
    }
}
