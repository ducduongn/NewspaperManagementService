package com.example.springsecuritydemo.controller;

import com.example.springsecuritydemo.auth.payload.MessageResponse;
import com.example.springsecuritydemo.models.dto.UserDto;
import com.example.springsecuritydemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequestMapping("api/v1/user-control")
@RestController
public class UserController {
    @Autowired
    private UserService userService;

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

}
