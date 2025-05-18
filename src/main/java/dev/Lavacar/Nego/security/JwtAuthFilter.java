package dev.Lavacar.Nego.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// Filtro que intercepta requisições HTTP para validar tokens JWT e autenticar usuários
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    // Serviço para carregar detalhes do usuário (implementado em UsersDetailsService)
    private final UserDetailsService userDetailsService;

    // Logger para registrar eventos e erros durante o processamento do filtro
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthFilter.class);

    // Construtor para injeção de dependências
    public JwtAuthFilter(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    // Método principal que processa cada requisição HTTP
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Loga a URI da requisição para depuração
        logger.info("Executando JwtAuthFilter para a requisição: " + request.getRequestURI());

        // Obtém o header Authorization da requisição
        String authHeader = request.getHeader("Authorization");

        // Verifica se o header existe e começa com "Bearer" (padrão para tokens JWT)
        if (authHeader == null || !authHeader.startsWith("Bearer")) {
            logger.info("Header Authorization ausente ou inválido.");
            // Se não houver token, continua a cadeia de filtros sem autenticar
            filterChain.doFilter(request, response);
            return;
        }

        // Extrai o token JWT removendo o prefixo "Bearer "
        String token = authHeader.substring(7);

        // Extrai o nome de usuário do token usando JwtUtil
        String username = JwtUtil.extractUsername(token);

        // Loga o username extraído para depuração
        logger.info("Token extraído. Username: " + username);

        // Verifica se o username foi extraído e se não há autenticação ativa no contexto
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Carrega os detalhes do usuário do banco usando o UserDetailsService
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // Valida o token usando JwtUtil
            if (JwtUtil.validateToken(token)) {
                // Cria um objeto de autenticação com os detalhes do usuário e suas permissões
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                // Define o usuário como autenticado no contexto do Spring Security
                SecurityContextHolder.getContext().setAuthentication(authToken);
                logger.info("Usuário autenticado via token JWT: " + username);
            } else {
                // Loga um aviso se o token for inválido
                logger.warn("Token JWT inválido para o usuário: " + username);
            }
        }

        // Continua a cadeia de filtros, passando a requisição para o próximo filtro ou controlador
        filterChain.doFilter(request, response);
    }
}