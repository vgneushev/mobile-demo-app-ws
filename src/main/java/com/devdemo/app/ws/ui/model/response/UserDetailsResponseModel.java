package com.devdemo.app.ws.ui.model.response;

import lombok.Data;

import java.util.Collection;

@Data
public class UserDetailsResponseModel {
    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private Collection<AddressesResponseModel> addresses;
}
