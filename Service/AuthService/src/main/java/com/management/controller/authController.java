package com.management.controller;

import com.management.ResponseResult;
import com.management.entity.User;
import com.management.service.UserAuthService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author 夏明
 * @version 1.0
 */
@RestController
@RequestMapping("/auth")
@CrossOrigin
public class authController {

    @Resource
    private UserAuthService userService;

    @PostMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/login")
    public ResponseResult<Map<String, String>> login(@RequestBody User user) {
        return userService.login(user);
    }

    @PostMapping("/logout")
    public ResponseResult logout() {
        return userService.logout();
    }
}
