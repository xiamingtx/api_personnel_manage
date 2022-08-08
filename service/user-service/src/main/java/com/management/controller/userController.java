package com.management.controller;

import com.management.common.ResponseResult;
import com.management.dto.user.UserCreateRequest;
import com.management.dto.user.UserUpdateRequest;
import com.management.mapper.UserMapper;
import com.management.service.UserService;
import com.management.vo.UserVo;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAuthority('admin')")
    @GetMapping("/list")
    public ResponseResult<List<UserVo>> list() {
        return new ResponseResult<>(2000, "查询成功",
                userService.list().stream().map(mapper::toVo).collect(Collectors.toList()));
    }

    // 根据id查询
    @PreAuthorize("hasAuthority('admin')")
    @GetMapping("/{id}")
    public ResponseResult<UserVo> get(@PathVariable("id") String id) {
        return new ResponseResult<>(2000, "查询成功", mapper.toVo(userService.get(id)));
    }

    // 添加用户
    @PreAuthorize("hasAuthority('admin')")
    @PostMapping
    public ResponseResult<Void> create(@RequestBody UserCreateRequest userCreateRequest) {
        return userService.create(userCreateRequest);
    }

    // 修改
    @PreAuthorize("hasAuthority('admin')")
    @PutMapping("/{id}")
    public ResponseResult<Void> update(@PathVariable String id, @RequestBody UserUpdateRequest userUpdateRequest) {
        return userService.update(id, userUpdateRequest);
    }

    // 删除
    @PreAuthorize("hasAuthority('admin')")
    @DeleteMapping("/{id}")
    public ResponseResult<Void> delete(@PathVariable String id) {
        return userService.delete(id);
    }
}
