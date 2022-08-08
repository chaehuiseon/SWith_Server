package com.example.demo.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class AuthInfo {
    private String token;
    private String email;
}
