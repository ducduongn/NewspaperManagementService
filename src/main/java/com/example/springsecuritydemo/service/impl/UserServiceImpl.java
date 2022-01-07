package com.example.springsecuritydemo.service.impl;

import com.example.springsecuritydemo.models.auth.User;
import com.example.springsecuritydemo.repository.UserRepository;
import com.example.springsecuritydemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public User findUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElse(null);

        return user;
    }

    @Override
    public List<User> findUserByFullName(String fullName) {
        return userRepository.findUserByFullName(fullName);
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElse(null);
    }
}
