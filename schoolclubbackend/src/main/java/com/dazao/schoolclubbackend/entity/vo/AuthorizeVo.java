package com.dazao.schoolclubbackend.entity.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class AuthorizeVo {
    Date expireTime;
    String role;
    String token;
    String username;

}
