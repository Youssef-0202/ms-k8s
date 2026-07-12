package com.demo.gateway_keycloak.service;

import com.demo.gateway_keycloak.controller.dto.UserDto;
import com.demo.gateway_keycloak.controller.dto.UserInfoResponse;
import com.demo.gateway_keycloak.controller.facade.UserInfoController;
import com.demo.gateway_keycloak.controller.mapper.UserMapper;
import com.demo.gateway_keycloak.model.Role;
import com.demo.gateway_keycloak.model.User;
import com.demo.gateway_keycloak.repository.RoleRepository;
import com.demo.gateway_keycloak.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author HP
 **/

@Service
@Slf4j
public class UserSyncService {
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final UserMapper userMapper;

    public UserSyncService(UserRepository userRepository, RoleService roleService, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.userMapper = userMapper;
    }

    @Transactional
    public User syncUserFromJwt(Jwt jwt) {
        try {
            String userId = jwt.getSubject();
            String email = jwt.getClaimAsString("email");

            if (email == null) {
                log.warn("No email found in JWT token for user: {}", userId);
                return null;
            }

            return userRepository.findByEmailWithRoles(email)
                    .map(existingUser -> updateExistingUser(existingUser, jwt))
                    .orElseGet(() -> createNewUser(jwt));

        } catch (Exception e) {
            log.error("Error syncing user from JWT", e);
            return null;
        }
    }
    private User updateExistingUser(User user, Jwt jwt) {
        user.setLastLogin(LocalDateTime.now());

        // Update user info if needed
        String firstName = jwt.getClaimAsString("given_name");
        String lastName = jwt.getClaimAsString("family_name");

        if (firstName != null) user.setFirstName(firstName);
        if (lastName != null) user.setLastName(lastName);

        // Sync roles from JWT
        syncUserRoles(user, jwt);

        log.info("Updated user login: {}", user.getEmail());
        return userRepository.save(user);
    }

    private User createNewUser(Jwt jwt) {
        String userId = jwt.getSubject();
        String email = jwt.getClaimAsString("email");
        String firstName = jwt.getClaimAsString("given_name");
        String lastName = jwt.getClaimAsString("family_name");
        String username = jwt.getClaimAsString("preferred_username");

        if (username == null) {
            username = email;
        }

        User newUser =  User.builder().id(userId).email(email).firstName(firstName).lastName(lastName).username(username)
                .mainRole(getAdminOrUser(jwt))
                .build();

        syncUserRoles(newUser, jwt);

        User savedUser = userRepository.save(newUser);
        log.info("Created new user: {} with {} roles", savedUser.getEmail(), savedUser.getRoles().size());
        return savedUser;
    }

    private void syncUserRoles(User user, Jwt jwt) {
        try {
            Set<String> roleNames = extractRolesFromJwt(jwt);
            Set<Role> roles = roleService.findOrCreateRoles(roleNames);

            // Clear existing roles and add new ones
            if(user.getRoles() != null){
                user.getRoles().clear();
            }
            roles.forEach(user::addRole);

            log.debug("Synced {} roles for user: {}", roles.size(), user.getEmail());
        } catch (Exception e) {
            log.warn("Could not sync roles from JWT for user: {}", user.getEmail(), e);
        }
    }

    private Set<String> extractRolesFromJwt(Jwt jwt) {
        Set<String> roles = new HashSet<>();

        try {
            // Extract realm roles
            var realmAccess = jwt.getClaimAsMap("realm_access");
            if (realmAccess != null) {
                var realmRoles = (java.util.List<String>) realmAccess.get("roles");
                if (realmRoles != null) {
                    roles.addAll(realmRoles);
                }
            }


            var resourceAccess = jwt.getClaimAsMap("resource_access");
            if (resourceAccess != null) {
                for (Object clientObj : resourceAccess.values()) {
                    if (clientObj instanceof java.util.Map) {
                        var clientMap = (java.util.Map<String, Object>) clientObj;
                        var clientRoles = (java.util.List<String>) clientMap.get("roles");
                        if (clientRoles != null) {
                            roles.addAll(clientRoles);
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.warn("Error extracting roles from JWT", e);
        }

        // Always add default USER role if no roles found
        if (roles.isEmpty()) {
            roles.add("USER");
        }

        return roles;
    }

    @Transactional(readOnly = true)
    public boolean userExists(String email) {
        return userRepository.existsByEmail(email);
    }

    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers() {
        return userMapper.toDtos(userRepository.findAll());
    }

    public UserInfoResponse getCurrentUser(Jwt jwt){
        String email = jwt.getClaimAsString("email");
        User user = userRepository.findByEmailWithRoles(email).orElse(null);

        return new UserInfoResponse(
                jwt.getClaimAsString("preferred_username"),
                jwt.getClaimAsString("email"),
                user != null ? user.getRoles().stream()
                        .map(role -> role.getName())
                        .collect(Collectors.toSet()) : null
        );
    }

    public String getAdminOrUser(Jwt jwt) {

        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
        if (realmAccess != null && realmAccess.containsKey("roles")) {
            @SuppressWarnings("unchecked")
            List<String> roles = (List<String>) realmAccess.get("roles");

            if (roles.contains("admin")) {
                return "admin";
            } else if (roles.contains("user")) {
                return "user";
            }
        }

        return "No main Role! ";
    }
}
