package com.dazao.schoolclubbackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dazao.schoolclubbackend.entity.Account;
import com.dazao.schoolclubbackend.mapper.AccountMapper;
import com.dazao.schoolclubbackend.service.AccountService;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements AccountService {

}
