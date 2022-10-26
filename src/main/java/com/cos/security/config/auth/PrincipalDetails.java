package com.cos.security.config.auth;

import com.cos.security.model.UserModel;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

// 시큐리티가 /login 주소 요청이 오면 낚아채서 로그인을 진행 시킨다.
// 로그인 진행이 완료가 되면 시큐리티 세션을 만들어 준다(세션키가 Security ContextHolder)
// Authentication 타입 객체만 세션에 저장 가능.
// Authentication 안에 User정보가 있어야 함.
// User정보가 담긴 객체도 UserDetails 타입 객체여야 함

// Security Session => Authentication => UserDetailss
@Data
public class PrincipalDetails implements UserDetails, OAuth2User {

    private UserModel userModel;

    private Map<String, Object> attributes;

    // 일반로그인
    public PrincipalDetails(UserModel userModel) {
        this.userModel = userModel;
    }

    // OAuth로그인
    public PrincipalDetails(UserModel userModel, Map<String, Object> attributes) {
        this.userModel = userModel;
        this.attributes = attributes;
    }

    // 해당 유저의 권한을 리턴
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return userModel.getRole();
            }
        });

        return collect;
    }

    @Override
    public String getPassword() {
        return userModel.getPassword();
    }

    @Override
    public String getUsername() {
        return userModel.getUsername();
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

    @Override
    public String getName() {
        return null;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }
}
