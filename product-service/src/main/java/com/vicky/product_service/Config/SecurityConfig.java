package com.vicky.product_service.Config;

import com.vicky.product_service.Filters.GatewayHeaderFilter;
import com.vicky.product_service.Filters.GatewaySecretFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final GatewayHeaderFilter gatewayHeaderFilter;
    private final GatewaySecretFilter gatewaySecretFilter;

    public SecurityConfig(GatewayHeaderFilter gatewayHeaderFilter,
                          GatewaySecretFilter gatewaySecretFilter) {
        this.gatewayHeaderFilter = gatewayHeaderFilter;
        this.gatewaySecretFilter = gatewaySecretFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/actuator/health").permitAll()
                        .requestMatchers("/actuator/prometheus").permitAll()
                        .requestMatchers("/api/internal/**").permitAll()
                        .requestMatchers("/error").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/product/**", "/api/category/**").permitAll()
                        .requestMatchers("/api/product/**", "/api/category/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                );
        httpSecurity.addFilterBefore(gatewaySecretFilter, UsernamePasswordAuthenticationFilter.class);
        httpSecurity.addFilterAfter(gatewayHeaderFilter, GatewaySecretFilter.class);
        return httpSecurity.build();
    }
}
