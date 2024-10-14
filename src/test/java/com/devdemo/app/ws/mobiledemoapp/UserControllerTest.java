package com.devdemo.app.ws.mobiledemoapp;

import com.devdemo.app.ws.service.impl.UserServiceImpl;
import com.devdemo.app.ws.shared.dto.AddressDto;
import com.devdemo.app.ws.shared.dto.UserDto;
import com.devdemo.app.ws.ui.controller.UserController;
import com.devdemo.app.ws.ui.model.response.UserDetailsResponseModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    @InjectMocks
    UserController userController;
    @Mock
    private UserServiceImpl userService;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @MethodSource
    private static Stream<Arguments> getValidUserSource() {
        UserDto actualUserData = new UserDto();

        List<AddressDto> addressDtos = new ArrayList<>();
        AddressDto shippingAddressDto = new AddressDto();
        shippingAddressDto.setType("shipping");
        shippingAddressDto.setAddressId("123");
        shippingAddressDto.setCity("Vlg");
        shippingAddressDto.setCountry("Ru");
        shippingAddressDto.setStreetName("Street 123");
        shippingAddressDto.setPostalCode("PostCode 123");
        addressDtos.add(shippingAddressDto);

        AddressDto billingAddressDto = new AddressDto();
        billingAddressDto.setType("billing");
        billingAddressDto.setAddressId("123");
        billingAddressDto.setCity("Vlg");
        billingAddressDto.setCountry("Ru");
        billingAddressDto.setStreetName("Street 123");
        billingAddressDto.setPostalCode("PostCode 123");
        addressDtos.add(billingAddressDto);

        actualUserData.setId(1L);
        actualUserData.setFirstName("Vlad");
        actualUserData.setUserId("1234");
        actualUserData.setEncryptedPassword("qwerty123");
        actualUserData.setEmailVerificationToken("token");
        actualUserData.setEmailVerified(Boolean.TRUE);
        actualUserData.setEmail("test@test.com");

        actualUserData.setAddresses(addressDtos);

        return Stream.of(Arguments.of(actualUserData));
    }

    @ParameterizedTest
    @MethodSource("getValidUserSource")
    final void testGetUser(final UserDto userDto) {
        when(userService.getUserById(anyString())).thenReturn(userDto);

        UserDetailsResponseModel responseModel = userController.getUser(userDto.getUserId());
        assertNotNull(responseModel);
        assertEquals(responseModel.getUserId(), userDto.getUserId());
        assertEquals(responseModel.getEmail(), userDto.getEmail());
        assertEquals(responseModel.getFirstName(), userDto.getFirstName());
        assertEquals(responseModel.getLastName(), userDto.getLastName());
        assertEquals(responseModel.getAddresses().size(), userDto.getAddresses().size());
    }
}
