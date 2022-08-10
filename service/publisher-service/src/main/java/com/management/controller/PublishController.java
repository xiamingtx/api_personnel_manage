package com.management.controller;

import com.management.common.ResponseResult;
import com.management.service.impl.CodeServiceImpl;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/publish")
public class PublishController {

    @Resource
    CodeServiceImpl codeService;

    @RequestMapping("/mail_code")
    public ResponseResult<Void> sendMailCode(@RequestParam String mail){
        return codeService.sendMailCode(mail);
    }
}