package com.example.springsecuritydemo.controller;

import com.example.springsecuritydemo.auth.jwt.JwtUtils;
import com.example.springsecuritydemo.auth.payload.request.LoginRequest;
import com.example.springsecuritydemo.auth.payload.request.SignUpRequest;
import com.example.springsecuritydemo.auth.payload.respond.JwtResponse;
import com.example.springsecuritydemo.auth.payload.respond.MessageResponse;
import com.example.springsecuritydemo.constant.auth.RoleMessage;
import com.example.springsecuritydemo.constant.auth.UserMessage;
import com.example.springsecuritydemo.models.auth.ERole;
import com.example.springsecuritydemo.models.auth.Role;
import com.example.springsecuritydemo.models.auth.User;
import com.example.springsecuritydemo.repository.auth.RoleRepository;
import com.example.springsecuritydemo.repository.auth.UserRepository;
import com.example.springsecuritydemo.service.auth.UserDetailsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignUpRequest signUpRequest) {
        LOGGER.info("sign up ongoing...");
        if(userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(UserMessage.USERNAME_EXISTED));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(UserMessage.EMAIL_EXISTED));
        }

        User user = new User(
                signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                passwordEncoder.encode(signUpRequest.getPassword())
        );

        Set<String> requestRoles = signUpRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        if (requestRoles == null) {
            Role role = roleRepository.findByRoleName(ERole.READER)
                    .orElseThrow(() -> new RuntimeException(RoleMessage.ROLE_NOT_FOUND));
            roles.add(role);
        } else {
            requestRoles.forEach(roleName -> {
                switch (roleName) {
                    case "journalist":
                        Role journalistRole = roleRepository.findByRoleName(ERole.JOURNALIST)
                                .orElseThrow(() -> new RuntimeException(RoleMessage.ROLE_NOT_FOUND));
                        roles.add(journalistRole);
                        break;
                    case "editor":
                        Role editorRole = roleRepository.findByRoleName(ERole.EDITOR)
                                .orElseThrow(() -> new RuntimeException(RoleMessage.ROLE_NOT_FOUND));
                        roles.add(editorRole);
                        break;
                    case "director":
                        Role directorRole = roleRepository.findByRoleName(ERole.DIRECTOR)
                                .orElseThrow(() -> new RuntimeException(RoleMessage.ROLE_NOT_FOUND));
                        roles.add(directorRole);
                        break;
                    default:
                        Role readerRole = roleRepository.findByRoleName(ERole.READER)
                                .orElseThrow(() -> new RuntimeException(RoleMessage.ROLE_NOT_FOUND));
                        roles.add(readerRole);
                        break;
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("USER_REGISTERED_SUCCESS"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> signIn(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(role -> role.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(
                jwt,
                userDetails.getId(),
                userDetails.getEmail(),
                userDetails.getUsername(),
                roles
        ));
    }
}
