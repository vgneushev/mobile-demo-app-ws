package com.devdemo.app.ws.service.impl;

import com.devdemo.app.ws.io.entity.AddressEntity;
import com.devdemo.app.ws.io.entity.UserEntity;
import com.devdemo.app.ws.repository.AddressRepository;
import com.devdemo.app.ws.repository.UserRepository;
import com.devdemo.app.ws.service.AddressService;
import com.devdemo.app.ws.shared.dto.AddressDto;
import lombok.NonNull;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AddressRepository addressRepository;

    private final ModelMapper mapper = new ModelMapper();


    @Override
    public Collection<AddressDto> getAddresses(final @NonNull String userId) {
        Collection<AddressDto> returnValue = new ArrayList<>();

        final UserEntity userEntity = userRepository.findByUserId(userId);
        if(userEntity == null) {
            return returnValue;
        }

        Collection<AddressEntity> address = addressRepository.findAllByUserDetails(userEntity);
        address
                .forEach(
                        addressEntity ->
                                returnValue.add(mapper.map(addressEntity, AddressDto.class)));
        return returnValue;
    }
}
