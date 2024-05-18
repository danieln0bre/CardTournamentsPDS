package br.ufrn.imd.config;

import br.ufrn.imd.repository.MongoPersistentTokenRepository;
import br.ufrn.imd.repository.PersistentLoginRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import br.ufrn.imd.model.User;
import br.ufrn.imd.repository.UserRepository;

import java.util.ArrayList;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final UserRepository userRepository;
    private final PersistentLoginRepository persistentLoginRepository;

    public SecurityConfig(UserRepository userRepository, PersistentLoginRepository persistentLoginRepository) {
        this.userRepository = userRepository;
        this.persistentLoginRepository = persistentLoginRepository;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable() // Desabilitar CSRF para facilitar o teste com Postman, mas isso deve ser habilitado em produção
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/public/**", "/api/users/register/**", "/api/users/login").permitAll()
                .anyRequest().authenticated()
            )
            .logout(logout -> logout.permitAll())
            .rememberMe(rememberMe -> rememberMe
                .tokenValiditySeconds(86400) // 1 dia
                .key("mySecretKey") // Chave secreta para hashing
                .userDetailsService(userDetailsService()) // Serviço para carregar os detalhes do usuário
                .tokenRepository(persistentTokenRepository()) // Repositório de tokens persistente
            );

        return http.build();
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        return new MongoPersistentTokenRepository(persistentLoginRepository);
    }
    
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            System.out.println("Fetching user by username: " + username);
            User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            System.out.println("User found: " + user.getUsername());
            return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), new ArrayList<>());
        };
    }
}
