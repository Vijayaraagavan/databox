package com.vijay.databox.config;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicLong;

import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.filters.CorsFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@SuppressWarnings("deprecation")
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // @Override
    // protected void configure(HttpSecurity http) throws Exception {
    // http
    // .authorizeRequests()
    // .anyRequest().permitAll()
    // .and()
    // .csrf().disable()
    // .headers().frameOptions().disable(); // Only needed if you're using an
    // embedded H2 database console, for example
    // }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // handle public, private routes
                .authorizeRequests(authorizeRequests -> {
                    authorizeRequests
                    // .antMatchers("/api/**").authenticated()
                    .anyRequest().permitAll();
                })
                // disable csrf (this allows postman api to use)
                .csrf(csrf -> csrf.disable()).headers(headers -> headers.frameOptions().disable())
                // if session stateful means cookies, stateless means jwt like tokens
                .sessionManagement(
                        sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // set response status code if unauthorized exception occurs
                .exceptionHandling(exceptionHandling -> {
                    exceptionHandling.authenticationEntryPoint((request, response, ex) -> {
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage());
                    });
                });
        return http.build();
    }

    CorsConfigurationSource corsFilter() {
        UrlBasedCorsConfigurationSource source =
            new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedHeaders(Arrays.asList("*"));
        config.setAllowedOrigins(Arrays.asList("*"));
        config.setAllowedMethods(Arrays.asList("*"));
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager getAuthenticationManagerBean() throws Exception {
       return authentication -> {
			System.out.println("authentication usr" + authentication);

        // return new UsernamePasswordAuthenticationToken(authentication, null);
        return authentication;
       };
    }

    @Bean
    AtomicLong getAtomicLong() {
        return new AtomicLong(System.currentTimeMillis());
    }
}
