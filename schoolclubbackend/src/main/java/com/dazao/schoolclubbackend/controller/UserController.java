package com.dazao.schoolclubbackend.controller;


import com.dazao.schoolclubbackend.entity.RestBean;
import com.dazao.schoolclubbackend.entity.vo.EmailRegisterVo;
import com.dazao.schoolclubbackend.service.AccountService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.function.Supplier;

/**
 * 和用户相关的controller
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    AccountService accountService;
    /**
     * @return 注册用户的方法
     */
    @PostMapping("/register")
    public RestBean<Void> registerUser(@RequestBody EmailRegisterVo emailRegisterVo){
        return this.getMessage(()-> accountService.registerEmailUser(emailRegisterVo));
    }


    private RestBean<Void> getMessage(Supplier<String> supplierMessage){
        String message = supplierMessage.get();
        return message == null ? RestBean.success() : RestBean.failure(400,message);
    }
}
