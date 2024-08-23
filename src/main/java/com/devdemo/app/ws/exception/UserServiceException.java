package com.devdemo.app.ws.exception;

public class UserServiceException extends RuntimeException{

    private static final long serialVersionUID = 123456789076543L;

    public UserServiceException(String message) {
        super(message);
    }

}
