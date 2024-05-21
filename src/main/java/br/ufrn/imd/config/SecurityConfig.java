package br.ufrn.imd.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.session.web.http.CookieHttpSessionIdResolver;
import org.springframework.session.web.http.HttpSessionIdResolver;

import br.ufrn.imd.service.UserService;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserService userService;

    public SecurityConfig(UserService userService) {
        this.userService = userService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationConfiguration authenticationConfiguration) throws Exception {
        AuthenticationManager authenticationManager = authenticationManager(authenticationConfiguration);
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManager, userService);

        http
            .csrf().disable()  // Disable CSRF for simplicity; enable it in production
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/api/users/login", "/login","/api/users/register" , "/public/**").permitAll() // Allow access to login endpoints
                .requestMatchers("/api/events/createEvent", "/api/events/{id}/update", "/api/events/{id}/delete",
                		"/api/events/{eventId}/finalize", "api/events/{eventId}/start", "/api/events/{eventId}/generatePairings",
                		"/api/events/{eventId}/finalizeRound").hasRole("MANAGER")
                .anyRequest().authenticated() // All other requests need to be authenticated
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
            )
            .addFilterBefore(customAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .logout(logout -> logout.permitAll());

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            System.out.println("Fetching user details for username: " + username);
            return userService.getUserByUsername(username)
                .map(user -> {
                    System.out.println("User found: " + user.getUsername());
                    List<SimpleGrantedAuthority> authorities = new ArrayList<>();
                    if (user.getRole().equals("ROLE_PLAYER")) {
                        authorities.add(new SimpleGrantedAuthority("ROLE_PLAYER"));
                    } else if (user.getRole().equals("ROLE_MANAGER")) {
                        authorities.add(new SimpleGrantedAuthority("ROLE_MANAGER"));
                    }
                    return new User(user.getUsername(), user.getPassword(), authorities);
                })
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new PlainTextPasswordEncoder();
    }

    @Bean
    public HttpSessionIdResolver httpSessionIdResolver() {
        return new CookieHttpSessionIdResolver();
    }
}

