package bf.gov.mtdpce.config;

import bf.gov.mtdpce.security.AuthEntryPointJwt;
import bf.gov.mtdpce.security.AuthTokenFilter;
import bf.gov.mtdpce.security.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.security.web.header.writers.StaticHeadersWriter;
import org.springframework.web.cors.CorsConfigurationSource;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Autowired
    private CorsConfigurationSource corsConfigurationSource;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Hiérarchie des rôles : un rôle supérieur hérite des autorisations des rôles inférieurs.
    // Indispensable pour que ROLE_SUPER_ADMIN puisse accéder aux endpoints @PreAuthorize("hasRole('ADMIN')") etc.
    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl hierarchy = new RoleHierarchyImpl();
        hierarchy.setHierarchy(
                "ROLE_SUPER_ADMIN > ROLE_ADMIN\n" +
                "ROLE_ADMIN > ROLE_MODERATOR\n" +
                "ROLE_MODERATOR > ROLE_USER"
        );
        return hierarchy;
    }

    // Branche la hiérarchie de rôles sur la sécurité au niveau des méthodes (@PreAuthorize).
    @Bean
    static MethodSecurityExpressionHandler methodSecurityExpressionHandler(RoleHierarchy roleHierarchy) {
        DefaultMethodSecurityExpressionHandler handler = new DefaultMethodSecurityExpressionHandler();
        handler.setRoleHierarchy(roleHierarchy);
        return handler;
    }

    // WebSecurityConfig.java — activer cors() avec le bean CorsConfigurationSource
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .csrf(csrf -> csrf.disable())
                // En-têtes de sécurité HTTP (anti-clickjacking, HSTS, referrer, permissions).
                .headers(headers -> headers
                        // Empêche l'inclusion du site dans une iframe (clickjacking).
                        .frameOptions(frame -> frame.deny())
                        .contentSecurityPolicy(csp -> csp.policyDirectives(
                                "frame-ancestors 'none'; object-src 'none'; base-uri 'self'"))
                        // Force HTTPS pendant 1 an (appliqué uniquement sur les réponses HTTPS).
                        .httpStrictTransportSecurity(hsts -> hsts
                                .includeSubDomains(true)
                                .maxAgeInSeconds(31536000))
                        .referrerPolicy(ref -> ref.policy(
                                ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN))
                        // Désactive les API navigateur sensibles par défaut.
                        .addHeaderWriter(new StaticHeadersWriter("Permissions-Policy",
                                "geolocation=(), microphone=(), camera=(), payment=(), usb=(), interest-cohort=()"))
                )
                .exceptionHandling(ex -> ex.authenticationEntryPoint(unauthorizedHandler))
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // ✅ Ajout des endpoints de documentation (Indispensable pour Swagger/OpenAPI)
                        .requestMatchers(
                                "/v3/api-docs/**",    // JSON de la doc v3
                                "/api-docs/**",       // Chemin alternatif souvent utilisé (ton erreur vient de là)
                                "/swagger-ui/**",     // Ressources de l'interface
                                "/swagger-ui.html",   // Page d'accueil Swagger
                                "/swagger-resources/**",
                                "/webjars/**"
                        ).permitAll()

                        // ✅ Tes endpoints publics existants
                        .requestMatchers(
                                "/api/v1/auth/**",
                                "/api/auth/**",
                                "/api/v1/public/**",
                                "/api/v1/articles/published/**",
                                "/api/v1/agendas/published/**",
                                "/api/v1/ministeres/**",
                                "/api/v1/ministres/**",
                                "/api/v1/missions/**",
                                "/api/v1/articles/**",
                                "/api/v1/structures/**",
                                "/api/v1/domaines/**",
                                "/api/v1/structures-rattachees/**",
                                "/api/v1/types/**",
                                "/api/v1/services/**",
                                "/api/v1/themes/**",
                                "/api/v1/statistiques/**",
                                "/api/v1/events/public/**",
                                "/api/v1/faqs/public/**",
                                "/api/v1/services/public/**",
                                "/api/v1/documents/public/**",
                                "/api/v1/flash-infos/public/**",
                                "/api/v1/banners/public/**",
                                "/api/v1/conseils/public/**",
                                "/api/v1/ticker-config/public/**",
                                "/api/v1/newsletter/public/**",
                                "/api/v1/analytics/track",
                                "/api/v1/health",
                                "/uploads/**",
                                "/sitemap.xml"
                        ).permitAll()

                        // Tout le reste nécessite une authentification
                        .anyRequest().authenticated()
                );

        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        // Anti-spam sur les formulaires publics (contact, newsletter).
        http.addFilterBefore(new bf.gov.mtdpce.security.RateLimitingFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Masque l'en-tête HTTP « Server » (n'expose plus la version de Tomcat/Coyote).
     */
    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> hideServerHeaderCustomizer() {
        return factory -> factory.addConnectorCustomizers(
                connector -> connector.setProperty("server", " "));
    }

    @Bean
    public HttpFirewall httpFirewall() {
        StrictHttpFirewall firewall = new StrictHttpFirewall();

        firewall.setAllowUrlEncodedLineFeed(true);        // %0A
        firewall.setAllowUrlEncodedCarriageReturn(true);  // %0D

        firewall.setAllowBackSlash(true);
        firewall.setAllowUrlEncodedSlash(true);
        firewall.setAllowSemicolon(true);

        return firewall;
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(HttpFirewall firewall) {
        return web -> web.httpFirewall(firewall);
    }

}
