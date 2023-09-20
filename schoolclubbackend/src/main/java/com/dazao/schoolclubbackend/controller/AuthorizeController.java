package com.dazao.schoolclubbackend.controller;

import com.dazao.schoolclubbackend.entity.RestBean;
import com.dazao.schoolclubbackend.service.EmailService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/api/auth")
public class AuthorizeController {
    @Resource
    EmailService emailService;
    @GetMapping("/ask-code")
    public RestBean<String> askVerifyCode(@RequestParam String email, @RequestParam String type, HttpServletRequest request){
        String end = emailService.registerEmailVerifyCode(type, email, request.getRemoteAddr());
        if (end == null)
            return RestBean.success();
        else
            return RestBean.failure(400,end);
    }
}
