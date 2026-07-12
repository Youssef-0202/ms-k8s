package com.demo.gateway_keycloak.controller.facade;


import com.demo.gateway_keycloak.service.UserSyncService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author HP
 **/
@RestController
@RequestMapping("/api")
public class Test {

    private final UserSyncService userSyncService;

    public Test(UserSyncService userSyncService) {
        this.userSyncService = userSyncService;
    }

    @GetMapping("/test")
    public String test(@AuthenticationPrincipal Jwt jwt){
        return userSyncService.getAdminOrUser(jwt);

    }
}
