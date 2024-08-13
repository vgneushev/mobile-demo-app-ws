package com.devdemo.app.ws.mobiledemoappws.shared.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private long Id;
    private String userId;
    private String username;
    private String password;
    private String email;
    private String encryptedPassword;
    private String emailVerificationToken;
    private Boolean emailVerified = false;
}
