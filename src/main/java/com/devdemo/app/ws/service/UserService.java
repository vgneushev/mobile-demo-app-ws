package com.devdemo.app.ws.service;

import com.devdemo.app.ws.shared.dto.UserDto;
import lombok.NonNull;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    UserDto createUser(@NonNull final UserDto userDto);
    UserDto getUser(@NonNull final String email);
    UserDto getUserById(@NonNull final String id);
}
