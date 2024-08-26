package com.devdemo.app.ws.ui.controller;

import com.devdemo.app.ws.exception.UserServiceException;
import com.devdemo.app.ws.service.UserService;
import com.devdemo.app.ws.shared.dto.UserDto;
import com.devdemo.app.ws.ui.model.request.UserDetailsRequestModel;
import com.devdemo.app.ws.shared.util.ErrorMessages;
import com.devdemo.app.ws.ui.model.response.UserDetailsResponseModel;
import lombok.NonNull;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(
            path = "/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public UserDetailsResponseModel getUser(@PathVariable @NonNull final String id) {
        UserDto user = userService.getUserById(id);
        UserDetailsResponseModel responseModel = new UserDetailsResponseModel();
        BeanUtils.copyProperties(user, responseModel);
        return responseModel;
    }

    @PostMapping(
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public UserDetailsResponseModel createUser(
            @RequestBody @NonNull final UserDetailsRequestModel requestModel) throws UserServiceException {

        if (requestModel.getFirstName().isEmpty()) {
            throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
        }

        UserDetailsResponseModel responseUser = new UserDetailsResponseModel();
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(requestModel, userDto);
        UserDto createdUser = userService.createUser(userDto);
        BeanUtils.copyProperties(createdUser, responseUser);
        return responseUser;
    }

    @PutMapping(
            path = "/{id}",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public UserDetailsResponseModel updateUser(
            @RequestBody @NonNull final UserDetailsRequestModel requestModel,
            @PathVariable @NonNull final String id) {

        UserDto userDto = new UserDto();
        UserDetailsResponseModel responseUser = new UserDetailsResponseModel();
        BeanUtils.copyProperties(requestModel, userDto);

        UserDto updatedUser = userService.updateUser(id, userDto);
        BeanUtils.copyProperties(updatedUser, responseUser);

        return responseUser;
    }

    @DeleteMapping
    public String deleteUser() {
        return "delete user was called";
    }

}
