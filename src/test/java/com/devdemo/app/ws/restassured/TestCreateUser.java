package com.devdemo.app.ws.restassured;

import com.devdemo.app.ws.ui.model.request.AddressRequestModel;
import com.devdemo.app.ws.ui.model.request.UserDetailsRequestModel;
import com.devdemo.app.ws.ui.model.response.UserDetailsResponseModel;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TestCreateUser {

    private final String CONTEXT_PATH = "/mobile-app-ws";
    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8888;
    }

    @MethodSource
    private static Stream<Arguments> getValidUserSource() {
        UserDetailsRequestModel actualUserData = new UserDetailsRequestModel();

        List<AddressRequestModel> addressDtos = new ArrayList<>();
        AddressRequestModel shippingAddressDto = new AddressRequestModel();
        shippingAddressDto.setType("shipping");
        shippingAddressDto.setCity("Vlg");
        shippingAddressDto.setCountry("Ru");
        shippingAddressDto.setStreetName("Street 123");
        shippingAddressDto.setPostalCode("PostCode 123");
        addressDtos.add(shippingAddressDto);

        AddressRequestModel billingAddressDto = new AddressRequestModel();
        billingAddressDto.setType("billing");
        billingAddressDto.setCity("Vlg");
        billingAddressDto.setCountry("Ru");
        billingAddressDto.setStreetName("Street 123");
        billingAddressDto.setPostalCode("PostCode 123");
        addressDtos.add(billingAddressDto);

        actualUserData.setFirstName("Vlad");
        actualUserData.setLastName("Vlad");
        actualUserData.setPassword("qwerty123");
        actualUserData.setEmail("test11@test.com");

        actualUserData.setAddresses(addressDtos);

        return Stream.of(Arguments.of(actualUserData));
    }

    @ParameterizedTest
    @MethodSource("getValidUserSource")
    final void test(final UserDetailsRequestModel requestModel) {

        UserDetailsResponseModel responseModel = given()
                .log().all()
                .contentType("application/json")
                .accept("application/json")
                .body(requestModel)
                .when()
                .post(CONTEXT_PATH + "/users")
                .then()
                .statusCode(200)
                .contentType("application/json")
                .extract()
                .response()
                .as(UserDetailsResponseModel.class);

        assertNotNull(responseModel.getUserId());
        assertEquals(responseModel.getFirstName(), requestModel.getFirstName());
        assertEquals(responseModel.getLastName(), requestModel.getLastName());
        assertEquals(responseModel.getEmail(), requestModel.getEmail());
        assertEquals(responseModel.getAddresses().size(), requestModel.getAddresses().size());

    }
}
