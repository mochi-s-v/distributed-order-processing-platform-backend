package com.vicky.cart_service.Config;

import com.vicky.cart_service.Filter.GatewayHeaderFilter;
import com.vicky.cart_service.Filter.GatewaySecretFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
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
                        .anyRequest().authenticated());
        httpSecurity.addFilterBefore(gatewaySecretFilter, UsernamePasswordAuthenticationFilter.class);
        httpSecurity.addFilterAfter(gatewayHeaderFilter, GatewaySecretFilter.class);
        return httpSecurity.build();
    }
}
