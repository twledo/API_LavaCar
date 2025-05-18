package dev.Lavacar.Nego.service;

import dev.Lavacar.Nego.model.Users;
import dev.Lavacar.Nego.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

// Serviço para operações relacionadas a usuários, incluindo registro
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // Construtor para injeção de dependências
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Registra um novo usuário com senha criptografada
    public Users registerUser(String username, String password) {
        // Criptografa a senha usando BCrypt
        String passwordEncrypted = passwordEncoder.encode(password);
        // Cria uma nova entidade Users com a senha criptografada
        Users user = new Users(username, passwordEncrypted);
        // Salva o usuário no banco de dados
        return userRepository.save(user);
    }

    // Busca um usuário pelo nome de usuário
    public Users findByUsername(String username) {
        // Retorna o usuário encontrado ou null se não existir
        return userRepository.findByUsername(username).orElse(null);
    }
}