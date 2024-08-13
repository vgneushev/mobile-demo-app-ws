package com.devdemo.app.ws.mobiledemoappws.ui.model.response;

import lombok.Data;

@Data
public class UserDetailsResponseModel {
    private String userId;
    private String firstName;
    private String lastName;
    private String email;
}
