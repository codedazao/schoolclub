package com.dazao.schoolclubbackend.config;


import com.dazao.schoolclubbackend.entity.RestBean;
import com.dazao.schoolclubbackend.filter.JwtFilter;
import com.dazao.schoolclubbackend.utils.JwtUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.io.PrintWriter;

@Configuration
public class SecurityConfig {
    @Resource
    JwtUtil jwtUtil;
    @Resource
    JwtFilter jwtFilter;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.authorizeHttpRequests(config-> config
                .requestMatchers("/api/auth/login")
                .permitAll()
                .anyRequest()
                .authenticated())
                .formLogin(config -> config.loginProcessingUrl("/api/auth/login")
                .successHandler(this::successLoginHandler)
                .failureHandler(this::failureLoginHandler))
                .logout(config->config.logoutUrl("/api/logout").logoutSuccessHandler(this::logoutSuccessHandler))
                .exceptionHandling(config->config
                        .authenticationEntryPoint(this::accessDeniedHandler))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    private void logoutSuccessHandler(HttpServletRequest httpServletRequest,
                                      HttpServletResponse httpServletResponse,
                                      Authentication authentication) throws IOException {
        httpServletResponse.setContentType("application/json;charset=utf-8");
        httpServletResponse.getWriter().write(RestBean.success("登出成功").asJsonString());

    }

    private void accessDeniedHandler(HttpServletRequest httpServletRequest,
                                     HttpServletResponse httpServletResponse,
                                     AuthenticationException e) throws IOException {
        httpServletResponse.setContentType("application/json;charset=utf-8");
        httpServletResponse.getWriter().write(RestBean.failure(401,"未登录,清重新登录").asJsonString());
    }


    private void failureLoginHandler(HttpServletRequest httpServletRequest,
                                     HttpServletResponse httpServletResponse,
                                     AuthenticationException e) throws IOException {
        httpServletResponse.setContentType("application/json;charset=utf-8");
        httpServletResponse.getWriter().write(RestBean.failure(401,"登录失败").asJsonString());
    }

    private void successLoginHandler(HttpServletRequest httpServletRequest,
                                     HttpServletResponse httpServletResponse,
                                     Authentication authentication) throws IOException {
        httpServletResponse.setContentType("application/json;charset=utf-8");
        User user = (User) authentication.getPrincipal();
        PrintWriter writer = httpServletResponse.getWriter();
        writer
                .write(RestBean.success(jwtUtil.createJwt(user, 1, "小明"),"登录成功")
                .asJsonString());
        writer.close();
    }


}
