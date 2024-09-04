package com.devdemo.app.ws.service;

import com.devdemo.app.ws.shared.dto.AddressDto;
import lombok.NonNull;

import java.util.Collection;

public interface AddressService {
    Collection<AddressDto> getAddresses(final @NonNull String userId);

    AddressDto getUserAddress(final @NonNull String addressId);
}
