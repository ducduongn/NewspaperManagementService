package com.example.springsecuritydemo.controller;

import com.example.springsecuritydemo.auth.payload.MessageResponse;
import com.example.springsecuritydemo.models.auth.User;
import com.example.springsecuritydemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/user-control")
@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/get-by-username")
    public ResponseEntity<?> getUserByUsername(@RequestParam String username) {
        User user = userService.findUserByUsername(username);

        if (user != null) {
            return ResponseEntity.ok("User found: " + user.toString());
        }
        return ResponseEntity.ok(new MessageResponse("No user have been found!"));
    }

//    public ResponseEntity<?> updateUser()

}
