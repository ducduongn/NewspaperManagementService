package com.example.springsecuritydemo.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/access")
public class AccessController {

    @GetMapping("/all")
    public String publicAccess() {
        return "This content is public!";
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('ROLE_JOURNALIST') or " +
            "hasRole('ROLE_EDITOR') or " +
            "hasRole('_ROLE_DIRECTOR') or " +
            "hasRole('ROLE_ADMIN')")
    public String userAcess() {
        return "This content is for user only";
    }

    @GetMapping("/upload-article")
    @PreAuthorize("hasRole('ROLE_JOURNALIST')")
    public String uploadArticleAccess() {
        return "Journalist uploads here!";
    }

    @GetMapping("/approve-article")
    @PreAuthorize("hasRole('ROLE_EDITOR') or " +
            "hasRole('DIRECTOR')")
    public String approveArticle() {
        return "Editor approve article here!";
    }

    @GetMapping("/user-management")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String manageUser() {
        return "Admin manage user here!";
    }
}
