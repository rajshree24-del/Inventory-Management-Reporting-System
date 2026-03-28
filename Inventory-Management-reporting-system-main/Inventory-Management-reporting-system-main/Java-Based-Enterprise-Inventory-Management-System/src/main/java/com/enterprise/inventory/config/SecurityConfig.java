
package com.enterprise.inventory.config;

import com.enterprise.inventory.security.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    // Password Encoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Authentication Provider
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    // Security Filter Chain
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http

                .authenticationProvider(authenticationProvider())

                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth

                        // PUBLIC PAGES
                        .requestMatchers(
                                "/", "/home", "/login",
                                "/css/**", "/js/**", "/images/**"
                        ).permitAll()

                        // ALLOW AI API (IMPORTANT)
                        .requestMatchers("/api/**").permitAll()

                        // DASHBOARD
                        .requestMatchers("/dashboard")
                        .hasAnyRole("ADMIN", "USER")

                        // PRODUCTS
                        .requestMatchers("/products/**")
                        .hasRole("ADMIN")

                        // STOCK LOGS
                        .requestMatchers("/logs")
                        .hasRole("ADMIN")

                        // LOW STOCK
                        .requestMatchers("/low-stock")
                        .hasAnyRole("ADMIN", "USER")

                        // OTHER REQUESTS
                        .anyRequest().authenticated()
                )

                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/dashboard", true)
                        .failureUrl("/login?error")
                        .permitAll()
                )

                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )

                .exceptionHandling(ex -> ex
                        .accessDeniedPage("/access-denied")
                );

        return http.build();
    }
}

