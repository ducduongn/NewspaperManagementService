package com.example.springsecuritydemo.worker;

import com.example.springsecuritydemo.constant.RoleMessage;
import com.example.springsecuritydemo.models.auth.ERole;
import com.example.springsecuritydemo.models.auth.Role;
import com.example.springsecuritydemo.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class RoleChecker {
    @Autowired
    private RoleRepository roleRepository;

    public Set<Role> getRolesFromDto(Set<String> requestRoles) {
        Set<Role> roles = new HashSet<>();

        if (requestRoles == null) {
            Role role = roleRepository.findByRoleName(ERole.ROLE_JOURNALIST)
                    .orElseThrow(() -> new RuntimeException(RoleMessage.ROLE_NOT_FOUND));
            roles.add(role);
        } else {
            requestRoles.forEach(roleName -> {
                switch (roleName) {
                    case "admin":
                        Role journalistRole = roleRepository.findByRoleName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException(RoleMessage.ROLE_NOT_FOUND));
                        roles.add(journalistRole);
                        break;
                    case "editor":
                        Role editorRole = roleRepository.findByRoleName(ERole.ROLE_EDITOR)
                                .orElseThrow(() -> new RuntimeException(RoleMessage.ROLE_NOT_FOUND));
                        roles.add(editorRole);
                        break;
                    case "director":
                        Role directorRole = roleRepository.findByRoleName(ERole.ROLE_DIRECTOR)
                                .orElseThrow(() -> new RuntimeException(RoleMessage.ROLE_NOT_FOUND));
                        roles.add(directorRole);
                        break;
                    default:
                        Role readerRole = roleRepository.findByRoleName(ERole.ROLE_JOURNALIST)
                                .orElseThrow(() -> new RuntimeException(RoleMessage.ROLE_NOT_FOUND));
                        roles.add(readerRole);
                        break;
                }
            });
        }
        return roles;
    }
}
