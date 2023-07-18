package com.inmotionchat.startup.configuration;

import com.inmotionchat.core.security.AccessTokenFilter;
import com.inmotionchat.core.security.Endpoint;
import com.inmotionchat.core.security.InMotionSecurityProperties;
import com.inmotionchat.core.security.SpringSecurityRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.security.SecureRandom;

@Configuration
public class SecurityConfiguration {

    private AccessTokenFilter accessTokenFilter;

    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") // Module loaded at runtime from core regardless of config
    public SecurityConfiguration(AccessTokenFilter accessTokenFilter) {
        this.accessTokenFilter = accessTokenFilter;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            final AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10, new SecureRandom());
    }

    @Bean
    public SecurityFilterChain configure(final HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .cors().and()
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize -> {

                    for (Endpoint e : InMotionSecurityProperties.doNotAuthenticate()) {
                        authorize.requestMatchers(e.getMethod(), e.getPath()).permitAll();
                    }

                    authorize.anyRequest().hasAuthority(SpringSecurityRoles.ROLE_USER);

                })
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(accessTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return source;
    }

}
