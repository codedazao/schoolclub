package com.dazao.schoolclubbackend.service.impl;

import com.dazao.schoolclubbackend.service.EmailService;
import com.dazao.schoolclubbackend.utils.Const;
import com.dazao.schoolclubbackend.utils.FlowLimitUtils;
import jakarta.annotation.Resource;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class EmailServiceImpl implements EmailService {
    @Resource
    AmqpTemplate amqpTemplate;
    @Resource
    StringRedisTemplate stringRedisTemplate;
    @Resource
    FlowLimitUtils flowLimitUtils;

    @Value("${spring.mail.limitTime}")
    int emailLimitTime;

    /**
     * 用户给用户发送邮箱验证码的方法
     * @param type  是什么类型的邮件
     * @param email 用户邮箱
     * @param ip 用户限流的ip
     * @return
     */
    @Override
    public String registerEmailVerifyCode(String type, String email, String ip) {
        if (this.verifyLimit(ip)){
            Random random = new Random();
            int code = random.nextInt(899999) + 100000;
            Map<String,Object> data = Map.of("type",type,"code",code,"email",email);
            amqpTemplate.convertAndSend("email",data);
            stringRedisTemplate.opsForValue().set(
                    new StringBuilder().append(Const.VERIFY_EMAIL_DATA).append(email).toString(),
                    String.valueOf(code),5, TimeUnit.MINUTES
            );
            return null;
        }else {
            return "请求频繁，请稍后再试";
        }
    }

    private boolean verifyLimit(String ip){
        return flowLimitUtils.limitOnceCheck(ip,emailLimitTime);
    }
}
