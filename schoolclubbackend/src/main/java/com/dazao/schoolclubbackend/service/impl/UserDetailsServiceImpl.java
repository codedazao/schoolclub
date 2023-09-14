package com.dazao.schoolclubbackend.service.impl;

import com.dazao.schoolclubbackend.entity.Account;
import com.dazao.schoolclubbackend.entity.security.MyUserDetails;
import com.dazao.schoolclubbackend.service.AccountService;
import com.dazao.schoolclubbackend.service.MyUserDetailService;
import jakarta.annotation.Resource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class UserDetailsServiceImpl implements MyUserDetailService {
    @Resource
    AccountService accountService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountService
                .query()
                .eq("username", username)
                .or()
                .eq("email", username).one();

        if (account == null){
            throw new UsernameNotFoundException("用户名或密码错误");
        }
        MyUserDetails userDetails = new MyUserDetails();
        userDetails
                .setUsername(username)
                .setPassword(account.getPassword())
                .setUserId(account.getId())
                .setAuthorities(account.getRole());
        return userDetails;
    }
}
