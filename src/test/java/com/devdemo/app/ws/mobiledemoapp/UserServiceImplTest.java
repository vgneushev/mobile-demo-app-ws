package com.devdemo.app.ws.mobiledemoapp;

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
import static org.mockito.Mockito.when;

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
    final void testCreateUser(final UserEntity entity) {
        when(userRepository.findByEmail(anyString())).thenReturn(null);
        when(bCryptPasswordEncoder.encode(anyString())).thenReturn(entity.getEncryptedPassword());
        when(userRepository.save(any(UserEntity.class))).thenReturn(entity);
        when(util.generateTokenForUserId(anyString())).thenReturn(entity.getEmailVerificationToken());

        List<AddressDto> addressDtos = new ArrayList<>();
        AddressDto addressDto = new AddressDto();
        addressDto.setType("shipping");
        addressDtos.add(addressDto);

        UserDto userDto = new UserDto();
        userDto.setAddresses(addressDtos);

        UserDto storedUserDetails = userService.createUser(userDto);
        assertNotNull(storedUserDetails);
    }
}
