package com.management.service;

import com.management.common.ResponseResult;

/**
 * @author 夏明
 * @version 1.0
 */
public interface CodeService {
    ResponseResult<Void> sendMailCode(String mail);
}
