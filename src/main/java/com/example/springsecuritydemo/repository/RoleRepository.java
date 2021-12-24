package com.example.springsecuritydemo.repository;

import com.example.springsecuritydemo.models.auth.ERole;
import com.example.springsecuritydemo.models.auth.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(ERole roleName);
}
