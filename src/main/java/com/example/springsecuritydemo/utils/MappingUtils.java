package com.example.springsecuritydemo.utils;

import com.example.springsecuritydemo.models.auth.ERole;
import com.example.springsecuritydemo.models.auth.Role;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MappingUtils {
    public static Set<String> convertERoleListToStringList(Set<Role> roles) {
        Set<String> roleStringList = new HashSet<>();

        for(Role role: roles) {
            roleStringList.add(role.getRoleName().name());
        }

        return roleStringList;
    }
}
