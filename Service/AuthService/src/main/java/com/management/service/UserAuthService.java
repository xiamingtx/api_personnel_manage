package com.management.service;

import com.management.utils.ResponseResult;
import com.management.entity.User;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author 夏明
 * @version 1.0
 */
@Service
public interface UserAuthService{

    ResponseResult<Map<String, String>> login(User user);

    ResponseResult logout();
}
