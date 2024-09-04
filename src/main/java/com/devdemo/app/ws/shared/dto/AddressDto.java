package com.devdemo.app.ws.shared.dto;

import lombok.Data;

@Data
public class AddressDto {
    private static final long serialVersionUID = 2L;
    private long id;
    private String addressId;
    private String city;
    private String country;
    private String streetName;
    private String postalCode;
    private String type;
    private UserDto userDetails;
}
