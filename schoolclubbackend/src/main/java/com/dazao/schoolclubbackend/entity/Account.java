package com.dazao.schoolclubbackend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@TableName("db_account")
@Data
@AllArgsConstructor
public class Account {
    @TableId(type = IdType.AUTO)
    int id;
    String username;
    String password;
    String email;
    String role;
    Date registerTime;
}
