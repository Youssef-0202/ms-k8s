package com.demo.gateway_keycloak.controller.dto;

import com.demo.gateway_keycloak.model.User;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.HashSet;
import java.util.Set;

/**
 * @author HP
 **/
@AllArgsConstructor
@Data
@RequiredArgsConstructor
public class RoleDto {
    private String name;
    private String description;
    private Set<UserDto> users = new HashSet<>();
}
