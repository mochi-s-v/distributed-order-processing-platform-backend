package com.vicky.user_service.config;

import com.vicky.user_service.Filters.GatewayHeaderFilter;
import com.vicky.user_service.Filters.GatewaySecretFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final GatewaySecretFilter gatewaySecretFilter;
    private final GatewayHeaderFilter gatewayHeaderFilter;

    public SecurityConfig(GatewaySecretFilter gatewaySecretFilter,
                          GatewayHeaderFilter gatewayHeaderFilter) {
        this.gatewayHeaderFilter = gatewayHeaderFilter;
        this.gatewaySecretFilter = gatewaySecretFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/api/user/register", "/api/user/login").permitAll()
                        .requestMatchers("/actuator/health").permitAll()
                        .requestMatchers("/actuator/prometheus").permitAll()
                        .anyRequest().authenticated()
                );
        httpSecurity.addFilterBefore(gatewaySecretFilter, UsernamePasswordAuthenticationFilter.class);
        httpSecurity.addFilterAfter(gatewayHeaderFilter, GatewaySecretFilter.class);
        return httpSecurity.build();
    }
}

