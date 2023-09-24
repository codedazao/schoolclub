package com.dazao.schoolclubbackend.controller;

import com.dazao.schoolclubbackend.entity.RestBean;
import com.dazao.schoolclubbackend.entity.security.MyUserDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    final String a ="hello";
    @RequestMapping(a)
    public RestBean<String> hello(){
        MyUserDetails userInfo =(MyUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println(userInfo.getUsername());
        return RestBean.success("hello");
    }
}
