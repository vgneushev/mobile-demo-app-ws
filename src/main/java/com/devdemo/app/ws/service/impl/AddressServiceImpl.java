package com.devdemo.app.ws.service.impl;

import com.devdemo.app.ws.io.entity.AddressEntity;
import com.devdemo.app.ws.io.entity.UserEntity;
import com.devdemo.app.ws.repository.AddressRepository;
import com.devdemo.app.ws.repository.UserRepository;
import com.devdemo.app.ws.service.AddressService;
import com.devdemo.app.ws.shared.dto.AddressDto;
import com.devdemo.app.ws.shared.util.ErrorMessages;
import lombok.NonNull;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

import static com.devdemo.app.ws.shared.util.ErrorMessages.NO_RECORD_FOUND;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AddressRepository addressRepository;

    private final ModelMapper mapper = new ModelMapper();


    @Override
    public AddressDto getUserAddress(final @NonNull String addressId) {

        final AddressEntity addressEntity = addressRepository.findByAddressId(addressId);
        if (addressEntity == null) {
            throw new IllegalArgumentException(NO_RECORD_FOUND.getErrorMessage());
        }

        return mapper.map(addressEntity, AddressDto.class);
    }

    @Override
    public Collection<AddressDto> getAddresses(final @NonNull String userId) {
        final UserEntity userEntity = userRepository.findByUserId(userId);
        if(userEntity == null) {
            throw new UsernameNotFoundException(NO_RECORD_FOUND.getErrorMessage());
        }

        return addressRepository.findAllByUserDetails(userEntity)
                .stream()
                .map(addressEntity -> mapper.map(addressEntity, AddressDto.class))
                .collect(Collectors.toList());
    }
}
