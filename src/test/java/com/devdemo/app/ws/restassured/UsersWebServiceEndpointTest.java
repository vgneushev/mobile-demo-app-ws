package com.devdemo.app.ws.restassured;

import com.devdemo.app.ws.ui.model.request.AddressRequestModel;
import com.devdemo.app.ws.ui.model.request.UserDetailsRequestModel;
import io.qala.datagen.RandomShortApi;
import io.restassured.response.Response;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;

public class UsersWebServiceEndpointTest extends BaseRestAssuredTest {
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
    final void testErrorLoginUser(final UserDetailsRequestModel requestModel) {
        given()
                .log().all()
                .contentType("application/json")
                .accept("application/json")
                .body(requestModel)
                .when()
                .post(CONTEXT_PATH + "/users")
                .then()
                .statusCode(200)
                .contentType("application/json");


        Map<String, String> loginDetails = new HashMap<>();
        loginDetails.put("email", requestModel.getEmail());
        loginDetails.put("password", requestModel.getPassword());


        Response responseModel = given()
                .log().all()
                .contentType("application/json")
                .accept("application/json")
                .body(loginDetails)
                .when()
                .post(CONTEXT_PATH + "/users/login")
                .then()
                .statusCode(403)
                .extract()
                .response();

       // String authHeader = responseModel.header("Authorization");
       // String userId = responseModel.header("UserID");
    }
}
