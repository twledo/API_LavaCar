package dev.Lavacar.Nego.config;

import dev.Lavacar.Nego.security.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// Habilita o Spring Security e marca esta classe como uma configuração
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Define o codificador de senhas usando BCrypt, que é um algoritmo seguro para hashing de senhas
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Filtro JWT para validar tokens em requisições e o serviço de detalhes do usuário
    private final JwtAuthFilter jwtAuthFilter;
    private final UserDetailsService userDetailsService;

    // Construtor para injeção de dependências
    public SecurityConfig(JwtAuthFilter jwtAuthFilter, UserDetailsService userDetailsService) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.userDetailsService = userDetailsService;
    }

    // Configura a cadeia de filtros de segurança do Spring Security
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Desativa CSRF, já que a API usa tokens JWT (stateless) e não precisa de proteção CSRF
        http.csrf(csrf -> csrf.disable())
                // Configura a política de sessão como stateless (sem armazenamento de sessão no servidor)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Define regras de autorização para requisições HTTP
                .authorizeHttpRequests(auth -> auth
                        // Permite acesso público aos endpoints de autenticação (ex.: /auth/register, /auth/login)
                        .requestMatchers("/auth/**").permitAll()
                        // Exige autenticação para todos os outros endpoints
                        .anyRequest().authenticated())
                // Adiciona o filtro JWT antes do filtro padrão de autenticação por username/password
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        // Retorna a configuração construída
        return http.build();
    }

    // Configura o gerenciador de autenticação usado pelo Spring Security
    @Bean
    public AuthenticationManager authenticationManager() {
        // Cria um provedor de autenticação que usa o UserDetailsService e o BCrypt
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        // Retorna um gerenciador que usa o provedor configurado
        return new ProviderManager(authProvider);
    }
}