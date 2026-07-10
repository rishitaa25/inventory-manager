package org.example.inventory_manager_beta1.Configuration;

import org.example.inventory_manager_beta1.Configuration.SessionToken.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Central Spring Security configuration for the Inventory Manager project.
 *
 * <p>Responsibilities:
 * <ul>
 *   <li>Configures Argon2 password hashing used throughout the backend</li>
 *   <li>Registers {@link JwtAuthFilter} so every request is checked for a valid token</li>
 *   <li>Declares stateless session management — no {@code HttpSession} is created</li>
 *   <li>Whitelists public endpoints (login, signup) and keeps WebSocket and H2 open</li>
 *   <li>Provides a no-op {@link UserDetailsService} to prevent Spring Boot from
 *       autoconfiguring a second conflicting security filter chain</li>
 * </ul>
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration {

    private final JwtAuthFilter jwtAuthFilter ;

    /**
     * INIT: Injects the JWT authentication filter into the security configuration.
     * @param jwtAuthFilter
     *  The filter that validates tokens on every request
     */
    public SecurityConfiguration(JwtAuthFilter  jwtAuthFilter ) {this.jwtAuthFilter = jwtAuthFilter;}

    /**
     * INIT: Configures the Argon2 password encoder used for hashing and verifying
     * passwords throughout the application.
     * @return The configured Argon2 {@link PasswordEncoder}
     */
    @Bean
    public PasswordEncoder passwordEncoder() {return Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();}

    /**
     * INIT: Provides a no-op {@link UserDetailsService} bean to prevent Spring Boot
     * from autoconfiguring its own default security filter chain alongside this one.
     * <p>Without this bean, Spring Boot sees no {@code UserDetailsService} and
     * generates a random password on startup, then installs a second filter chain
     * that intercepts requests before {@link JwtAuthFilter} runs which causes all
     * authenticated requests to be rejected. This bean satisfies Spring's
     * requirement without doing anything, since authentication is handled
     * entirely via JWT sessions.
     * @return
     *  A {@link UserDetailsService} that always throws {@link UsernameNotFoundException}
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            throw new UsernameNotFoundException(
                    "UserDetailsService is not used — authentication is handled via JWT sessions.");
        };
    }

    /**
     * INIT: Defines a custom {@link AuthenticationEntryPoint} that intercepts requests
     * which reach a protected endpoint without valid authentication.
     *
     * <p>Without this, Spring Security silently returns a bare 403 with no body when
     * a request has no session or an invalid token. This entry point returns a proper
     * 401 UNAUTHORIZED with a plain text message instead.
     *
     * @return The custom {@link AuthenticationEntryPoint}
     */
    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authException) -> {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.TEXT_PLAIN_VALUE);
            response.getWriter().write("No valid session. Please login again.");
            response.getWriter().flush();
        };
    }

    /**
     * INIT: Configures the main Spring Security filter chain.
     * <p>Key decisions:
     * <ul>
     *   <li>CSRF disabled — not needed for stateless REST APIs</li>
     *   <li>Sessions are STATELESS — all auth state lives in the JWT and session DB row</li>
     *   <li>Login and signup endpoints are public — no token required</li>
     *   <li>WebSocket and H2 console endpoints are kept open for dev use</li>
     *   <li>All other requests require a valid JWT with an active session</li>
     *   <li>{@link JwtAuthFilter} runs before Spring's built-in auth filter</li>
     * </ul>
     * @param http
     *  The {@link HttpSecurity} object used to configure web-based security
     * @return
     *  The configured {@link SecurityFilterChain}
     * @throws Exception
     *  If an error occurs during security configuration
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)

                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/admin/login",
                                "/admin/signup",
                                "/employee/login",
                                "/employee/signup",
                                "/driver/login",
                                "/driver/signup"
                        ).permitAll()
                        // WebSocket handshakes must be permitted before auth is established
                        .requestMatchers(
                                "/shipment/**",
                                "/export/**",
                                "/admin-notifications/**",
                                "/employee-notifications/**"
                        ).permitAll()
                        .requestMatchers("/h2-console/**").permitAll()
                        .anyRequest().authenticated()
                )

                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(authenticationEntryPoint())
                )

                .headers(headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
                )

                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
