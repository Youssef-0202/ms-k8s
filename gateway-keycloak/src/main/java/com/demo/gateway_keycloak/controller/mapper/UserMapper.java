package com.demo.gateway_keycloak.controller.mapper;

import com.demo.gateway_keycloak.controller.dto.RoleDto;
import com.demo.gateway_keycloak.controller.dto.UserDto;
import com.demo.gateway_keycloak.model.Role;
import com.demo.gateway_keycloak.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author HP
 **/

@Service
public class UserMapper {


    public UserDto toDto(User user) {
        if (user == null) return null;

        UserDto dto = new UserDto();
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setUsername(user.getUsername());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setLastLogin(user.getLastLogin());
        dto.setEnabled(user.isEnabled());

        // Map only role names (no full RoleDto needed here)
        if (user.getRoles() != null) {
            dto.setRoles(user.getRoles().stream()
                    .map(role -> {
                        RoleDto roleDto = new RoleDto();
                        roleDto.setName(role.getName());
                        roleDto.setDescription(role.getDescription());
                        return roleDto;
                    })
                    .collect(Collectors.toSet()));
        }

        return dto;
    }




    public User toEntity(UserDto dto) {
        if (dto == null) return null;

        User user = new User();
        user.setEmail(dto.getEmail());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setUsername(dto.getUsername());
        user.setCreatedAt(dto.getCreatedAt());
        user.setLastLogin(dto.getLastLogin());
        user.setEnabled(dto.isEnabled());

        // Map roles (lightweight)
        if (dto.getRoles() != null) {
            Set<Role> roles = dto.getRoles().stream()
                    .map(roleDto -> {
                        Role role = new Role();
                        role.setName(roleDto.getName());
                        role.setDescription(roleDto.getDescription());
                        return role;
                    })
                    .collect(Collectors.toSet());
            user.setRoles(roles);
        }

        return user;
    }




    public List<UserDto> toDtos(List<User> users){
        List<UserDto> userDtos = new ArrayList<>() ;

        if (users == null){
            return null;
        }
        for (User user:users){
            UserDto dto = this.toDto(user);
            userDtos.add(dto);
        }

        return userDtos;
    }
}
