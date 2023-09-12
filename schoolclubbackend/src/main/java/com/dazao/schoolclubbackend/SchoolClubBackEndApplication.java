package com.dazao.schoolclubbackend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.dazao.schoolclubbackend.mapper")
@SpringBootApplication
public class SchoolClubBackEndApplication {

    public static void main(String[] args) {
        SpringApplication.run(SchoolClubBackEndApplication.class, args);
    }

}
