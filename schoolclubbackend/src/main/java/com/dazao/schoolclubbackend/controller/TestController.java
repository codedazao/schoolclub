package com.dazao.schoolclubbackend.controller;

import com.dazao.schoolclubbackend.entity.RestBean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @RequestMapping("/hello")
    public RestBean<String> hello(){
        return RestBean.success("hello");
    }
}
