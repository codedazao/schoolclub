package com.dazao.schoolclubbackend.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dazao.schoolclubbackend.entity.Account;
import com.dazao.schoolclubbackend.entity.vo.EmailRegisterVo;
import com.dazao.schoolclubbackend.mapper.AccountMapper;
import com.dazao.schoolclubbackend.service.AccountService;
import com.dazao.schoolclubbackend.utils.Const;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements AccountService {
    @Resource
    StringRedisTemplate stringRedisTemplate;
    @Resource
    PasswordEncoder passwordEncoder;
    @Override
    public String registerEmailUser(EmailRegisterVo registerVo) {
        //首先把东西都拿出来
        String email = registerVo.getEmail();
        String inputCode = registerVo.getCode();
        String username = registerVo.getUsername();
        //验证邮箱是否有验证码
        String codeKey = Const.VERIFY_EMAIL_DATA +
                email;
        String code = stringRedisTemplate.opsForValue().get(codeKey);
        if (code == null) return "请先获取验证码";
        //检查验证码是否正确
        if (code.equals(inputCode)) return "验证码错误，请重新输入";
        //检查邮箱是否有被注册过
        if (this.isRegister(email,username)) return "用户名或邮箱已被注册过，请更换";
        String passwordEncode = passwordEncoder.encode(registerVo.getPassword());
        Account account=new Account(null,username,passwordEncode,email,null,new Date());
        if (this.save(account)){
            //注册成功删掉key
            stringRedisTemplate.delete(codeKey);
            return null;
        }else {
            return "内部错误，请联系管理员";
        }
    }

    /**
     * @param email 检查邮箱或者用户名是否被注册过
     * @return 返回true是被注册过，返回false是没有
     */
    private boolean isRegister(String email,String username){
        return this.baseMapper.exists(Wrappers.<Account>query().eq("email",email).or().eq("username",username));
    }
}
