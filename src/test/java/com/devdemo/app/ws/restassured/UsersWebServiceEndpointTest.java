package com.devdemo.app.ws.restassured;

import com.devdemo.app.ws.io.entity.UserEntity;
import com.devdemo.app.ws.repository.UserRepository;
import com.devdemo.app.ws.ui.model.request.AddressRequestModel;
import com.devdemo.app.ws.ui.model.request.UserDetailsRequestModel;
import com.devdemo.app.ws.ui.model.response.UserDetailsResponseModel;
import io.qala.datagen.RandomShortApi;
import io.restassured.response.Response;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class UsersWebServiceEndpointTest extends BaseRestAssuredTest {

    private final ModelMapper mapper = new ModelMapper();
    @Autowired
    UserRepository repository;
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

        given()
                .log().all()
                .contentType("application/json")
                .accept("application/json")
                .body(loginDetails)
                .when()
                .post(CONTEXT_PATH + "/users/login")
                .then()
                .statusCode(403);
    }

    @ParameterizedTest
    @MethodSource("getValidUserSource")
    final void testLoginUser(final UserDetailsRequestModel requestModel) {
        final UserDetailsResponseModel userDetailsResponseModel = given()
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

        repository.updateUserEmailVerificationStatus(true, userDetailsResponseModel.getUserId());
        UserEntity user = repository.findByUserId(userDetailsResponseModel.getUserId());
        assertEquals(user.getEmailVerified(), true);

        Map<String, String> loginDetails = new HashMap<>();
        loginDetails.put("email", requestModel.getEmail());
        loginDetails.put("password", requestModel.getPassword());

        Response response = given()
                .log().all()
                .contentType("application/json")
                .accept("application/json")
                .body(loginDetails)
                .when()
                .post(CONTEXT_PATH + "/users/login")
                .then()
                .statusCode(200)
                .extract()
                .response();

        String authHeader = response.header("Authorization");
        String userId = response.header("UserID");
        assertEquals(userId, userDetailsResponseModel.getUserId());
        assertNotNull(authHeader);

        final UserDetailsResponseModel getUserResponse = given()
                .log().all()
                .contentType("application/json")
                .pathParam("id", userId)
                .accept("application/json")
                .header("Authorization", authHeader)
                .body(loginDetails)
                .when()
                .get(CONTEXT_PATH + "/users/{id}")
                .then()
                .statusCode(200)
                .extract()
                .response()
                .as(UserDetailsResponseModel.class);

        assertEquals(userId, getUserResponse.getUserId());
        assertEquals(requestModel.getAddresses().size(), getUserResponse.getAddresses().size());

    }
}
