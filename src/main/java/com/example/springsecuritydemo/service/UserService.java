package com.example.springsecuritydemo.service;

import com.example.springsecuritydemo.models.auth.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    public User findUserByEmail(String email);

    public List<User> findUserByFullName(String fullName);

    public User findUserByUsername(String username);
}
