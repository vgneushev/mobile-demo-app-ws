package com.devdemo.app.ws.mobiledemoapp;

import com.devdemo.app.ws.io.entity.AddressEntity;
import com.devdemo.app.ws.io.entity.UserEntity;
import com.devdemo.app.ws.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class UserRepositoryTest {

    @Autowired
    UserRepository repository;
    @BeforeAll
    public void setUp() {
        UserEntity entity = new UserEntity();
        entity.setId(1L);
        entity.setFirstName("Vlad");
        entity.setLastName("Vlad");
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

        UserEntity entity2 = new UserEntity();
        entity2.setId(1L);
        entity2.setFirstName("Vlad2");
        entity2.setLastName("Vlad2");
        entity2.setUserId("12342");
        entity2.setEncryptedPassword("qwerty123");
        entity2.setEmailVerificationToken("token");
        entity2.setEmailVerified(Boolean.TRUE);
        entity2.setEmail("test2@test.com");
        entity2.setAddresses(address);

        repository.save(entity);
        repository.save(entity2);
    }

    @Test
    final void testGetVerifiedUsers() {
        Pageable pageable = PageRequest.of(0, 1);
        Page<UserEntity> page = repository.findAllUsersWithConfirmedEmail(pageable);
        assertNotNull(page);

        List<UserEntity> userEntities = page.getContent();
        assertNotNull(userEntities);
        assertEquals(userEntities.size(), 1);
    }

    @Test
    final void testGetUserByName() {
        final String name = "Vlad2";
        List<UserEntity> users = repository.findUserByFirstName(name);
        assertNotNull(users);
        assertEquals(users.size(), 1);
        assertEquals(users.get(0).getFirstName(), name);
    }

    @Test
    final void testGetUserByLastName() {
        final String name = "Vlad2";
        List<UserEntity> users = repository.findUserByLastName(name);
        assertNotNull(users);
        assertEquals(users.size(), 1);
        assertEquals(users.get(0).getFirstName(), name);
    }

    @Test
    final void testGetUserByKeyword() {
        final String keyword = "2";
        List<UserEntity> users = repository.findUserByKeyword(keyword);
        assertNotNull(users);
        assertEquals(users.size(), 1);
        assertTrue(users.get(0).getFirstName().contains(keyword));
        assertTrue(users.get(0).getLastName().contains(keyword));
    }
    @Test
    final void testGetUserFirstLastNamesByKeyword() {
        final String name = "Vlad2";
        final String keyword = "2";
        List<Object[]> users = repository.getUserFirstLastNamesByKeyword(keyword);
        assertNotNull(users);
        assertEquals(users.size(), 1);
        Object[] user = users.get(0);
        String firstName = String.valueOf(user[0]);
        String lastName = String.valueOf(user[1]);
        assertEquals(firstName, name);
        assertEquals(lastName, name);
    }
}
