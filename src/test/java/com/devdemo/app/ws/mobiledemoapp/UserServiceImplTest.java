package com.devdemo.app.ws.mobiledemoapp;

import com.devdemo.app.ws.exception.UserServiceException;
import com.devdemo.app.ws.io.entity.AddressEntity;
import com.devdemo.app.ws.io.entity.UserEntity;
import com.devdemo.app.ws.repository.PasswordResetTokenRepository;
import com.devdemo.app.ws.repository.UserRepository;
import com.devdemo.app.ws.service.impl.UserServiceImpl;
import com.devdemo.app.ws.shared.dto.AddressDto;
import com.devdemo.app.ws.shared.dto.UserDto;
import com.devdemo.app.ws.shared.util.Util;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {

    @InjectMocks
    UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;
    @Mock
    Util util;
    @Mock
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    PasswordResetTokenRepository passwordResetTokenRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @MethodSource
    private static Stream<Arguments> getValidUserSource() {
        UserEntity entity = new UserEntity();
        entity.setId(1L);
        entity.setFirstName("Vlad");
        entity.setUserId("1234");
        entity.setEncryptedPassword("qwerty123");
        entity.setEmailVerificationToken("token");
        entity.setEmailVerified(Boolean.TRUE);
        entity.setEmail("test@test.com");

        List<AddressEntity> address = new ArrayList<>();
        AddressEntity shippingAddressEntity = new AddressEntity();
        shippingAddressEntity.setType("shipping");
        shippingAddressEntity.setUserDetails(entity);
        shippingAddressEntity.setAddressId("123");
        shippingAddressEntity.setCity("Vlg");
        shippingAddressEntity.setCountry("Ru");
        shippingAddressEntity.setStreetName("Street 123");
        shippingAddressEntity.setPostalCode("PostCode 123");
        address.add(shippingAddressEntity);

        AddressEntity billingAddressEntity = new AddressEntity();
        billingAddressEntity.setType("billing");
        billingAddressEntity.setUserDetails(entity);
        billingAddressEntity.setAddressId("123");
        billingAddressEntity.setCity("Vlg");
        billingAddressEntity.setCountry("Ru");
        billingAddressEntity.setStreetName("Street 123");
        billingAddressEntity.setPostalCode("PostCode 123");
        address.add(billingAddressEntity);

        entity.setAddresses(address);

        return Stream.of(Arguments.of(entity));
    }

    @ParameterizedTest
    @MethodSource("getValidUserSource")
    final void testGetUser(final UserEntity entity) {
        when(userRepository.findByEmail(anyString())).thenReturn(entity);
        final UserDto userDto = userService.getUser(entity.getEmail());
        assertNotNull(userDto);
    }

    @Test
    final void testGetUserNotFound() {
        when(userRepository.findByEmail(anyString())).thenReturn(null);
        assertThrows(UsernameNotFoundException.class, ()-> userService.getUser("test@test.com"));
    }

    @ParameterizedTest
    @MethodSource("getValidUserSource")
    final void testUserAlreadyExist(final UserEntity expectedUserData) {
        when(userRepository.findByEmail(anyString())).thenReturn(expectedUserData);
        assertThrows(UserServiceException.class,
                ()-> userService.createUser(new UserDto().setEmail(expectedUserData.getEmail())));
    }

    @ParameterizedTest
    @MethodSource("getValidUserSource")
    final void testCreateUser(final UserEntity expectedUserData) {
        when(userRepository.findByEmail(anyString())).thenReturn(null);
        when(bCryptPasswordEncoder.encode(anyString())).thenReturn(expectedUserData.getEncryptedPassword());
        when(userRepository.save(any(UserEntity.class))).thenReturn(expectedUserData);
        when(util.generateTokenForUserId(anyString())).thenReturn(expectedUserData.getEmailVerificationToken());

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

        UserDto actualUserData = new UserDto();
        actualUserData.setAddresses(addressDtos);

        UserDto storedUserDetails = userService.createUser(actualUserData);
        assertNotNull(storedUserDetails);
        assertEquals(expectedUserData.getFirstName(), storedUserDetails.getFirstName());
        assertEquals(expectedUserData.getLastName(), storedUserDetails.getLastName());
        assertNotNull(storedUserDetails.getUserId());
        assertEquals(expectedUserData.getAddresses().size(), storedUserDetails.getAddresses().size());
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }
}
