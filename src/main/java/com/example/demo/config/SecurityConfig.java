package com.example.demo.config;

import com.example.demo.dto.ErrorResponse;
import com.example.demo.security.JwtAuthFilter;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.time.LocalDateTime;

@Configuration
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final ObjectMapper objectMapper;

    public SecurityConfig(
            JwtAuthFilter jwtAuthFilter,
            ObjectMapper objectMapper) {

        this.jwtAuthFilter = jwtAuthFilter;
        this.objectMapper = objectMapper;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
            .csrf(AbstractHttpConfigurer::disable)

            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            .formLogin(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)

            .headers(headers -> headers
                .frameOptions(frame -> frame.sameOrigin())
            )

            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    HttpMethod.POST,
                    "/api/auth/registro",
                    "/api/auth/login"
                ).permitAll()

                // Consola de H2 para las pruebas del laboratorio.
                .requestMatchers("/h2-console/**").permitAll()

                .requestMatchers(
                    HttpMethod.POST,
                    "/api/productos"
                ).hasRole("ADMIN")

                .requestMatchers("/api/**").authenticated()

                .anyRequest().denyAll()
            )

            .exceptionHandling(errors -> errors
                .authenticationEntryPoint((request, response, ex) -> {
                    response.setStatus(401);
                    response.setContentType(
                        MediaType.APPLICATION_JSON_VALUE
                    );
                    response.setCharacterEncoding("UTF-8");

                    ErrorResponse error = new ErrorResponse(
                        LocalDateTime.now(),
                        401,
                        "Unauthorized",
                        "Debes enviar un token válido para acceder",
                        request.getRequestURI(),
                        null
                    );

                    objectMapper.writeValue(
                        response.getWriter(),
                        error
                    );
                })

                .accessDeniedHandler((request, response, ex) -> {
                    response.setStatus(403);
                    response.setContentType(
                        MediaType.APPLICATION_JSON_VALUE
                    );
                    response.setCharacterEncoding("UTF-8");

                    ErrorResponse error = new ErrorResponse(
                        LocalDateTime.now(),
                        403,
                        "Forbidden",
                        "No tienes permisos para realizar esta operación",
                        request.getRequestURI(),
                        null
                    );

                    objectMapper.writeValue(
                        response.getWriter(),
                        error
                    );
                })
            )

            .addFilterBefore(
                jwtAuthFilter,
                UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }
}