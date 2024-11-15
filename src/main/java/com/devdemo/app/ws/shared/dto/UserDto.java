package com.devdemo.app.ws.shared.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Collection;

@Data
@Accessors(chain = true)
public class UserDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private long Id;
    private String userId;
    private String firstName;
    private String lastName;
    private String password;
    private String email;
    private String encryptedPassword;
    private String emailVerificationToken;
    private Boolean emailVerified = false;
    private Collection<AddressDto> addresses;
    @Override
    public String toString() {
        return "UserDto{" +
                "Id=" + Id +
                ", userId='" + userId + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", encryptedPassword='" + encryptedPassword + '\'' +
                ", emailVerificationToken='" + emailVerificationToken + '\'' +
                ", emailVerified=" + emailVerified +
                ", addresses=" + addresses +
                '}';
    }
}
