package com.demo.gateway_keycloak.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author HP
 **/
@Entity
@Data
@Table(name = "users")
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class User {
    @Id
    private String id;

    @Column(nullable = false,unique = true)
    private String email;

    private String firstName;
    private String lastName;
    private String username;
    @Column(name = "main_role")
    private String mainRole;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @EqualsAndHashCode.Exclude
    private Set<Role> roles = new HashSet<>();

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime lastLogin;
    private boolean enabled = true;

    public void addRole(Role role) {
        if (this.roles == null) this.roles = new HashSet<>();
        this.roles.add(role);
        if (role.getUsers() == null) role.setUsers(new HashSet<>());
        role.getUsers().add(this);
    }

    public void removeRole(Role role) {
        this.roles.remove(role);
        role.getUsers().remove(this);
    }



}
