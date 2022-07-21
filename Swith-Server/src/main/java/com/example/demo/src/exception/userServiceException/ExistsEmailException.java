package com.example.demo.src.exception.userServiceException;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ExistsEmailException extends RuntimeException{
    private String message;
}
