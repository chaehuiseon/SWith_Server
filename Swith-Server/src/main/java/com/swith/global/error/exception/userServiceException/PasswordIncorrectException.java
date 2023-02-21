package com.swith.global.error.exception.userServiceException;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PasswordIncorrectException extends RuntimeException {
    private String message;
}
