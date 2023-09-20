package com.dazao.schoolclubbackend.listener;

import jakarta.annotation.Resource;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RabbitListener(queues = "mail")
public class MailQueueListener {
    @Resource
    JavaMailSender javaMailSender;
    /**
     * 我的邮箱（邮件发送者，一般为自己公司的邮箱）
     */
    @Value("${spring.mail.username}")
    String mailUsername;

    @RabbitHandler
    public void sendMailMessage(Map<String,Object> data){
        String email = (String) data.get("email");
        Integer code = (Integer)data.get("code");
        String type = (String)data.get("type");
        SimpleMailMessage simpleMailMessage = switch (type) {
            case "register" -> createMessage("欢迎注册我们的网站",
                    "您的邮件注册验证码为:" + code + "有效时间为3分钟,为了保证您的安全，请勿将验证码泄漏给别人,如不是您本人操作，请忽略",
                    email);
            case "resetpassword" -> createMessage("您的密码重置邮件","您好，您正在进行密码重置操作，验证码为"+code+"如非本人操作，请忽略",
                    email);
            default -> null;
        };
        if (simpleMailMessage != null)
            javaMailSender.send(simpleMailMessage);
    }


    /**
     * 发送邮件的方法
     *
     * @param title 邮件标题
     * @param content 邮件内容
     * @param email 邮件收信人
     * @return 发送邮件的对象，只需调用这个对象的send方法即可
     */
    private SimpleMailMessage createMessage(String title,String content, String email){
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setSubject(title);
        simpleMailMessage.setText(content);
        simpleMailMessage.setTo(email);
        simpleMailMessage.setFrom(mailUsername);
        return simpleMailMessage;
    }
}
