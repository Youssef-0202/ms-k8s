package com.demo.gateway_keycloak.controller.dto;

import com.demo.gateway_keycloak.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * @author HP
 **/

@AllArgsConstructor
@Data
@RequiredArgsConstructor
public class UserDto {
    private String email;
    private String firstName;
    private String lastName;
    private String username;
    private Set<RoleDto> roles = new HashSet<>();
    private LocalDateTime createdAt;
    private LocalDateTime lastLogin;
    private boolean enabled = true;
}
