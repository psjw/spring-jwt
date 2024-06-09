package com.psjw.springjwt.config;

import com.psjw.springjwt.jwt.JWTFilter;
import com.psjw.springjwt.jwt.JWTUtil;
import com.psjw.springjwt.jwt.LoginFilter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.MessageDigestPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final AuthenticationConfiguration authenticationConfiguration;
    private final JWTUtil jwtUtil;

    //AuthenticationManager Bean 등록
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    public PasswordEncoder passwordEncoder(){
////        PasswordEncoderFactories.createDelegatingPasswordEncoder(); 참고
//        Map<String, PasswordEncoder> encoders = new HashMap();
//        String encodingId = "SHA-256";
//        encoders.put(encodingId, new MessageDigestPasswordEncoder(encodingId));
//        return new DelegatingPasswordEncoder(encodingId, encoders);
//    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http    //프론트에서 받은 자원을 다른 서버에 공유할 수 없음
                .cors(cors -> {
                    cors.configurationSource(request -> {
                        CorsConfiguration configuration = new CorsConfiguration();

                        configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000")); //허용할 앞단의 서버
                        configuration.setAllowedMethods(Collections.singletonList("*"));
                        configuration.setAllowCredentials(true);
                        configuration.setAllowedHeaders(Collections.singletonList("*"));
                        configuration.setMaxAge(3600L);

                        configuration.setExposedHeaders(Collections.singletonList("Authorization")); //사용자에게 보내줄헤더

                        return configuration;
                    });
                })
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
                //addFilterAt : 원하는 자리, addFilterBefore : 특정 필터 이전, , addFilterBefore : 특정 필터 이후
                .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration),jwtUtil), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JWTFilter(jwtUtil), LoginFilter.class)
                .sessionManagement(session -> {
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                });

        return http.build();
    }



}
