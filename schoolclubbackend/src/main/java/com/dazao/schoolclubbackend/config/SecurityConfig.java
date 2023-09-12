package com.dazao.schoolclubbackend.config;


import com.dazao.schoolclubbackend.entity.RestBean;
import com.dazao.schoolclubbackend.entity.security.MyUserDetails;
import com.dazao.schoolclubbackend.entity.vo.AuthorizeVo;
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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
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
        String headerToken = httpServletRequest.getHeader("Authorization");
        boolean invalidated = jwtUtil.invalidateJwt(headerToken);
        PrintWriter writer = httpServletResponse.getWriter();
        if (!invalidated){
            writer.write(RestBean.failure(400,"退出登录失败").asJsonString());
        }else {
            writer.write(RestBean.success("登出成功").asJsonString());
        }
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
        MyUserDetails user = (MyUserDetails) authentication.getPrincipal();
        PrintWriter writer = httpServletResponse.getWriter();
        String token = jwtUtil.createJwt(user, user.getUserId(), user.getUsername());
        AuthorizeVo authorizeVo = new AuthorizeVo()
                .setToken(token)
                .setRole(user.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority).toList().toString())
                .setUsername(user.getUsername())
                .setExpireTime(jwtUtil.expireTime());
        writer
                .write(RestBean.success(authorizeVo,"登录成功")
                .asJsonString());
        writer.close();
    }

}
