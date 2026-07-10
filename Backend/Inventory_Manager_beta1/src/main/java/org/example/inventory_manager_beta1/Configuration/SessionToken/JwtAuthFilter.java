package org.example.inventory_manager_beta1.Configuration.SessionToken;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.inventory_manager_beta1.Entities.Session;
import org.example.inventory_manager_beta1.Services.SessionService;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Runs once per request. Validates the JWT and then verifies the session
 * still exists in the database. If the user has logged out, the session
 * row is gone and this filter will not authenticate the request.
 */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final SessionService sessionService;

    /**
     * INIT: Constructs the filter with its required dependencies.
     * @param jwtUtil
     *  Used to validate tokens and extract the session ID
     * @param sessionService
     *  Used to look up whether the session is still active
     */
    public JwtAuthFilter(JwtUtil jwtUtil, SessionService sessionService) {
        this.jwtUtil = jwtUtil;
        this.sessionService = sessionService;
    }

    /**
     * FILTER: Core filter logic that validates the JWT and authenticates the request
     * if an active session is found for the token's session ID.
     * @param request
     *  The incoming HTTP request
     * @param response
     *  The HTTP response
     * @param filterChain
     *  The remaining filter chain to continue processing
     * @throws ServletException
     *  If a servlet error occurs
     * @throws IOException
     *  If an I/O error occurs
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);

            // Check to make sure JWT is valid (signature + expiry)
            if (jwtUtil.isTokenValid(token)) {
                String sessionId = jwtUtil.getSessionId(token);

                // Does the session still exist in the DB?
                Optional<Session> optionalSession = sessionService.findSession(sessionId);

                if (optionalSession.isPresent()) {
                    Session session = optionalSession.get();

                    // Build authority from the access level stored in the session row
                    SimpleGrantedAuthority authority = new SimpleGrantedAuthority(
                            "ROLE_" + session.getAccessLevel().name());

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    session.getUserName(),
                                    session.getEmployeeId(),
                                    List.of(authority)
                            );

                    SecurityContextHolder.getContext().setAuthentication(authentication);

                } else {
                    // Token is valid but session is gone — user has logged out.
                    // Write the 401 response here directly so Spring Security's
                    // default 403 handler never gets a chance to fire.
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    response.setContentType(MediaType.TEXT_PLAIN_VALUE);
                    response.getWriter().write("Session has expired or been logged out. Please login again.");
                    response.getWriter().flush();
                    return; // Stop the filter chain — don't continue processing
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}
