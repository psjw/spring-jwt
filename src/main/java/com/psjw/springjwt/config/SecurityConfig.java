package com.psjw.springjwt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) //Session이 고정이라 csrf 방지해줘야함 -> STATELESS
                //form 로그인 방식 disable
                .formLogin(AbstractHttpConfigurer::disable)
                //http basic 인증방식 disable
                .httpBasic(AbstractHttpConfigurer::disable)
                //경로별 인가작업
                .authorizeHttpRequests((auth) -> {
                    auth.requestMatchers("/login", "/", "/join").permitAll()
                            .requestMatchers("/admin").hasRole("ADMIN")
                            .anyRequest().authenticated();
                })
                .sessionManagement(session -> {
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                });

        return http.build();
    }
}
