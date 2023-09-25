package com.dazao.schoolclubbackend.controller;

import com.dazao.schoolclubbackend.entity.RestBean;
import com.dazao.schoolclubbackend.service.EmailService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import java.util.function.Supplier;

@Validated
@RestController
@RequestMapping("/api/auth")
public class AuthorizeController {
    @Resource
    EmailService emailService;

    /**
     * @param email 用户获取验证码的邮箱
     * @param type 邮箱验证码的类型
     * @param request 请求servlet
     * @return 返回restbean
     */
    @GetMapping("/ask-code")
    public RestBean<Void> askVerifyCode(@RequestParam @Email String email,
                                          @Pattern(regexp = "(register||reset)") @RequestParam String type,
                                          HttpServletRequest request){
        return getMessage(() -> emailService.registerEmailVerifyCode(type, email, request.getRemoteAddr()));
    }

    private RestBean<Void> getMessage(Supplier<String> supplierMessage){
        String message = supplierMessage.get();
        return message == null ? RestBean.success() : RestBean.failure(400,message);
    }
}
