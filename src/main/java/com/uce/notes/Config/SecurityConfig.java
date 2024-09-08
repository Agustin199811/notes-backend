package com.uce.notes.Config;

import com.uce.notes.Repository.TokenRepository;
import com.uce.notes.Repository.UserRepository;
import com.uce.notes.Services.ServicesImp.CustomLogoutSuccessHandler;
import com.uce.notes.Config.Filters.JwtAuthenticationFilter;
import com.uce.notes.Config.Filters.JwtAuthorizationFilter;
import com.uce.notes.Services.ServicesImp.UserDetailServiceImp;
import com.uce.notes.Jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TokenRepository tokenRepository;

    @Autowired
    JwtAuthorizationFilter jwtAuthorizationFilter;

    @Autowired
    CustomLogoutSuccessHandler customLogoutSuccessHandler;

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtUtils, userRepository, tokenRepository);
        jwtAuthenticationFilter.setAuthenticationManager(authenticationManager);
        jwtAuthenticationFilter.setFilterProcessesUrl("/login");
        http.authorizeHttpRequests((requests) -> requests
                .requestMatchers("/hello", "/hi").hasRole("USER")
                .requestMatchers("/roles").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/users").permitAll()
                .requestMatchers(HttpMethod.POST, "/users/forgot-password").permitAll()
                .requestMatchers(HttpMethod.POST, "/users/reset-password").permitAll()
                .requestMatchers(HttpMethod.GET, "/users/validate-reset-token").permitAll()
                .requestMatchers(HttpMethod.GET, "/users").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE,"/users/delete").hasRole("USER")
                .requestMatchers(HttpMethod.GET, "/users/me").hasRole("USER")
                .requestMatchers("/notes/**").hasRole("USER")
                .anyRequest().denyAll());
        http.sessionManagement(session
                -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.headers(headers ->
                headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));
        http.csrf(AbstractHttpConfigurer::disable);
        http.addFilter(jwtAuthenticationFilter);
        http.addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);

        http.logout(logout -> logout
                        .logoutUrl("/logout") // Set the URL for logout
                        .logoutSuccessHandler(customLogoutSuccessHandler) // Set the custom logout success handler
                        .invalidateHttpSession(true) // Invalidate the session
                //.deleteCookies("JSESSIONID") // Optionally delete cookies
        );

        return http.build();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailServiceImp userDetailServiceImp) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailServiceImp);
        return provider;
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

