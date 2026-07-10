package org.example.inventory_manager_beta1.Configuration.Annotation;

import org.example.inventory_manager_beta1.DataEnums.AccessLevel;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Method-level annotation for declarative access control.
 * Place this on any controller or service method to restrict it to callers
 * whose {@link AccessLevel} meets or exceeds the specified minimum.
 *
 * <p>The check is hierarchical — a caller's ordinal must be greater than or
 * equal to the required level's ordinal:
 * {@code EMPLOYEE(0) < DRIVER(1) < ADMIN(2) < GENERAL_MANAGER(3)}
 *
 * <p>Enforcement is handled by {@link AccessControl}, which reads the caller's
 * role from the {@code SecurityContextHolder} populated by {@code JwtAuthFilter}.
 *
 * <p>Usage examples:
 * <pre>
 *   //Any logged-in user can call this
 *   {@literal @}RequiresAccess(AccessLevel.EMPLOYEE)
 *   public List{@literal <}Item{@literal >} getInventory() { ... }
 *
 *   //Only ADMIN and GENERAL_MANAGER can call this
 *   {@literal @}RequiresAccess(AccessLevel.ADMIN)
 *   public void deleteEmployee(Integer id) { ... }
 *
 *   //Only GENERAL_MANAGER can call this
 *   {@literal @}RequiresAccess(AccessLevel.GENERAL_MANAGER)
 *   public void deleteAllAdmins() { ... }
 * </pre>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiresAccess {AccessLevel value();}