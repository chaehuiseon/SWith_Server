package com.swith.security;

import com.swith.src.enums.RoleType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class AuthInfo {
    private String token;
    private String email;
    private List<RoleType> role;
}
