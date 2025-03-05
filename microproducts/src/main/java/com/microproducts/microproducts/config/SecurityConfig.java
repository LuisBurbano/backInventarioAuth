package com.microproducts.microproducts.config;

import com.microproducts.microproducts.filters.JwtTokenFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${jwt.secret}")
    private String secretKey;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Habilitar CORS y deshabilitar CSRF
                .cors(cors -> cors.disable())
                .csrf(csrf -> csrf.disable())

                // Configurar manejo de sesiones sin estado
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // Configurar autorización de endpoints
                .authorizeHttpRequests(authorize -> authorize
                        // Endpoints públicos
                        .requestMatchers("/api/products").hasRole("STORE") // Permitir acceso público temporalmente
                        .requestMatchers("/api/products/**").hasRole("STORE") // Solo usuarios con rol STORE pueden acceder
                        .anyRequest().authenticated()
                )

                // Agregar el filtro JWT antes del filtro de autenticación
                .addFilterBefore(new JwtTokenFilter(secretKey), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}