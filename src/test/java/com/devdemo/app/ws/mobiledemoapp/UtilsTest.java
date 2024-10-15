package com.devdemo.app.ws.mobiledemoapp;

import com.devdemo.app.ws.shared.dto.UserDto;
import com.devdemo.app.ws.shared.util.Constant;
import com.devdemo.app.ws.shared.util.Util;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class UtilsTest {
    @Autowired
    Util util;
    @BeforeEach
    public void setUp() {

    }

    @MethodSource
    private static Stream<Arguments> getValidUserSource() {
        UserDto actualUserData = new UserDto();

        return Stream.of(Arguments.of(actualUserData));
    }
    @Test
    final void testGenerateUserId() {
        final String userId1 = Util.generateUserId();
        final String userId2 = Util.generateUserId();

        assertNotNull(userId1);
        assertNotNull(userId2);
        assertEquals(userId1.length(), Constant.USER_ID_LENGTH);
        assertNotEquals(userId1, userId2);
    }
    @Test
    final void testHasTokenNotExpired() {
        final String newToken = Util.generateTokenForUserId(Util.generateUserId());
        System.out.println(newToken);
        assertFalse(Util.hasTokenExpired(newToken));
    }

    @Test
    final void testHasTokenExpired() {
        final String oldToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ3QTBUNG1qbERpUTBmWUx5SGtlUkRvaFpHdXNzczciLCJleHAiOjE3Mjk4NTkwODN9.9ssXz7ZjrNuCJtW8y-UCe9wKrzxQHM6OeZn_VSD0KSKUUBtu4FI-H7AGgG7oSdEZnTFNwu6y9J-NrVnkf3SDWg";
        assertTrue(Util.hasTokenExpired(oldToken));
    }
}
