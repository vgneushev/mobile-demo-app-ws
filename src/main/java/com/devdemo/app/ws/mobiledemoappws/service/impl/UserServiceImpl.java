package com.devdemo.app.ws.mobiledemoappws.service.impl;

import com.devdemo.app.ws.mobiledemoappws.repository.UserRepository;
import com.devdemo.app.ws.mobiledemoappws.io.entity.UserEntity;
import com.devdemo.app.ws.mobiledemoappws.service.UserService;
import com.devdemo.app.ws.mobiledemoappws.shared.dto.UserDto;
import io.qala.datagen.RandomShortApi;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.devdemo.app.ws.mobiledemoappws.util.Constant.ENCRYPTED_PASSWORD_LENGTH;
import static com.devdemo.app.ws.mobiledemoappws.util.Constant.USER_ID_LENGTH;

@Component
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDto createUser(UserDto userDto) {

        UserEntity storedUserDetails = userRepository.findByEmail(userDto.getEmail());

        if(storedUserDetails != null) {
            throw new RuntimeException("User already exist");
        }

        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(userDto, userEntity);
        userEntity.setEncryptedPassword(RandomShortApi.alphanumeric(ENCRYPTED_PASSWORD_LENGTH));
        userEntity.setUserId(RandomShortApi.alphanumeric(USER_ID_LENGTH));

        UserEntity savedUserDetails = userRepository.save(userEntity);
        UserDto returnValue = new UserDto();
        BeanUtils.copyProperties(savedUserDetails, returnValue);

        return returnValue;
    }

}
