package com.example.webappfinal.util;

import com.example.webappfinal.model.Admin;
import com.example.webappfinal.repository.UserRepository;
import com.example.webappfinal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

// Use this class just to make an Admin if there isn't one, then use AppStartRunner to run it on start up
@Component
public class AdminInitializer {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public void initAdmin() {
        if (!userRepository.existsByRole("ROLE_ADMIN")) {
            Admin admin = new Admin();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("Mr.Admin118"));
            admin.setRole("ROLE_ADMIN");
            userService.saveUser(admin);
        }
    }
}
