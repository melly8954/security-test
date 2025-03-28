package com.tl.securitytest.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf((auth) -> auth.disable());        // 배포 환경 시 csrf 공격 방지를 위해 csrf disable() 설정을 제거하고 추가적인 설정을 진행해야 한다.
        http.
                authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/","/login","/join","/joinProc").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/my/**").hasAnyRole("ADMIN", "USER")
                        .anyRequest().authenticated());     // authenticated() 는 로그인만 진행하면 모두 접근할 수 있는 메서드      cf). denyAll() 존재
        http.
                formLogin((auth) -> auth
                        .loginPage("/login")
                        .loginProcessingUrl("/loginProc")
                        .permitAll());
        http
                .sessionManagement((auth) -> auth
                        .maximumSessions(1)                     // 하나의 아이디에 대한 다중 로그인 허용 개수
                        .maxSessionsPreventsLogin(true));       // 다중 로그인 개수를 초과하였을 경우 처리 방법, true : 초과시 새로운 로그인 차단 / false : 초과시 기존 세션 하나 삭제
        http
                .sessionManagement((auth) -> auth
                        .sessionFixation().changeSessionId());  // 세션 고정 공격을 보호하기 위한 로그인 성공시 세션 설정 방법, 로그인 시 동일한 세션에 대한 id 변경
        
        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {

        return new BCryptPasswordEncoder();
    }
}
