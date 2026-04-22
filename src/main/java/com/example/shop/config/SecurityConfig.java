package com.example.shop.config;

import com.example.shop.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.ignoringRequestMatchers(new AntPathRequestMatcher("/api/**")))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/",
                                "/members/login",
                                "/oauth2/**",
                                "/login/**",
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/error"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/members/login")        // 우리가 만든 로그인 페이지
                        .defaultSuccessUrl("/")             // 로그인 성공 시 이동할 곳
                        .usernameParameter("username")      // HTML의 input name과 일치 (우리는 email을 username으로 넘김)
                        .failureUrl("/members/login/error")

                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/members/login")
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService) // 우리가 만든 서비스 등록!
                        )
                        .defaultSuccessUrl("/")
                )
                .logout(logout -> logout
                        .logoutUrl("/members/logout")
                        .logoutSuccessUrl("/")
                );
        return http.build();
    }
}