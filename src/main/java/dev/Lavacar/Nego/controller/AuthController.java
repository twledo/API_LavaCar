package dev.Lavacar.Nego.controller;

import dev.Lavacar.Nego.model.Users;
import dev.Lavacar.Nego.security.JwtUtil;
import dev.Lavacar.Nego.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

// Controlador REST para endpoints de autenticação
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    // Construtor para injeção de dependências
    public AuthController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    // Endpoint para registrar um novo usuário
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody Map<String, String> request) {
        // Log temporário para depuração (não recomendado em produção)
        System.out.println("Recebido registro: " + request);
        // Registra o usuário com nome de usuário e senha (criptografada no UserService)
        Users user = userService.registerUser(request.get("username"), request.get("password"));
        // Log temporário para depuração
        System.out.println("Usuário registrado: " + user.getUsername());
        // Retorna o usuário registrado com status 200 OK
        return ResponseEntity.ok(user);
    }

    // Endpoint para login de usuário
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> request) {
        // Busca o usuário pelo nome de usuário
        Optional<Users> user = Optional.ofNullable(userService.findByUsername(request.get("username")));
        // Verifica se o usuário existe e se a senha fornecida corresponde à senha criptografada
        if (user.isPresent() && passwordEncoder.matches(request.get("password"), user.get().getPassword())) {
            // Gera um token JWT para o usuário autenticado
            String token = JwtUtil.generateToken(user.get().getUsername());
            // Retorna o token em um mapa com status 200 OK
            return ResponseEntity.ok(Map.of("token", token));
        }
        // Retorna erro 401 Unauthorized se as credenciais forem inválidas
        return ResponseEntity.status(401).body("Credenciais inválidas");
    }
}