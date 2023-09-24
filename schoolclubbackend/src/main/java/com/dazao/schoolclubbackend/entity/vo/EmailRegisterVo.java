package com.dazao.schoolclubbackend.entity.vo;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class EmailRegisterVo {
    @Email
    @NotNull
    String email;
    @Length(min = 6, max = 6)
    @NotNull
    String code;
    @Pattern(regexp = "^[a-zA-z0-9\\ue400]")
    @Length(min = 1, max = 10)
    @NotNull
    String username;
    @Length(min = 6,max = 10)
    @NotNull
    String password;
}
