package com.management.controller;

import com.management.common.ResponseResult;
import com.management.dto.user.UserRegisterRequest;
import com.management.dto.user.UserLoginRequest;
import com.management.mapper.UserMapper;
import com.management.service.UserAuthService;
import com.management.vo.UserVo;
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

    @Resource
    private UserMapper mapper;

    @PostMapping("/register")
    public ResponseResult<Void> register(@RequestBody UserRegisterRequest userRegisterRequest) {
        return userService.register(userRegisterRequest);
    }

    @PostMapping("/code")
    public ResponseResult<Void> sendCode(@RequestParam String mail) {
        return userService.sendCode(mail);
    }

    @PostMapping("/login")
    public ResponseResult<Map<String, String>> login(@RequestBody UserLoginRequest UserLoginRequest) {
        return userService.login(UserLoginRequest);
    }

    @PostMapping("/logout")
    public ResponseResult<Void> logout() {
        return userService.logout();
    }

    @GetMapping("/me")
    public ResponseResult<UserVo> fetchCurrentUser() {
        return new ResponseResult<>(2000, "查询成功",
                mapper.toVo(userService.fetchCurrentUser()));
    }
}
