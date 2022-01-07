package com.example.springsecuritydemo.controller;

import auth.payload.MessageResponse;
import com.example.springsecuritydemo.constant.UserMessage;
import com.example.springsecuritydemo.models.auth.Role;
import com.example.springsecuritydemo.models.auth.User;
import com.example.springsecuritydemo.models.dto.UserCreateDto;
import com.example.springsecuritydemo.models.dto.UserDto;
import com.example.springsecuritydemo.service.UserService;
import com.example.springsecuritydemo.worker.RoleChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping("api/v1/user-control")
@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleChecker roleChecker;

    @GetMapping("/get-by-username")
    public ResponseEntity<?> getUserByUsername(@RequestParam String username) {
        UserDto userDto = userService.findUserByUsername(username);

        if (userDto != null) {
            return ResponseEntity.ok(userDto);
        }
        return ResponseEntity.ok(new MessageResponse("No user have been found!"));
    }

    @PreAuthorize("hasRole('ROLE_JOURNALIST')")
    @GetMapping("/get-all")
    public ResponseEntity<?> gertAllUser() {
        List<UserDto> users = userService.findAllUser();

        if (users.size() > 0) {
            return ResponseEntity.ok(users);
        }
        return ResponseEntity.ok(new MessageResponse("No user have been found!"));
    }

    @PutMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody UserCreateDto userCreateDto) {
        if(userService.checkIfUsernameExisted(userCreateDto.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(UserMessage.USERNAME_EXISTED));
        }

        if (userService.checkIfEmailExisted(userCreateDto.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(UserMessage.EMAIL_EXISTED));
        }

        User user = new User(
                userCreateDto.getUsername(),
                userCreateDto.getFullName(),
                userCreateDto.getEmail(),
                passwordEncoder.encode(userCreateDto.getPassword())
        );

        Set<String> requestRoles = userCreateDto.getRoleList();

        Set<Role> roles = roleChecker.getRolesFromDto(requestRoles);

        user.setRoles(roles);

        userService.saveUser(user);

        return ResponseEntity.ok(new MessageResponse("CREATE USER SUCCESSFULLY!"));}
}
