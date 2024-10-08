package com.devdemo.app.ws.ui.model.response;

import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

@Data
public class AddressesResponseModel extends RepresentationModel<AddressesResponseModel> {
    private String addressId;
    private String city;
    private String country;
    private String streetName;
    private String postalCode;
    private String type;
}
