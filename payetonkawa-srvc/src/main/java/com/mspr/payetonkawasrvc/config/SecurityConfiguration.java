package com.mspr.payetonkawasrvc.config;

import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.Collections;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
   public final AuthenticationProvider authenticationProvider;
   public final  JwtAuthenticationFilter jwtAuthFilter;
   private final LogoutHandler logoutHandler;


   @Bean
   public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

      http
              .csrf()
              .disable()
              .authorizeHttpRequests()
              .requestMatchers(
                      "/api/v1/auth/**"
              )
              .permitAll()
              .anyRequest()
              .authenticated()
              .and()
              .sessionManagement()
              .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
              .and()
              .authenticationProvider(authenticationProvider)
              .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
              .logout()
              .logoutUrl("/api/v1/auth/logout")
              .addLogoutHandler(logoutHandler)
              .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext());

      return http.build();
   }
}
