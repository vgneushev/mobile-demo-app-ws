package com.devdemo.app.ws.service;

import com.devdemo.app.ws.shared.dto.UserDto;
import com.devdemo.app.ws.ui.model.response.operation.OperationStatusModel;
import com.devdemo.app.ws.ui.model.response.operation.RequestOperationStatus;
import lombok.NonNull;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    UserDto createUser(@NonNull final UserDto userDto);
    UserDto updateUser(@NonNull final String userId, @NonNull final UserDto userDto);
    UserDto getUser(@NonNull final String email);

    List<UserDto> getUsers(final int page, final int limit);

    UserDto getUserById(@NonNull final String id);
    void deleteUser(@NonNull final String id);
    RequestOperationStatus verifyEmailToken(@NonNull final String token);

    RequestOperationStatus requestPasswordReset(@NonNull final String email);
    RequestOperationStatus resetPassword(@NonNull final String token, @NonNull final String password);
}
