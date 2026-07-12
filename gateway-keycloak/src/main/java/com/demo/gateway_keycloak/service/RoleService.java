package com.demo.gateway_keycloak.service;

import com.demo.gateway_keycloak.model.Role;
import com.demo.gateway_keycloak.repository.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

/**
 * @author HP
 **/
@Service
@Slf4j
public class RoleService {
    private final RoleRepository repository;

    public RoleService(RoleRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Role findOrCreateRole( String roleName){
        return repository.findByName(roleName).orElseGet(() ->{
                Role newRole = Role.builder().name(roleName).description("Auto-generated role").build();
                log.info("Creating new role: {}", roleName);
                return repository.save(newRole);
        });
    }

    @Transactional
    public Set<Role> findOrCreateRoles(Set<String> roleNames){
        Set<Role> roles = new HashSet<>();

        for(String roleName:roleNames){
            Role role = findOrCreateRole(roleName);
            roles.add(role);
        }

        return roles;
    }

    @Transactional(readOnly = true)
    public Set<Role> getRolesByNames(Set<String> roleNames) {
        return repository.findByNames(roleNames);
    }

    @Transactional(readOnly = true)
    public Set<Role> getRolesByUserId(String userId) {
        return repository.findByUserId(userId);
    }

    @Transactional
    public void initializeDefaultRoles() {
        String[] defaultRoles = {"user", "admin"};

        for (String roleName : defaultRoles) {
            if (!repository.existsByName(roleName)) {
                Role role = new Role(roleName, "Default system role");
                repository.save(role);
                log.info("Created default role: {}", roleName);
            }
        }
    }

}
