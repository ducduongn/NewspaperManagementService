package com.example.springsecuritydemo.auth.payload;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SignUpRequest {
    @NotBlank
    @Size(min = 5, max = 20)
    private String username;

    @NotBlank
    @Email
    @Size(max = 20)
    private String email;

    @NotBlank
    @Size(max = 40)
    private String password;

    private Set<String> roles;
}
