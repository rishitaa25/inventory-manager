package org.example.inventory_manager_beta1.ExceptionHandling;

import org.example.inventory_manager_beta1.ExceptionHandling.Exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * The global Exception Handler class for handling different Exceptions and converting
 * them into readable HTTP codes for frontend use and for the users of the app
 */
@RestControllerAdvice
@SuppressWarnings("unused")
public class GlobalExceptionHandler {

    /**
     * Catches any unhandled RuntimeException and returns a generic server error.
     * Acts as a safety net for exceptions not covered by a more specific handler.
     * @param e The RuntimeException
     * @return An HTTP Internal Server Error response (500)
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleGeneral(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }

    /**
     * @param e
     *  The EmployeeNotFoundException
     * @return
     *  An HTTP Not Found response (404)
     */
    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<String> handleEmployeeNotFound(EmployeeNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    /**
     * @param e
     *  The AdminNotFoundException
     * @return
     *  An HTTP Not Found response (404)
     */
    @ExceptionHandler(AdminNotFoundException.class)
    public ResponseEntity<String> handleAdminNotFound(AdminNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    /**
     * @param e
     *  The DriverNotFoundException
     * @return
     *  An HTTP Not Found response (404)
     */
    @ExceptionHandler(DriverNotFoundException.class)
    public ResponseEntity<String> handleDriverNotFound(DriverNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    /**
     * @param e
     *  The InventoryNotFoundException
     * @return
     *  An HTTP Not Found response (404)
     */
    @ExceptionHandler(InventoryNotFoundException.class)
    public ResponseEntity<String> handleInventoryNotFound(InventoryNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    /**
     * @param e
     *  The InventoryChangeNotFoundException
     * @return
     *  An HTTP Not Found response (404)
     */
    @ExceptionHandler(InventoryChangeNotFoundException.class)
    public ResponseEntity<String> handleInventoryChangeNotFound(InventoryChangeNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    /**
     * @param e
     *  The ShipmentNotFoundException
     * @return
     *  An HTTP Not Found response (404)
     */
    @ExceptionHandler(ShipmentNotFoundException.class)
    public ResponseEntity<String> handleShipmentNotFound(ShipmentNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    /**
     * @param e
     *  The ShippingCompanyNotFoundException
     * @return
     *  An HTTP Not Found response (404)
     */
    @ExceptionHandler(ShippingCompanyNotFoundException.class)
    public ResponseEntity<String> handleShippingCompanyNotFound(ShippingCompanyNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    /**
     * @param e
     *  The ItemNotFoundException
     * @return
     *  An HTTP Not Found response (404)
     */
    @ExceptionHandler(ItemNotFoundException.class)
    public ResponseEntity<String> handleItemNotFound(ItemNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    /**
     * @param e
     *  The ShiftNotFoundException
     * @return
     *  An HTTP Not Found response (404)
     */
    @ExceptionHandler(ShiftNotFoundException.class)
    public ResponseEntity<String> handleShiftNotFound(ShiftNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    /**
     * @param e
     *  The NotificationNotFoundException
     * @return
     *  An HTTP Not Found response (404)
     */
    @ExceptionHandler(NotificationNotFoundException.class)
    public ResponseEntity<String> handleNotificationNotFound(NotificationNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    /**
     * Thrown by {@code AccessControl} when a caller's access level is too low
     * for the requested operation.
     * @param e
     *  The InsufficientAccessLevelException
     * @return
     *  An HTTP Forbidden response (403)
     */
    @ExceptionHandler(InsufficientAccessLevelException.class)
    public ResponseEntity<String> handleInsufficientAccessLevel(InsufficientAccessLevelException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
    }

    /**
     * Thrown by {@code AccessControl} when a request arrives with no valid session —
     * either no token was provided, the token is expired, or the session has been logged out.
     * @param e
     *  The UnauthorizedException
     * @return
     *  An HTTP Unauthorized response (401)
     */
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<String> handleUnauthorized(UnauthorizedException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }

    /**
     * Thrown when a user attempts an illegal action such as offloading a shipment
     * that hasn't arrived yet or has already been offloaded.
     * @param e
     *  The IllegalActionException
     * @return
     *  An HTTP Bad Request response (400)
     */
    @ExceptionHandler(IllegalActionException.class)
    public ResponseEntity<String> handleIllegalAction(IllegalActionException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}