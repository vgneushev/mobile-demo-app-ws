package com.devdemo.app.ws.mobiledemoappws.service.impl;

import com.devdemo.app.ws.mobiledemoappws.repository.UserRepository;
import com.devdemo.app.ws.mobiledemoappws.io.entity.UserEntity;
import com.devdemo.app.ws.mobiledemoappws.service.UserService;
import com.devdemo.app.ws.mobiledemoappws.shared.dto.UserDto;
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

import static com.devdemo.app.ws.mobiledemoappws.shared.util.Constant.USER_ID_LENGTH;

@Component
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDto createUser(@NonNull final UserDto userDto) {

        UserEntity storedUserDetails = userRepository.findByEmail(userDto.getEmail());

        if(storedUserDetails != null) {
            throw new RuntimeException("User already exist");
        }

        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(userDto, userEntity);
        userEntity.setEncryptedPassword(passwordEncoder.encode(userDto.getPassword()));
        userEntity.setUserId(RandomShortApi.alphanumeric(USER_ID_LENGTH));

        UserEntity savedUserDetails = userRepository.save(userEntity);
        UserDto returnValue = new UserDto();
        BeanUtils.copyProperties(savedUserDetails, returnValue);

        return returnValue;
    }

    @Override
    public UserDto getUser(@NonNull String email) {

        UserEntity storedUserDetails = userRepository.findByEmail(email);

        if(storedUserDetails == null) {
            throw new RuntimeException("User not found");
        }

        UserDto returnValue = new UserDto();
        BeanUtils.copyProperties(storedUserDetails, returnValue);

        return returnValue;
    }

    @Override
    public UserDto getUserById(@NonNull String id) {

        UserEntity storedUserDetails = userRepository.findByUserId(id);

        if(storedUserDetails == null) {
            throw new RuntimeException("User not found");
        }

        UserDto returnValue = new UserDto();
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
