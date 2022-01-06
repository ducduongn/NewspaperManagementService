package com.example.springsecuritydemo;

import com.example.springsecuritydemo.models.auth.ERole;
import com.example.springsecuritydemo.models.auth.Role;
import com.example.springsecuritydemo.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@ComponentScan
@Configuration
@EnableAutoConfiguration(exclude = {RabbitAutoConfiguration.class})
public class SpringSecurityDemoApplication {

    @Autowired
    private RoleRepository roleRepository;


    public static void main(String[] args) {
        SpringApplication.run(SpringSecurityDemoApplication.class, args);
    }

    @PostConstruct
    public void initializeRoles() {
        List<Role> roleList = new ArrayList<>();

        Role adminRole = new Role(ERole.ROLE_ADMIN);
        Role journalistRole = new Role(ERole.ROLE_JOURNALIST);
        Role editorRole = new Role(ERole.ROLE_EDITOR);
        Role directorRole = new Role(ERole.ROLE_DIRECTOR);

        roleList.add(adminRole);
        roleList.add(journalistRole);
        roleList.add(editorRole);
        roleList.add(directorRole);

        for (Role role:roleList) {
            if (!roleRepository.existsByRoleName(role.getRoleName())) {
                roleRepository.save(role);
            }
        }
    }
}
