package com.devdemo.app.ws.mobiledemoappws.service;

import com.devdemo.app.ws.mobiledemoappws.shared.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    UserDto createUser(UserDto userDto);
}
