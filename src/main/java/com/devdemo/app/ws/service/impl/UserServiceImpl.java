package com.devdemo.app.ws.service.impl;

import com.devdemo.app.ws.exception.UserServiceException;
import com.devdemo.app.ws.service.UserService;
import com.devdemo.app.ws.shared.dto.UserDto;
import com.devdemo.app.ws.shared.util.Constant;
import com.devdemo.app.ws.repository.UserRepository;
import com.devdemo.app.ws.io.entity.UserEntity;
import com.devdemo.app.ws.shared.util.ErrorMessages;
import io.qala.datagen.RandomShortApi;
import lombok.NonNull;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDto createUser(@NonNull final UserDto userDto) {
        UserDto returnValue = new UserDto();
        UserEntity storedUserDetails = userRepository.findByEmail(userDto.getEmail());

        if(storedUserDetails != null) {
            throw new UserServiceException(ErrorMessages.RECORD_ALREADY_EXIST.getErrorMessage());
        }

        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(userDto, userEntity);
        userEntity.setEncryptedPassword(passwordEncoder.encode(userDto.getPassword()));
        userEntity.setUserId(RandomShortApi.alphanumeric(Constant.USER_ID_LENGTH));

        UserEntity savedUserDetails = userRepository.save(userEntity);
        BeanUtils.copyProperties(savedUserDetails, returnValue);

        return returnValue;
    }

    @Override
    public UserDto updateUser(@NonNull String userId, @NonNull UserDto userDto) {
        UserDto returnValue = new UserDto();
        UserEntity storedUserDetails = userRepository.findByUserId(userId);

        if(storedUserDetails == null) {
            throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
        }

        storedUserDetails.setFirstName(userDto.getFirstName());
        storedUserDetails.setLastName(userDto.getLastName());

        UserEntity updatedUserDetails = userRepository.save(storedUserDetails);
        BeanUtils.copyProperties(updatedUserDetails, returnValue);

        return returnValue;
    }

    @Override
    public UserDto getUser(@NonNull String email) {
        UserDto returnValue = new UserDto();
        UserEntity storedUserDetails = userRepository.findByEmail(email);

        if(storedUserDetails == null) {
            throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
        }

        BeanUtils.copyProperties(storedUserDetails, returnValue);

        return returnValue;
    }

    @Override
    public UserDto getUserById(@NonNull String id) {
        UserDto returnValue = new UserDto();
        UserEntity storedUserDetails = userRepository.findByUserId(id);

        if(storedUserDetails == null) {
            throw new RuntimeException("User not found");
        }

        BeanUtils.copyProperties(storedUserDetails, returnValue);

        return returnValue;
    }

    /**
     *
     * @param username in this case email
     * @return User data
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(@NonNull final String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(username);
        if (username==null) {
            throw new UsernameNotFoundException(username);
        }

        return new User(username, userEntity.getEncryptedPassword(), new ArrayList<>());
    }
}
