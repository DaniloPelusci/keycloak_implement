package com.dan.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	    http
	        .authorizeHttpRequests(auth -> auth
	            .requestMatchers("/public/**").permitAll()
	            .requestMatchers("/admin/**").hasRole("admin")
	            .anyRequest().authenticated()
	        )
	        .oauth2ResourceServer(oauth -> oauth
	            .jwt(jwt -> jwt.jwtAuthenticationConverter(new JwtAuthConverter()))
	        );

	    return http.build();
	}

}
