package com.demo.gateway_keycloak.model;

import com.mysql.cj.x.protobuf.MysqlxCrud;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

/**
 * @author HP
 **/
@Entity
@Table(name = "roles")
@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class Role {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    private String description;

    @ManyToMany(mappedBy = "roles")
    @EqualsAndHashCode.Exclude
    private Set<User> users = new HashSet<>();

    public Role(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
