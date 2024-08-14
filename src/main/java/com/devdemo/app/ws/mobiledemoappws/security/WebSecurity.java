package com.devdemo.app.ws.mobiledemoappws.security;

import lombok.NonNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurity {

    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder passwordEncoder;

    public WebSecurity(@NonNull final UserDetailsService userDetailsService,
                       @NonNull final BCryptPasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder
                = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);

        http.csrf(AbstractHttpConfigurer::disable).authorizeHttpRequests(
                (authz) -> authz.requestMatchers(HttpMethod.POST, "/users")
                        .permitAll()
                        .anyRequest().authenticated());
        return http.build();
    }
}
