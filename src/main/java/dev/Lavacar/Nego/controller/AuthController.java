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

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody Map<String, String> request) {
        System.out.println("Recebido registro: " + request);
        Users user = userService.registerUser(request.get("username"), request.get("password"));
        System.out.println("Usuário registrado: " + user.getUsername());
        return ResponseEntity.ok(user);
    }


    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> request) {
        Optional<Users> user = Optional.ofNullable(userService.findByUsername(request.get("username")));
        if (user.isPresent() && passwordEncoder.matches(request.get("password"), user.get().getPassword())) {
            String token = JwtUtil.generateToken(user.get().getUsername());
            return ResponseEntity.ok(Map.of("token", token));
        }
        return ResponseEntity.status(401).body("Credenciais inválidas");
    }

}
