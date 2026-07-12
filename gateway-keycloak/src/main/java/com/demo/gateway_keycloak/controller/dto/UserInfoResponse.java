package com.demo.gateway_keycloak.controller.dto;

import lombok.Data;

/**
 * @author HP
 **/

@Data
public class UserInfoResponse {
    private String username;
    private String email;
    private java.util.Set<String> roles;

    public UserInfoResponse(String username, String email, java.util.Set<String> roles) {
        this.username = username;
        this.email = email;
        this.roles = roles;
    }

   }
