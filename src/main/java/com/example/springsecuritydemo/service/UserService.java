package com.example.springsecuritydemo.service;

import com.example.springsecuritydemo.models.auth.User;
import com.example.springsecuritydemo.models.dto.UserCreateDto;
import com.example.springsecuritydemo.models.dto.UserDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    List<UserDto> findAllUser();

    User findUserByEmail(String email);

    List<User> findUserByFullName(String fullName);

    UserDto findUserByUsername(String username);

    UserDto createUser(UserCreateDto createDto);

    Boolean checkIfEmailExisted(String email);

    Boolean checkIfUsernameExisted(String username);

    User saveUser(User user);
}
