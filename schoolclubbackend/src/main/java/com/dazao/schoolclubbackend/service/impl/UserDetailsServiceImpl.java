package com.dazao.schoolclubbackend.service.impl;

import com.dazao.schoolclubbackend.entity.security.MyUserDetails;
import com.dazao.schoolclubbackend.service.MyUserDetailService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class UserDetailsServiceImpl implements MyUserDetailService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MyUserDetails userDetails = new MyUserDetails();
        userDetails
                .setUsername("dazao")
                .setPassword("$2a$10$jMR2XFPDWKQe932SV38p1O5d0kS9cAVglpCJyDFc5Symgg7C/ytPW")
                .setUserId(1)
                .setAuthorities("user");
        return userDetails;
    }
}
