package dev.Lavacar.Nego.service;

import dev.Lavacar.Nego.model.Users;
import dev.Lavacar.Nego.repository.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// Serviço que carrega detalhes do usuário para autenticação do Spring Security
@Service
public class UsersDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    // Construtor para injeção de dependências
    public UsersDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Carrega detalhes do usuário pelo nome de usuário
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Busca o usuário no banco de dados ou lança exceção se não encontrado
        Users user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
        // Constrói um objeto UserDetails com nome de usuário, senha e roles
        return User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles("USER") // Define a role padrão como "USER"
                .build();
    }
}