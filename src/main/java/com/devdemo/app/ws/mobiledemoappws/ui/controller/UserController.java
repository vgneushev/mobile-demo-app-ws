package com.devdemo.app.ws.mobiledemoappws.ui.controller;

import com.devdemo.app.ws.mobiledemoappws.service.UserService;
import com.devdemo.app.ws.mobiledemoappws.shared.dto.UserDto;
import com.devdemo.app.ws.mobiledemoappws.ui.model.request.UserDetailsRequestModel;
import com.devdemo.app.ws.mobiledemoappws.ui.model.response.UserDetailsResponseModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping
    public String getUser() {
        return "get user was called";
    }

    @PostMapping
    public UserDetailsResponseModel createUser(@RequestBody final UserDetailsRequestModel requestModel) {
        UserDetailsResponseModel responseUser = new UserDetailsResponseModel();
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(requestModel, userDto);
        UserDto createdUser = userService.createUser(userDto);
        BeanUtils.copyProperties(createdUser, responseUser);
        return responseUser;
    }

    @PutMapping
    public String updateUser() {
        return "update user was called";
    }

    @DeleteMapping
    public String deleteUser() {
        return "delete user was called";
    }

}
