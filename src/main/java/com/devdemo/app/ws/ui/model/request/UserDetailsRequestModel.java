package com.devdemo.app.ws.ui.model.request;

import lombok.Data;

@Data
public class UserDetailsRequestModel {
        private String firstName;
        private String lastName;
        private String email;
        private String password;
}
