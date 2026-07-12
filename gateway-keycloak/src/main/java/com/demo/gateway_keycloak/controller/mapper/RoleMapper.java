package com.demo.gateway_keycloak.controller.mapper;

import com.demo.gateway_keycloak.controller.dto.RoleDto;
import com.demo.gateway_keycloak.controller.dto.UserDto;
import com.demo.gateway_keycloak.model.Role;
import com.demo.gateway_keycloak.model.User;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author HP
 **/

@Service
public class RoleMapper {
    public RoleDto toDto(Role role) {
        if (role == null) return null;

        RoleDto dto = new RoleDto();
        dto.setName(role.getName());
        dto.setDescription(role.getDescription());

        // Map only usernames instead of full UserDto
        if (role.getUsers() != null) {
            dto.setUsers(role.getUsers().stream()
                    .map(user -> {
                        UserDto u = new UserDto();
                        u.setUsername(user.getUsername());
                        u.setEmail(user.getEmail());
                        return u;
                    })
                    .collect(Collectors.toSet()));
        }

        return dto;
    }


    public Role toEntity(RoleDto dto) {
        if (dto == null) return null;

        Role role = new Role();
        role.setName(dto.getName());
        role.setDescription(dto.getDescription());

        // Map users (lightweight)
        if (dto.getUsers() != null) {
            Set<User> users = dto.getUsers().stream()
                    .map(userDto -> {
                        User user = new User();
                        user.setUsername(userDto.getUsername());
                        user.setEmail(userDto.getEmail());
                        return user;
                    })
                    .collect(Collectors.toSet());
            role.setUsers(users);
        }

        return role;
    }

}
