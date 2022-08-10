package com.management.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@Component
@Slf4j
public class ConsumerComponent {

    @Resource
    private StringRedisTemplate template;

    //JavaMailSender是专门用于发送邮件的对象，自动配置类已经提供了Bean
    @Resource
    private JavaMailSender sender;

    @Value("${spring.mail.username}")
    private String me;

    /**
     * 处理消息
     * @author 夏明
     * @date 2022/8/10 12:29
     * @return Consumer<String>
     */
    @Bean("code")   //注意这里需要填写我们前面交换机名称中"名称"，这样生产者发送的数据才会正确到达
    public Consumer<String> consumer() {
        return mail -> {
            // 构建redis键
            String redisKey = "register:" + mail;
            // 随机生成value
            String text = String.valueOf((int)((Math.random()*9+1)*100000));
            // 在redis中存放五分钟
            template.opsForValue().set(redisKey, text, 300, TimeUnit.SECONDS);
            // 发送value给邮箱
            //SimpleMailMessage是一个比较简易的邮件封装，支持设置一些比较简单内容
            SimpleMailMessage message = new SimpleMailMessage();
            //设置邮件标题
//        message.setSubject("【华北电力大学教务处】关于近期学校对您的处分决定");
            message.setSubject("【人力资源管理系统】注册码");
            //设置邮件内容
//        message.setText("XXX同学您好，经监控和教务巡查发现，您近期存在旷课、迟到、早退、上课刷抖音行为，" +
//                "现已通知相关辅导员，请手写5000字书面检讨，并在2022年4月1日17点前交到辅导员办公室。");
            message.setText("验证码: " + text);
            //设置邮件发送给谁，可以多个，这里就发给你的QQ邮箱
            message.setTo(mail);
            //邮件发送者，这里要与配置文件中的保持一致
            message.setFrom(me);
            //OK，万事俱备只欠发送
            sender.send(message);
            log.info("完成发送邮件到邮箱: " + mail);
        };
    }
}