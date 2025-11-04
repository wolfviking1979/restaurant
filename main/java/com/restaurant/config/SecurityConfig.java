package com.restaurant.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.restaurant.security.CustomUserDetailsService;
import com.restaurant.security.JwtAuthenticationEntryPoint;
import com.restaurant.security.JwtAuthenticationFilter;
import com.restaurant.security.JwtTokenProvider;

import java.util.Arrays;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;
    private final JwtTokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService,
                          JwtTokenProvider tokenProvider,
                          JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint) {
        this.customUserDetailsService = customUserDetailsService;
        this.tokenProvider = tokenProvider;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Новый синтаксис
                .csrf(csrf -> csrf.disable())
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/menu/dishes/active").permitAll()
                        .requestMatchers("/api/menu/categories").permitAll()
                        .requestMatchers("/api/tables/active").permitAll()
                        .requestMatchers("/api/tables/available").permitAll()
                        .requestMatchers("/api/public/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()

                        // User management - только ADMIN
                        .requestMatchers("/api/users/**").hasRole("ADMIN")

                        // Menu management - ADMIN и MANAGER
                        .requestMatchers("/api/menu/dishes").hasAnyRole("ADMIN", "MANAGER")
                        .requestMatchers("/api/menu/categories").hasAnyRole("ADMIN", "MANAGER")

                        // Table management - ADMIN и MANAGER
                        .requestMatchers("/api/tables").hasAnyRole("ADMIN", "MANAGER")

                        // Reservations - MANAGER, WAITER
                        .requestMatchers("/api/reservations/**").hasAnyRole("ADMIN", "MANAGER", "WAITER")

                        // Orders - WAITER, MANAGER, ADMIN, CHEF
                        .requestMatchers("/api/orders/**").hasAnyRole("ADMIN", "MANAGER", "WAITER", "CHEF")

                        // Payments - WAITER, MANAGER
                        .requestMatchers("/api/payments/**").hasAnyRole("ADMIN", "MANAGER", "WAITER")

                        // Reports - ADMIN, MANAGER
                        .requestMatchers("/api/reports/**").hasAnyRole("ADMIN", "MANAGER")

                        // Inventory - STOREKEEPER, MANAGER, ADMIN, CHEF
                        .requestMatchers("/api/inventory/**").hasAnyRole("ADMIN", "MANAGER", "STOREKEEPER", "CHEF")

                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(tokenProvider, customUserDetailsService);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}