package com.example.springsecuritydemo.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class UserCreateDto {
    @NotBlank
    @Size(min = 5, max = 20)
    private String username;

    @NotBlank
    @Email
    @Size(max = 20)
    private String email;

    @NotBlank
    @Email
    @Size(max = 20)
    private String password;

    @NotBlank
    private String fullName;

    @NotNull
    private Set<String> roleList;

    public UserCreateDto(String username,
                         String email,
                         String password,
                         String fullName) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.fullName = fullName;
    }
}
