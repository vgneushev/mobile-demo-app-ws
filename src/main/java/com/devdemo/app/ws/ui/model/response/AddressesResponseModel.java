package com.devdemo.app.ws.ui.model.response;

import lombok.Data;

@Data
public class AddressesResponseModel {
    private String addressId;
    private String city;
    private String country;
    private String streetName;
    private String postalCode;
    private String type;
}
