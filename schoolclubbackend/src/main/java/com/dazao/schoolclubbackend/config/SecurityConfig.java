package com.dazao.schoolclubbackend.config;


import com.dazao.schoolclubbackend.entity.RestBean;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.SecurityFilterChain;

import java.io.IOException;

@Configuration
public class SecurityConfig {

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
        httpServletResponse.setStatus(403);
        httpServletResponse.getWriter().write(RestBean.failure(403,"未登录,清重新登录").asJsonString());
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
        httpServletResponse.getWriter().write(RestBean.success("登录成功").asJsonString());
    }


}
