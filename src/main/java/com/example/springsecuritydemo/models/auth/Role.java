package com.example.springsecuritydemo.models.auth;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_name")
    private ERole roleName;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "roles")
    private Set<User> users;


    public Role() {

    }

}
