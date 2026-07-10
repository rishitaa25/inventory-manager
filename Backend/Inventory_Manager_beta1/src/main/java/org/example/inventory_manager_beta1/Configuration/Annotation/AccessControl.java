package org.example.inventory_manager_beta1.Configuration.Annotation;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.example.inventory_manager_beta1.DataEnums.AccessLevel;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.example.inventory_manager_beta1.ExceptionHandling.Exceptions.*;

/**
 * AOP Aspect that enforces method-level access control throughout the application.
 * Intercepts any method annotated with {@link RequiresAccess} and verifies that
 * the currently authenticated caller meets the required {@link AccessLevel}.
 * <p>How it works:
 * <ol>
 *   <li>{@code JwtAuthFilter} validates the incoming token and places an
 *       {@code Authentication} object in the {@code SecurityContextHolder}</li>
 *   <li>This aspect reads the role from that {@code Authentication} object</li>
 *   <li>It compares the caller's {@code AccessLevel} ordinal against the required
 *       level's ordinal — higher ordinal means higher access</li>
 *   <li>If no valid session exists, an {@link UnauthorizedException} is thrown</li>
 *   <li>If the caller's level is too low, an {@link InsufficientAccessLevelException}
 *       is thrown before the method body runs</li>
 * </ol>
 * <p>AccessLevel hierarchy (lowest to highest):
 * {@code EMPLOYEE(0) < DRIVER(1) < ADMIN(2) < GENERAL_MANAGER(3)}
 */
@Aspect
@Component
public class AccessControl {

    /**
     * Intercepts any method annotated with {@link RequiresAccess} and checks
     * whether the authenticated caller has a sufficient access level.
     * @param requiredAccess
     *  The annotation instance carrying the required {@link AccessLevel}
     * @throws UnauthorizedException
     *  If no valid session exists in the SecurityContext
     * @throws InsufficientAccessLevelException
     *  If the caller's access level is too low
     */
    @SuppressWarnings("unused")
    @Before("@annotation(requiredAccess)")
    public void checkAccess(RequiresAccess requiredAccess) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        //User does not have a valid/active session
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedException("No valid session. Please login again.");
        }

        //Get the role from the user (format: "EMPLOYEE", "ADMIN", etc.)
        String roleString = authentication.getAuthorities().stream()
                .findFirst()
                .map(grantedAuthority -> grantedAuthority.getAuthority().replace("ROLE_", ""))
                .orElseThrow(() -> new UnauthorizedException("No role assigned to this account."));

        AccessLevel callerLevel;
        try {
            callerLevel = AccessLevel.valueOf(roleString);
        } catch (IllegalArgumentException e) {
            throw new UnauthorizedException("Unrecognised role: " + roleString);
        }

        AccessLevel requiredLevel = requiredAccess.value();

        //Hierarchical check: user's ordinal must be >= required ordinal
        if (callerLevel.ordinal() < requiredLevel.ordinal()) {
            throw new InsufficientAccessLevelException(requiredLevel, callerLevel);
        }
    }
}
