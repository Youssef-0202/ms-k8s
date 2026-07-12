package com.demo.gateway_keycloak.repository;

import com.demo.gateway_keycloak.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

/**
 * @author HP
 **/
@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
    Optional<Role> findByName(String name);
    boolean existsByName(String name);

    @Query("SELECT r FROM Role r WHERE r.name IN :roleNames")
    Set<Role> findByNames(Set<String> roleNames);

    @Query("SELECT r FROM Role r JOIN r.users u WHERE u.id = :userId")
    Set<Role> findByUserId(String userId);
}
