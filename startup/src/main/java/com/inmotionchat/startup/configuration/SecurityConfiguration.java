package com.inmotionchat.startup.configuration;

import com.inmotionchat.identity.security.AccessTokenFilter;
import com.inmotionchat.identity.security.Endpoint;
import com.inmotionchat.identity.security.InMotionSecurityProperties;
import com.inmotionchat.identity.security.SpringSecurityRoles;
import com.inmotionchat.identity.service.contract.TokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.security.SecureRandom;

@Configuration
public class SecurityConfiguration {

    private final TokenProvider tokenProvider;

    @Autowired
    public SecurityConfiguration(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Bean
    public AccessTokenFilter accessTokenFilter(TokenProvider tokenProvider) {
        return new AccessTokenFilter(tokenProvider);
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
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize -> {

                    for (Endpoint e : InMotionSecurityProperties.doNotAuthenticate()) {
                        authorize.requestMatchers(e.getMethod(), e.getPath()).permitAll();
                    }

                    authorize.anyRequest().hasAuthority(SpringSecurityRoles.ROLE_USER);

                })
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(accessTokenFilter(this.tokenProvider), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

}
