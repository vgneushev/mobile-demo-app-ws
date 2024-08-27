package com.devdemo.app.ws.ui.model.request;

import lombok.Data;

import java.util.Collection;

@Data
public class UserDetailsRequestModel {
        private String firstName;
        private String lastName;
        private String email;
        private String password;
        private Collection<AddressRequestModel> address;
}
