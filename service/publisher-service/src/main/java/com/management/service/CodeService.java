package com.management.service;

import com.management.common.ResponseResult;

/**
 * @author 夏明
 * @version 1.0
 */
public interface CodeService {

    /**
     * 根据邮箱地址发送邮件
     * @author 夏明
     * @date 2022/8/25 12:01
     * @param mail
     * @return ResponseResult<Void>
     */
    ResponseResult<Void> sendMailCode(String mail);
}
