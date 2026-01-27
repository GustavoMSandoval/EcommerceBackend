package com.example.loginauthapi.infra.security;

// Importações básicas do Spring e Spring Security
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * CLASSE DE CONFIGURAÇÃO DE SEGURANÇA
 *
 * Esta classe é responsável por configurar toda a segurança da aplicação:
 * - Define quais rotas são públicas ou protegidas
 * - Configura autenticação baseada em JWT
 * - Remove uso de sessão (API Stateless)
 * - Define criptografia de senha
 *
 * Em DDD, isso pertence à camada de INFRAESTRUTURA,
 * pois segurança não é regra de negócio.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Serviço responsável por carregar o usuário do banco de dados.
     * O Spring Security usa isso durante o processo de autenticação.
     */
    @Autowired
    private CustomUserDetailsService userDetailsService;

    /**
     * Filtro de segurança customizado.
     * Normalmente é o filtro JWT, que:
     * - Intercepta todas as requisições
     * - Lê o token JWT
     * - Valida o token
     * - Autentica o usuário no contexto do Spring
     */
    @Autowired
    SecurityFilter securityFilter;

    /**
     * Define a cadeia de filtros de segurança da aplicação.
     * Aqui ficam as regras principais de acesso HTTP.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            /**
             * Desativa CSRF.
             * CSRF é usado com sessão/cookie.
             * Como a API usa JWT (stateless), pode ser desativado.
             */
            .csrf(csrf -> csrf.disable())

            /**
             * Habilita configuração padrão de CORS.
             * Necessário para permitir acesso do front-end (React, Angular, etc).
             */
            .cors(Customizer.withDefaults())

            /**
             * Define que a aplicação NÃO usa sessão.
             * Cada request deve trazer o JWT.
             */
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            /**
             * Define as regras de autorização das rotas.
             */
            .authorizeHttpRequests(authorize -> authorize

                // Rota pública para login
                .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()

                // Rota pública para cadastro de usuário
                .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()

                // Qualquer outra rota exige autenticação
                .anyRequest().authenticated()
            )

            /**
             * Adiciona o filtro JWT antes do filtro padrão de login do Spring.
             * Isso garante que o token seja validado antes de qualquer autenticação.
             */
            .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Bean responsável por criptografar senhas.
     * BCrypt é recomendado por ser seguro e lento contra ataques de força bruta.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * AuthenticationManager é o componente que executa o processo de autenticação.
     * Ele valida usuário e senha usando o UserDetailsService.
     */
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration
    ) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
