package com.demo.gateway_keycloak.controller.facade;

import com.demo.gateway_keycloak.controller.dto.UserDto;
import com.demo.gateway_keycloak.controller.dto.UserInfoResponse;
import com.demo.gateway_keycloak.model.User;
import com.demo.gateway_keycloak.repository.UserRepository;
import com.demo.gateway_keycloak.service.UserSyncService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author HP
 **/
@RestController
@RequestMapping("/api/user-info")
public class UserInfoController {

   private final UserSyncService service;

    public UserInfoController(UserSyncService service) {
        this.service = service;
    }


    @GetMapping("/me")
    public ResponseEntity<UserInfoResponse> getCurrentUser(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(service.getCurrentUser(jwt));
    }

    @GetMapping("/all-users")
    public List<UserDto> getAllUsers() {
        return service.getAllUsers();
    }

}
