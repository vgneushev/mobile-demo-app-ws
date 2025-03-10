package com.devdemo.app.ws.security;

import com.devdemo.app.ws.repository.UserRepository;
import com.devdemo.app.ws.shared.util.SpringDocUrl;
import lombok.NonNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class WebSecurity {
    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public WebSecurity(@NonNull final UserDetailsService userDetailsService,
                       @NonNull final BCryptPasswordEncoder passwordEncoder,
                       @NonNull final UserRepository userRepository) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Bean
    public SecurityFilterChain configure(@NonNull final HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder
                = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
        final AuthenticationManager authenticationManager = authenticationManagerBuilder.build();
        AuthenticationFilter filter = new AuthenticationFilter(authenticationManager);
        filter.setFilterProcessesUrl(SecurityConstants.LOGIN_URL);

        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // For local testing
                .csrf(AbstractHttpConfigurer::disable).authorizeHttpRequests(
                        (authz) -> authz
                                .requestMatchers(HttpMethod.POST, SecurityConstants.SIGN_UP_URL)
                                .permitAll()
                                .requestMatchers(HttpMethod.GET, SecurityConstants.VERIFICATION_EMAIL_URL)
                                .permitAll()
                                .requestMatchers(HttpMethod.POST, SecurityConstants.RESET_PASSWORD_REQUEST_URL)
                                .permitAll()
                                .requestMatchers(HttpMethod.POST, SecurityConstants.RESET_PASSWORD_URL)
                                .permitAll()
                                .requestMatchers(SpringDocUrl.API_V3.getUrl(), SpringDocUrl.CONFIG.getUrl(),
                                        SpringDocUrl.SWAGGER.getUrl(), SpringDocUrl.WEBJAR.getUrl())
                                .permitAll()
                                .requestMatchers(SecurityConstants.H2_CONSOLE_URL)
                                .permitAll()
                                .requestMatchers(HttpMethod.DELETE, SecurityConstants.USERS_URL)
                                .hasAuthority("DELETE_ AUTHORITY")
                                .anyRequest()
                                .authenticated())
                .authenticationManager(authenticationManager)
                .addFilter(filter)
                .addFilter(new AuthorizationFilter(authenticationManager, userRepository))
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.headers().frameOptions().disable();

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:8081"));
        config.setAllowedMethods(List.of("POST", "GET"));
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
