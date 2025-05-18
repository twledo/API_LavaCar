package dev.Lavacar.Nego.security;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

// Classe utilitária para gerenciar tokens JWT (geração, validação e extração de informações)
public class JwtUtil {

    // Chave secreta para assinar tokens JWT, gerada usando o algoritmo HS256 (HMAC SHA-256)
    // Essa chave é usada para garantir a integridade e autenticidade do token
    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // Tempo de expiração do token em milissegundos (86400000 ms = 24 horas)
    private static final long EXPIRATION_TIME = 86400000;

    // Gera um token JWT para um usuário com base no nome de usuário
    public static String generateToken(String username) {
        // Constrói o token JWT com:
        // - Subject: o nome de usuário (identificador do usuário)
        // - Expiration: data atual + tempo de expiração (24 horas)
        // - Signature: assinado com a chave secreta para garantir autenticidade
        return Jwts.builder()
                .setSubject(username) // Define o "subject" do token como o username
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // Define a expiração
                .signWith(key) // Assina o token com a chave secreta
                .compact(); // Compacta o token em uma string
    }

    // Extrai o nome de usuário (subject) de um token JWT
    public static String extractUsername(String token) {
        // Faz o parse do token usando a chave secreta e retorna o "subject" (username)
        return Jwts.parserBuilder()
                .setSigningKey(key) // Define a chave para validar a assinatura
                .build()
                .parseClaimsJws(token) // Faz o parse do token
                .getBody()
                .getSubject(); // Obtém o subject (username)
    }

    // Valida um token JWT, verificando sua assinatura e se não está expirado
    public static boolean validateToken(String token) {
        try {
            // Tenta fazer o parse do token com a chave secreta
            Jwts.parserBuilder()
                    .setSigningKey(key) // Define a chave para validação
                    .build()
                    .parseClaimsJws(token); // Verifica a assinatura e expiração
            return true; // Token válido
        } catch (JwtException e) {
            // Captura exceções como token expirado, assinatura inválida ou malformado
            return false; // Token inválido
        }
    }
}