package com.dazao.schoolclubbackend.entity.security;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Accessors(chain = true)
public class MyUserDetails implements UserDetails {
    @Getter
    @Setter
    int userId;
    @Setter
    String username;
    @Setter
    String password;

    @Setter
    Collection<? extends GrantedAuthority> authorities;

    public MyUserDetails setAuthorities(String... authorities){
        List<GrantedAuthority> authorityList = AuthorityUtils.createAuthorityList(authorities);
        this.authorities = new ArrayList<>(authorityList);
        return this;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
