package com.management.service.impl;

import com.management.common.ResponseResult;
import com.management.dto.user.UserCreateRequest;
import com.management.dto.user.UserDto;
import com.management.dto.user.UserUpdateRequest;
import com.management.entity.Role;
import com.management.entity.User;
import com.management.exception.BizException;
import com.management.exception.ExceptionType;
import com.management.mapper.UserMapper;
import com.management.repository.UserRepository;
import com.management.service.UserAuthService;
import com.management.service.UserService;
import org.springframework.data.jpa.repository.Modifying;
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
    UserAuthServiceImpl userAuthService;

    /**
     * 查询用户列表
     * @author 夏明
     * @date 2022/8/8 14:28
     * @return List<UserDto>
     */
    @Override
    public List<UserDto> list() {
        return repository.findAll().stream().filter(item -> item.getIsDeleted() == 0)
                .map(mapper::toDto).collect(Collectors.toList());
    }

    /**
     * 根据id查询用户
     * @author 夏明
     * @date 2022/8/8 14:28
     * @param id
     * @return UserDto
     */
    @Override
    public UserDto get(String id) {
        return mapper.toDto(getUserById(id));
    }

    /**
     * 添加用户
     * @author 夏明
     * @date 2022/8/8 14:43
     * @param userCreateRequest
     */
    @Override
    @Transactional
    @Modifying
    public ResponseResult<Void> create(UserCreateRequest userCreateRequest) {
        // 查询用户是否已存在
        String username = userCreateRequest.getUsername();
        if (repository.findUserByUsername(username) != null) {
            // 抛出用户已存在异常
            throw new BizException(ExceptionType.USER_NAME_DUPLICATE);
        }
        User user = mapper.createEntity(userCreateRequest);
        userAuthService.addUser(user);
        return new ResponseResult<>(2000, "添加成功!");
    }

    /**
     * 修改用户
     * @author 夏明
     * @date 2022/8/8 18:16
     * @param id
     * @param userUpdateRequest
     * @return ResponseResult<Void>
     */
    @Override
    @Transactional
    @Modifying
    public ResponseResult<Void> update(String id, UserUpdateRequest userUpdateRequest) {
        User user = mapper.updateEntity(getUserById(id),userUpdateRequest);
        repository.save(user);
        return new ResponseResult<>(2000, "修改成功");
    }

    private User getUserById(String id) {
        Optional<User> userOptional = repository.findById(id);
        if  (!userOptional.isPresent() || userOptional.get().getIsDeleted() == 1) {
            throw new BizException(ExceptionType.USER_NOT_FOUND);
        }
        return userOptional.get();
    }

    /**
     * 删除用户(逻辑删除)
     * @author 夏明
     * @date 2022/8/8 19:59
     * @param id
     * @return ResponseResult<Void>
     */
    @Override
    @Transactional
    @Modifying
    public ResponseResult<Void> delete(String id) {
        User user = getUserById(id);
        // 设置逻辑删除字段为1
        user.setIsDeleted(1);
        repository.save(user);
        return new ResponseResult<>(2000, "删除成功");
    }
}
