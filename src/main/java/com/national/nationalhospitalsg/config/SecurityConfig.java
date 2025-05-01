package com.national.nationalhospitalsg.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers(
                                "/patients.xhtml",
                                "/h2-console/**",  // Disable CSRF for H2 console
                                "/jakarta.faces.resource/**"
                        )
                )
                .headers(headers -> headers
                        .frameOptions(frameOptions -> frameOptions.sameOrigin()
                                // Allow frames from same origin (needed for H2 console)
                        )

                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/jakarta.faces.resource/**",
                                "/resources/**",
                                "/error",
                                "/login",
                                "/logout"
                        ).permitAll()
                        .requestMatchers("/patients.xhtml").authenticated()
                        .requestMatchers("/h2-console/**").authenticated()

                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .defaultSuccessUrl("/patients.xhtml", true)
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );

        return http.build();
    }

}
