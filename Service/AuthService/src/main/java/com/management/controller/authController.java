package com.management.controller;

import com.management.common.ResponseResult;
import com.management.dto.UserCreateRequest;
import com.management.dto.UserLoginRequest;
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
    public ResponseResult register(@RequestBody UserCreateRequest user) {
        return userService.register(user);
    }

    @PostMapping("/code")
    public ResponseResult sendCode(@RequestParam String mail) {
        return userService.sendCode(mail);
    }

    @PostMapping("/login")
    public ResponseResult<Map<String, String>> login(@RequestBody UserLoginRequest user) {
        return userService.login(user);
    }

    @PostMapping("/logout")
    public ResponseResult logout() {
        return userService.logout();
    }
}
