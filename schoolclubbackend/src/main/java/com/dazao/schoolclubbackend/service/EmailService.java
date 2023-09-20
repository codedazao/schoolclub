package com.dazao.schoolclubbackend.service;

public interface EmailService {
    /**
     * @param type 是什么类型的邮件
     * @return
     */
    String registerEmailVerifyCode(String type,String email,String ip);

}
