package com.cos.security.config;

import com.cos.security.config.oauth.PrincipalOauth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


// 1. 코드받기,
// 2. 액세스토큰(권한),
// 3. 프로필 정보 취득,
// 4-1. 정보를 토대로 회원 가입 시킴
// 4-2. 추가정보 받아서 가입 시킴

@Configuration
@EnableWebSecurity  // 스프링 시큐리티 필터(현재 클래스가 필터임)가 스프링 필터체인에 등록이 됩니다.
// secured 어노테이션 활성화, preAuthorize, postAuthorize 어노테이션 활성화
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private PrincipalOauth2UserService principalOauth2UserService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.cors().disable();
        http.headers().frameOptions().disable();
        http.authorizeRequests()
                .antMatchers("/user/**").authenticated()    // 인증만 되면 들어갈 수 있음
                .antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole ('ROLE_MANAGER')")
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
                .anyRequest().permitAll()
                .and()
                .formLogin()
                .loginPage("/loginForm")
                .loginProcessingUrl("/login") // /login주소가 호출되면 시큐리티가 낚아채서 대신 로그인을 진행해준다.
                .defaultSuccessUrl("/") // 로그인 성고하면 /로 이동
                .and()
                .oauth2Login()
                .loginPage("/loginForm") // fb로그인이 완료된 후 후처리 필요
                .userInfoEndpoint()
                .userService(principalOauth2UserService);

        return http.build();
    }

}
