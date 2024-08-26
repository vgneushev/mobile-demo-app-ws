package com.devdemo.app.ws.ui.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public enum ErrorMessages {
    MISSING_REQUIRED_FIELD("Required field is missing. Please, check documentation"),
    RECORD_ALREADY_EXIST("Record already exist"),
    INTERNAL_SERVER_ERROR("Internal server error"),
    NO_RECORD_FOUND("Record with provided ID is not found"),
    AUTHENTICATION_FAILED("Authentication failed"),
    COULD_NOT_UPDATE_RECORD("Could not update record"),
    COULD_NOT_UPDATE_DELETE("Could not delete record"),
    EMAIL_ADDRESS_NOT_VERIFIED("Email address could not be verified");

    @Setter
    @Getter
    private String errorMessage;
}
