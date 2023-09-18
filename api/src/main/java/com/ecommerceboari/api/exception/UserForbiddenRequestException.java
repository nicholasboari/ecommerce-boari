package com.ecommerceboari.api.exception;

public class UserForbiddenRequestException extends RuntimeException {
    public UserForbiddenRequestException(String message) {
        super(message);
    }
}
