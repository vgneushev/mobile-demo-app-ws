package com.devdemo.app.ws.restassured;

import com.devdemo.app.ws.ui.model.request.AddressRequestModel;
import com.devdemo.app.ws.ui.model.request.UserDetailsRequestModel;
import com.devdemo.app.ws.ui.model.response.UserDetailsResponseModel;
import io.qala.datagen.RandomShortApi;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TestCreateUser extends BaseRestAssuredTest {

    private final String CONTEXT_PATH = "/mobile-app-ws";

    @MethodSource
    private static Stream<Arguments> getValidUserSource() {
        UserDetailsRequestModel actualUserData = new UserDetailsRequestModel();

        List<AddressRequestModel> addressDtos = new ArrayList<>();
        AddressRequestModel shippingAddressDto = new AddressRequestModel();
        shippingAddressDto.setType("shipping");
        shippingAddressDto.setCity(RandomShortApi.english(randomStringLength));
        shippingAddressDto.setCountry(RandomShortApi.english(randomStringLength));
        shippingAddressDto.setStreetName(RandomShortApi.english(randomStringLength));
        shippingAddressDto.setPostalCode(RandomShortApi.alphanumeric(randomStringLength));
        addressDtos.add(shippingAddressDto);

        AddressRequestModel billingAddressDto = new AddressRequestModel();
        billingAddressDto.setType("billing");
        billingAddressDto.setCity(RandomShortApi.english(randomStringLength));
        billingAddressDto.setCountry(RandomShortApi.english(randomStringLength));
        billingAddressDto.setStreetName(RandomShortApi.alphanumeric(randomStringLength));
        billingAddressDto.setPostalCode(RandomShortApi.alphanumeric(randomStringLength));
        addressDtos.add(billingAddressDto);

        actualUserData.setFirstName(RandomShortApi.english(randomStringLength));
        actualUserData.setLastName(RandomShortApi.english(randomStringLength));
        actualUserData.setPassword(RandomShortApi.alphanumeric(randomStringLength));
        actualUserData.setEmail(RandomShortApi.alphanumeric(randomStringLength) + "test1@test.com");

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
