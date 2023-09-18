package com.ks.ninugo.configure.security;


import com.ks.ninugo.configure.security.filter.JwtAuthenticationFilter;
import com.ks.ninugo.configure.security.filter.JwtAuthorizationFilter;
import com.ks.ninugo.configure.security.provider.JwtTokenProvider;
import com.ks.ninugo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private AuthenticationManager manager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final PrincipalDetailsService principalDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable);
        http
                .addFilterBefore(
                        new JwtAuthenticationFilter(authenticationManager(http), userService),
                        JwtAuthorizationFilter.class
                )
                .addFilterAfter(
                        new JwtAuthorizationFilter(authenticationManager(http), userService, principalDetailsService),
                        JwtAuthenticationFilter.class)
                .httpBasic(withDefaults());;
        return http.build();
    }
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception{
        if (this.manager != null) {
            return this.manager;
        }

        AuthenticationManagerBuilder managerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        managerBuilder.authenticationProvider(jwtTokenProvider);

        this.manager = managerBuilder.build();
        return this.manager;
    }
}
