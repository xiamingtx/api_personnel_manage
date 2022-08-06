package com.management.controller;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 夏明
 * @version 1.0
 */
@RestController
@RequestMapping("/auth")
public class authController {

    @GetMapping("/register")
    public String register() {
        System.out.println(new BCryptPasswordEncoder().encode("123456"));
        return "register";
    }
}
