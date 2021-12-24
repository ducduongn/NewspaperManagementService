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
    @PreAuthorize("hasRole('JOURNALIST') or " +
            "hasRole('EDITOR') or " +
            "hasRole('DIRECTOR') or " +
            "hasRole('ADMIN')")
    public String userAcess() {
        return "This content is for user only";
    }

    @GetMapping("/upload-article")
    @PreAuthorize("hasRole('JOURNALIST')")
    public String uploadArticleAccess() {
        return "Journalist uploads here!";
    }

    @GetMapping("/approve-article")
    @PreAuthorize("hasRole('EDITOR') or " +
            "hasRole('DIRECTOR')")
    public String approveArticle() {
        return "Editor approve article here!";
    }

    @GetMapping("/user-management")
    @PreAuthorize("hasRole('ADMIN')")
    public String manageUser() {
        return "Admin manange user here!";
    }

}
