package com.example.springsecuritydemo.repository;

import com.example.springsecuritydemo.models.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface       UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserId(Long userId);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);

    Boolean existsByUserId(Long userId);

    List<User> findUserByFullName(String fullName);

    Boolean existsByUsername(String username);
}
