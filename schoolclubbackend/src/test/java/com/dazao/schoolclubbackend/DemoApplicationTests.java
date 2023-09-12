package com.dazao.schoolclubbackend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Random;

@SpringBootTest
class DemoApplicationTests {

    @Test
    void contextLoads() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        //$2a$10$jMR2XFPDWKQe932SV38p1O5d0kS9cAVglpCJyDFc5Symgg7C/ytPW
        System.out.println(passwordEncoder.encode("123456"));
    }

    @Test
    void random(){
    }
}
