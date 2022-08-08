package com.management.service.impl;

import com.management.common.ResponseResult;
import com.management.dto.user.UserCreateRequest;
import com.management.dto.user.UserDto;
import com.management.entity.Role;
import com.management.entity.User;
import com.management.exception.BizException;
import com.management.exception.ExceptionType;
import com.management.mapper.UserMapper;
import com.management.repository.UserRepository;
import com.management.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author 夏明
 * @version 1.0
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    UserRepository repository;

    @Resource
    UserMapper mapper;

    @Resource
    PasswordEncoder passwordEncoder;

    /**
     * 查询用户列表
     * @author 夏明
     * @date 2022/8/8 14:28
     * @return List<UserDto>
     */
    @PreAuthorize("hasAuthority('admin')")
    @Override
    public List<UserDto> list() {
        return repository.findAll().stream().map(mapper::toDto).collect(Collectors.toList());
    }

    /**
     * 根据id查询用户
     * @author 夏明
     * @date 2022/8/8 14:28
     * @param id
     * @return UserDto
     */
    @PreAuthorize("hasAuthority('admin')")
    @Override
    public UserDto get(String id) {
        Optional<User> userOptional = repository.findById(id);
        if (!userOptional.isPresent()) {
            throw new BizException(ExceptionType.USER_NOT_FOUND);
        }
        return mapper.toDto(userOptional.get());
    }

    /**
     * 添加用户
     * @author 夏明
     * @date 2022/8/8 14:43
     * @param userCreateRequest
     */
    @Transactional
    @Override
    public ResponseResult<Void> create(UserCreateRequest userCreateRequest) {
        // 查询用户是否已存在
        String username = userCreateRequest.getUsername();
        if (repository.findUserByUsername(username) != null) {
            // 抛出用户已存在异常
            throw new BizException(ExceptionType.USER_NAME_DUPLICATE);
        }
        User user = mapper.createEntity(userCreateRequest);
        addUser(user);
        return new ResponseResult<>(2000, "添加成功!");
    }

    public void addUser(User user) {
        // 对密码进行加密
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // 默认昵称
        user.setNickname(user.getUsername());
        // 设置默认权限
        Role role = new Role();
        role.setId("1");
        user.setRoles(Collections.singletonList(role));
        repository.save(user);
    }
}
