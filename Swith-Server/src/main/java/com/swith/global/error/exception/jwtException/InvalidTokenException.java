package com.swith.global.error.exception.jwtException;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class InvalidTokenException extends RuntimeException{
    private String message;
}
