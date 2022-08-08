package com.management.controller;

import com.management.common.ResponseResult;
import com.management.dto.user.UserCreateRequest;
import com.management.mapper.UserMapper;
import com.management.service.UserService;
import com.management.vo.UserVo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 夏明
 * @version 1.0
 */
@RestController
@RequestMapping("/user")
public class userController {

    @Resource
    UserService userService;

    @Resource
    UserMapper mapper;

    // 查询用户列表
    @GetMapping("/list")
    public ResponseResult<List<UserVo>> list() {
        return new ResponseResult<>(2000, "查询成功",
                userService.list().stream().map(mapper::toVo).collect(Collectors.toList()));
    }

    // 根据id查询
    @GetMapping("/{id}")
    public ResponseResult<UserVo> get(@PathVariable("id") String id) {
        return new ResponseResult<>(2000, "查询成功", mapper.toVo(userService.get(id)));
    }

    // 添加用户
    @PostMapping
    public ResponseResult<Void> create(@RequestBody UserCreateRequest userCreateRequest) {
        return userService.create(userCreateRequest);
    }
}
