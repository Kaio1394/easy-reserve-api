package br.com.easyreserve.common.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> {})
                // desabilita csrf para API REST
                .csrf(csrf -> csrf.disable())

                // não usa sessão (JWT é stateless)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // configura autorização por Role:
                // - OWNER: pode tudo (inclusive gerenciar usuários e workspaces)
                // - ADMIN: pode adicionar/editar/excluir Ambientes, Recursos de Ambiente e Chaves, mas NÃO Usuários
                // - COMMON: apenas visualização (GET)
                .authorizeHttpRequests(auth -> auth
                        // rotas públicas
                        .requestMatchers(
                                "/api/v1/auth/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**",
                                "/v1/public/**",
                                "/api/v1/public/**"
                        ).permitAll()

                        // Visualização pública dos módulos disponíveis (necessário na tela de cadastro do Workspace)
                        .requestMatchers(HttpMethod.GET,
                                "/api/v1/modules",
                                "/api/v1/modules/**",
                                "/api/v1/workspace-modules",
                                "/api/v1/workspace-modules/**"
                        ).permitAll()

                        // Edição do próprio perfil: permitido para COMMON, ADMIN e OWNER
                        .requestMatchers(HttpMethod.PUT,
                                "/api/v1/users/profile"
                        ).hasAnyRole("COMMON", "ADMIN", "OWNER")

                        // Gerenciamento de Usuários, Workspaces e Módulos (POST, PUT, DELETE): exclusivo OWNER
                        .requestMatchers(HttpMethod.POST,
                                "/api/v1/users/**",
                                "/api/v1/workspaces/**",
                                "/api/v1/modules/**",
                                "/api/v1/workspace-modules/**"
                        ).hasRole("OWNER")
                        .requestMatchers(HttpMethod.PUT,
                                "/api/v1/users/**",
                                "/api/v1/workspaces/**",
                                "/api/v1/modules/**",
                                "/api/v1/workspace-modules/**"
                        ).hasRole("OWNER")
                        .requestMatchers(HttpMethod.DELETE,
                                "/api/v1/users/**",
                                "/api/v1/workspaces/**",
                                "/api/v1/modules/**",
                                "/api/v1/workspace-modules/**"
                        ).hasRole("OWNER")

                        // Recomendação de Ambientes por Pesos: permitido para COMMON, ADMIN e OWNER
                        .requestMatchers(HttpMethod.POST,
                                "/api/v1/environments/recommend"
                        ).hasAnyRole("COMMON", "ADMIN", "OWNER")

                        // Gerenciamento de Ambientes, Recursos de Ambientes e Chaves (POST, PUT, DELETE): ADMIN ou OWNER
                        .requestMatchers(HttpMethod.POST,
                                "/api/v1/environments/**",
                                "/api/v1/environment-resources/**",
                                "/api/v1/keys/**"
                        ).hasAnyRole("ADMIN", "OWNER")
                        .requestMatchers(HttpMethod.PUT,
                                "/api/v1/environments/**",
                                "/api/v1/environment-resources/**",
                                "/api/v1/keys/**"
                        ).hasAnyRole("ADMIN", "OWNER")
                        .requestMatchers(HttpMethod.DELETE,
                                "/api/v1/environments/**",
                                "/api/v1/environment-resources/**",
                                "/api/v1/keys/**"
                        ).hasAnyRole("ADMIN", "OWNER")

                        // Reservas de Ambientes e Histórico de Reservas: permitido para COMMON, ADMIN e OWNER
                        .requestMatchers(
                                "/api/v1/reservations",
                                "/api/v1/reservations/**",
                                "/api/v1/reservation-histories",
                                "/api/v1/reservation-histories/**"
                        )
                        .hasAnyRole("COMMON", "ADMIN", "OWNER")

                        // Consultas (GET) e demais rotas autenticadas: COMMON, ADMIN e OWNER
                        .anyRequest().authenticated()
                )

                // adiciona filtro JWT antes do filtro padrão
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config
    ) throws Exception {
        return config.getAuthenticationManager();
    }
}
