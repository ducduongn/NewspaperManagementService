package com.example.springsecuritydemo.service.impl;

import com.example.springsecuritydemo.constant.RoleMessage;
import com.example.springsecuritydemo.models.auth.ERole;
import com.example.springsecuritydemo.models.auth.Role;
import com.example.springsecuritydemo.models.auth.User;
import com.example.springsecuritydemo.models.dto.UserCreateDto;
import com.example.springsecuritydemo.models.dto.UserDto;
import com.example.springsecuritydemo.repository.RoleRepository;
import com.example.springsecuritydemo.repository.UserRepository;
import com.example.springsecuritydemo.service.UserService;
import com.example.springsecuritydemo.utils.MappingUtils;
import com.example.springsecuritydemo.worker.RoleChecker;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private RoleChecker roleChecker;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ModelMapper modelMapper;


    @Override
    public List<UserDto> findAllUser() {
        List<UserDto> userDtoList = new ArrayList<>();

        List<User> userList;

        try {
            userList = userRepository.findAll();

            for(User user:userList) {
                UserDto userDto = modelMapper.map(user, UserDto.class);

                userDto.setRoleList(MappingUtils.convertERoleListToStringList(user.getRoles()));

                if (userDto.getUsername() != null) {
                    userDtoList.add(userDto);
                } else {
                    log.error("Error in mapping user to userDto!");
                }
            }
        } catch (Exception exception) {
            log.error("Error find all user!");
        }
        return userDtoList;
    }

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
    public UserDto findUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElse(null);

        log.info("user: " + user);

        if (user != null) {
            UserDto userDto = modelMapper.map(user, UserDto.class);

            userDto.setRoleList(MappingUtils.convertERoleListToStringList(user.getRoles()));

            return userDto;
        }
        return null;
    }

    @Override
    public UserDto createUser(UserCreateDto userCreateDto) {
        User createdUser = new User();

        createdUser.setUsername(userCreateDto.getUsername());
        createdUser.setEmail(createdUser.getEmail());
        createdUser.setFullName(userCreateDto.getFullName());
        createdUser.setPassword(userCreateDto.getPassword());

        Set<String> createRoleStringList = userCreateDto.getRoleList();

        Set<Role> roles = roleChecker.getRolesFromDto(createRoleStringList);

        createdUser.setRoles(roles);

        return null;
    }

    @Override
    public Boolean checkIfEmailExisted(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public Boolean checkIfUsernameExisted(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

}
