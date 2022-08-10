package com.management.service.impl;

import com.management.common.ResponseResult;
import com.management.service.CodeService;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author 夏明
 * @version 1.0
 */
@Service
public class CodeServiceImpl implements CodeService {

    @Resource
    StreamBridge bridge;  //通过bridge来发送消息

    @Override
    public ResponseResult<Void> sendMailCode(String mail) {
        //第一个参数其实就是RabbitMQ的交换机名称（数据会发送给这个交换机，到达哪个消息队列，不由我们决定）
        //这个交换机的命名稍微有一些规则:
        //输入:    <名称> + -in- + <index>
        //输出:    <名称> + -out- + <index>
        //这里我们使用输出的方式，来将数据发送到消息队列，注意这里的名称会和之后的消费者Bean名称进行对应

        bridge.send("code-out-0", mail);
        return new ResponseResult<>(2000, "发送成功");
    }
}
