package com.devdemo.app.ws.service.impl;

import com.devdemo.app.ws.exception.UserServiceException;
import com.devdemo.app.ws.io.entity.AddressEntity;
import com.devdemo.app.ws.io.entity.PasswordResetTokenEntity;
import com.devdemo.app.ws.repository.PasswordResetTokenRepository;
import com.devdemo.app.ws.service.UserService;
import com.devdemo.app.ws.shared.dto.UserDto;
import com.devdemo.app.ws.shared.util.AmazonSES;
import com.devdemo.app.ws.shared.util.Constant;
import com.devdemo.app.ws.repository.UserRepository;
import com.devdemo.app.ws.io.entity.UserEntity;
import com.devdemo.app.ws.shared.util.ErrorMessages;
import com.devdemo.app.ws.shared.util.Util;
import com.devdemo.app.ws.ui.model.response.operation.RequestOperationStatus;
import io.qala.datagen.RandomShortApi;
import lombok.NonNull;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
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

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    Util util;

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
        userEntity.setEmailVerificationToken(util.generateTokenForUserId(userEntity.getUserId()));
        userEntity.setEmailVerified(Boolean.FALSE);

        userEntity.getAddresses().forEach(
                addressEntity -> {
                    addressEntity.setAddressId(RandomShortApi.alphanumeric(Constant.ADDRESS_ID_LENGTH));
                    addressEntity.setUserDetails(userEntity);
                });

        final UserEntity savedUserDetails = userRepository.save(userEntity);
        final UserDto returnValue = mapper.map(savedUserDetails, UserDto.class);

        //Send email
        //new AmazonSES().verifyEmail(returnValue);

        return returnValue;
    }

    @Override
    public UserDto updateUser(@NonNull final String userId, @NonNull final UserDto userDto) {
        UserDto returnValue = new UserDto();
        UserEntity storedUserDetails = userRepository.findByUserId(userId);

        if (storedUserDetails == null) {
            throw new UsernameNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
        }

        // TODO if field value is not null
        storedUserDetails.setFirstName(userDto.getFirstName());
        storedUserDetails.setLastName(userDto.getLastName());

        if (!userDto.getAddresses().isEmpty()) {
            // TODO if each field value is not null
            final Type addressCollection = new TypeToken<Collection<AddressEntity>>() {}.getType();
            storedUserDetails.setAddresses(mapper.map(userDto.getAddresses(), addressCollection));
        }

        return mapper.map(userRepository.save(storedUserDetails), UserDto.class);
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
    public RequestOperationStatus verifyEmailToken(@NonNull final String token) {
        RequestOperationStatus returnValue = RequestOperationStatus.ERROR;
        UserEntity userEntity = userRepository.findUserByEmailVerificationToken(token);

        if (userEntity != null) {
            if(!Util.hasTokenExpired(token)) {
                userEntity.setEmailVerificationToken(null);
                userEntity.setEmailVerified(Boolean.TRUE);
                userRepository.save(userEntity);
                returnValue = RequestOperationStatus.SUCCESS;
            }
        }

        return returnValue;
    }

    @Override
    public RequestOperationStatus requestPasswordReset(@NonNull final String email) {
        RequestOperationStatus returnValue = RequestOperationStatus.ERROR;
        final UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity == null) {
            return returnValue;
        }

        final String token = Util.generatePasswordResetToken(userEntity.getUserId());
        PasswordResetTokenEntity passwordResetTokenEntity = new PasswordResetTokenEntity();
        passwordResetTokenEntity.setToken(token);
        passwordResetTokenEntity.setUserDetails(userEntity);
        passwordResetTokenRepository.save(passwordResetTokenEntity);

        //returnValue = new AmazonSES().sendPasswordResetRequest(userEntity.getFirstName(), userEntity.getEmail(), token);
        returnValue = RequestOperationStatus.SUCCESS;
        return returnValue;
    }

    @Override
    public RequestOperationStatus resetPassword(@NonNull String token, @NonNull String password) {
        if (Util.hasTokenExpired(token)) {
            return RequestOperationStatus.ERROR;
        }

        final PasswordResetTokenEntity passwordResetTokenEntity = passwordResetTokenRepository.findByToken(token);

        if (passwordResetTokenEntity == null) {
            return RequestOperationStatus.ERROR;
        }

        final String encodedPassword = passwordEncoder.encode(password);

        UserEntity userEntity = passwordResetTokenEntity.getUserDetails();
        userEntity.setEncryptedPassword(encodedPassword);
        final UserEntity savedUserEntity = userRepository.save(userEntity);

        if (savedUserEntity == null || !savedUserEntity.getEncryptedPassword().equalsIgnoreCase(encodedPassword)){
            return RequestOperationStatus.ERROR;
        }

        passwordResetTokenRepository.delete(passwordResetTokenEntity);

        return RequestOperationStatus.SUCCESS;
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
     * @param email in this case email
     * @return User data
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(@NonNull final String email) throws UsernameNotFoundException {
        final UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity == null) {
            throw new UsernameNotFoundException(email);
        }
        return new User(email, userEntity.getEncryptedPassword(), userEntity.getEmailVerified(),
                true, true, true, new ArrayList<>());
    }
}
