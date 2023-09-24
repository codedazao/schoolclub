package com.dazao.schoolclubbackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dazao.schoolclubbackend.entity.Account;
import com.dazao.schoolclubbackend.entity.vo.EmailRegisterVo;

public interface AccountService extends IService<Account> {
    /**
     * @param registerVo 注册用户的方法
     * @return 返回null表示成功，返回字符串表示注册没成功，会有错误信息
     */
    String registerEmailUser(EmailRegisterVo registerVo);

}
