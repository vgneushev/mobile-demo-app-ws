package com.devdemo.app.ws.ui.controller;

import com.devdemo.app.ws.exception.UserServiceException;
import com.devdemo.app.ws.service.AddressService;
import com.devdemo.app.ws.service.UserService;
import com.devdemo.app.ws.shared.dto.AddressDto;
import com.devdemo.app.ws.shared.dto.UserDto;
import com.devdemo.app.ws.ui.model.request.UserDetailsRequestModel;
import com.devdemo.app.ws.shared.util.ErrorMessages;
import com.devdemo.app.ws.ui.model.response.AddressesResponseModel;
import com.devdemo.app.ws.ui.model.response.operation.OperationStatusModel;
import com.devdemo.app.ws.ui.model.response.operation.RequestOperationName;
import com.devdemo.app.ws.ui.model.response.operation.RequestOperationStatus;
import com.devdemo.app.ws.ui.model.response.UserDetailsResponseModel;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AddressService addressService;

    private final ModelMapper mapper = new ModelMapper();

    @GetMapping(
            path = "/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public UserDetailsResponseModel getUser(@PathVariable @NonNull final String id) {
        return mapper.map(
                userService.getUserById(id),
                UserDetailsResponseModel.class);
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public List<UserDetailsResponseModel> getUsers(
            @RequestParam(value = "page", defaultValue = "0") final int page,
            @RequestParam(value = "limit", defaultValue = "25") final int limit) {
        return  userService.getUsers(page, limit)
                .stream()
                .map(userDto -> mapper.map(
                        userDto,
                        UserDetailsResponseModel.class))
                .collect(Collectors.toList());
    }

    @PostMapping(
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public UserDetailsResponseModel createUser(
            @RequestBody @NonNull final UserDetailsRequestModel requestModel) throws UserServiceException {

        if (requestModel.getFirstName().isEmpty()) { // TODO verify required fields
            throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
        }

        final UserDto userDto =  mapper.map(requestModel, UserDto.class);
        final UserDto createdUser = userService.createUser(userDto);
        return mapper.map(createdUser, UserDetailsResponseModel.class);
    }

    @PutMapping(
            path = "/{id}",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public UserDetailsResponseModel updateUser(
            @RequestBody @NonNull final UserDetailsRequestModel requestModel,
            @PathVariable @NonNull final String id) {

        final UserDto userDto = mapper.map(requestModel, UserDto.class);
        final UserDto updatedUser = userService.updateUser(id, userDto);
        return  mapper.map(updatedUser, UserDetailsResponseModel.class);
    }

    @DeleteMapping(
            path = "/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public OperationStatusModel deleteUser(@PathVariable @NonNull final String id) {

        userService.deleteUser(id);

        return new OperationStatusModel(
                RequestOperationStatus.SUCCESS.name(), RequestOperationName.DELETE.name());
    }

    @GetMapping(
            path = "/{id}/addresses",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public Collection<AddressesResponseModel> getUserAddresses(@PathVariable @NonNull final String id) {
        final Type listType = new TypeToken<List<AddressesResponseModel>>() {}.getType();
        Collection<AddressesResponseModel> responseModels = new ArrayList<>();

        Collection<AddressDto> addressDtos = addressService.getAddresses(id);
        if (addressDtos != null) {
            responseModels = mapper.map(addressDtos, listType);
        }

        return responseModels;
    }

}
