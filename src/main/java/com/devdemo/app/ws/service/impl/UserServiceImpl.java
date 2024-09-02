package com.devdemo.app.ws.service.impl;

import com.devdemo.app.ws.exception.UserServiceException;
import com.devdemo.app.ws.io.entity.AddressEntity;
import com.devdemo.app.ws.service.UserService;
import com.devdemo.app.ws.shared.dto.UserDto;
import com.devdemo.app.ws.shared.util.Constant;
import com.devdemo.app.ws.repository.UserRepository;
import com.devdemo.app.ws.io.entity.UserEntity;
import com.devdemo.app.ws.shared.util.ErrorMessages;
import io.qala.datagen.RandomShortApi;
import lombok.NonNull;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.data.domain.Page;


import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    private final ModelMapper mapper = new ModelMapper();

    @Override
    public UserDto createUser(@NonNull final UserDto userDto) {
        UserEntity storedUserDetails = userRepository.findByEmail(userDto.getEmail());

        if (storedUserDetails != null) {
            throw new UserServiceException(ErrorMessages.RECORD_ALREADY_EXIST.getErrorMessage());
        }

        UserEntity userEntity = mapper.map(userDto, UserEntity.class);
        userEntity.setEncryptedPassword(passwordEncoder.encode(userDto.getPassword()));
        userEntity.setUserId(RandomShortApi.alphanumeric(Constant.USER_ID_LENGTH));
        userEntity.getAddresses().forEach(
                addressEntity -> {
                    addressEntity.setAddressId(RandomShortApi.alphanumeric(Constant.ADDRESS_ID_LENGTH));
                    addressEntity.setUserDetails(userEntity);
                });

        final UserEntity savedUserDetails = userRepository.save(userEntity);
        return mapper.map(savedUserDetails, UserDto.class);
    }

    @Override
    public UserDto updateUser(@NonNull final String userId, @NonNull final UserDto userDto) {
        UserDto returnValue = new UserDto();
        UserEntity storedUserDetails = userRepository.findByUserId(userId);

        if (storedUserDetails == null) {
            throw new UsernameNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
        }

        storedUserDetails.setFirstName(userDto.getFirstName());
        storedUserDetails.setLastName(userDto.getLastName());

        UserEntity updatedUserDetails = userRepository.save(storedUserDetails);
        BeanUtils.copyProperties(updatedUserDetails, returnValue);

        return returnValue;
    }

    @Override
    public UserDto getUser(@NonNull final String email) {
        UserDto returnValue = new UserDto();
        UserEntity storedUserDetails = userRepository.findByEmail(email);

        if (storedUserDetails == null) {
            throw new UsernameNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
        }

        BeanUtils.copyProperties(storedUserDetails, returnValue);

        return returnValue;
    }

    @Override
    public List<UserDto> getUsers(final int page, final int limit) {
        List<UserDto> returnUsers = new ArrayList<>(limit);
        Pageable pageable = PageRequest.of(page, limit);
        Page<UserEntity> usersPage = userRepository.findAll(pageable);
        List<UserEntity> users = usersPage.getContent();
        users.forEach(userEntity -> {
            UserDto userDto = new UserDto();
            BeanUtils.copyProperties(userEntity, userDto);
            returnUsers.add(userDto);
        });

        return returnUsers;
    }

    @Override
    public void deleteUser(@NonNull final String userId) {
        UserEntity storedUserDetails = userRepository.findByUserId(userId);

        if (storedUserDetails == null) {
            throw new UsernameNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
        }

        userRepository.delete(storedUserDetails);
    }

    @Override
    public UserDto getUserById(@NonNull final String id) {
        UserDto returnValue = new UserDto();
        UserEntity storedUserDetails = userRepository.findByUserId(id);

        if (storedUserDetails == null) {
            throw new RuntimeException("User not found");
        }

        BeanUtils.copyProperties(storedUserDetails, returnValue);

        return returnValue;
    }

    /**
     * @param username in this case email
     * @return User data
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(@NonNull final String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(username);
        if (username == null) {
            throw new UsernameNotFoundException(username);
        }

        return new User(username, userEntity.getEncryptedPassword(), new ArrayList<>());
    }
}
