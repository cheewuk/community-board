package com.example.communityboard.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/", "/posts/**", "/users/register", "/users/login", "/css/**", "/js/**", "/h2-console/**", "/error").permitAll() // 메인 페이지, 게시글 목록, 회원가입, 로그인 페이지 및 정적 리소스, 오류 페이지 접근 허용
                .anyRequest().authenticated() // 나머지 요청은 인증된 사용자만 접근 허용
            )
            .formLogin(form -> form
                .loginPage("/users/login") // 커스텀 로그인 페이지
                .defaultSuccessUrl("/", true) // 로그인 성공 시 리다이렉트될 기본 URL
                .failureUrl("/users/login?error") // 로그인 실패 시 리다이렉트될 URL
                .permitAll() // 로그인 페이지는 모두 접근 가능
            )
            .logout(logout -> logout
                .logoutUrl("/users/logout") // 로그아웃 URL
                .logoutSuccessUrl("/users/login?logout") // 로그아웃 성공 시 리다이렉트될 URL
                .permitAll()
            )
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/h2-console/**") // H2 콘솔 접근 시 CSRF 예외 처리
            )
            .headers(headers -> headers
                .frameOptions(frameOptions -> frameOptions.sameOrigin()) // H2 콘솔을 iframe으로 사용할 수 있도록 설정
            );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
