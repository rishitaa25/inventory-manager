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
public class GlobalExceptionHandler {
    /**
     * A custom RuntimeException for converting generic RuntimeExceptions into an
     * HTTP server error code
     * @return
     *  An HTTP Internal Server Error code (Error 500)
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleGeneral(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(e.getMessage());
        //.body("An unexpected error occurred");
    }

    /**
     * A custom RuntimeException for converting an Employee related RuntimeException into
     * an HTTP server error code
     * @param e
     *  The Employee related RuntimeException
     * @return
     *  An HTTP Not Found code (Error 404)
     */
    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<String> handleEmployeeNotFound(EmployeeNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    /**
     * A custom RuntimeException for converting an Admin related RuntimeException into
     * an HTTP server error code
     * @param e
     *  The Admin related RuntimeException
     * @return
     *  An HTTP Not Found code (Error 404)
     */
    @ExceptionHandler(AdminNotFoundException.class)
    public ResponseEntity<String> handleAdminNotFound(AdminNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    /**
     * A custom RuntimeException for converting a Driver related RuntimeException into
     * an HTTP server error code
     * @param e
     *  The Driver related RuntimeException
     * @return
     *  An HTTP Not Found code (Error 404)
     */
    @ExceptionHandler(DriverNotFoundException.class)
    public ResponseEntity<String> handleDriverNotFound(DriverNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    /**
     * A custom RuntimeException for converting an Inventory related RuntimeException into
     * an HTTP server error code
     * @param e
     *  The Inventory related RuntimeException
     * @return
     *  An HTTP Not Found code (Error 404)
     */
    @ExceptionHandler(InventoryNotFoundException.class)
    public ResponseEntity<String> handleInventoryNotFound(InventoryNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    /**
     * A custom RuntimeException for converting a Shipment related RuntimeException into
     * an HTTP server error code
     * @param e
     *  The Shipment related RuntimeException
     * @return
     *  An HTTP Not Found code (Error 404)
     */
    @ExceptionHandler(ShipmentNotFoundException.class)
    public ResponseEntity<String> handleShipmentNotFound(ShipmentNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    /**
     * A custom RuntimeException for converting an Item related RuntimeException into
     * an HTTP server error code
     * @param e
     *  The Item related RuntimeException
     * @return
     *  An HTTP Not Found code (Error 404)
     */
    @ExceptionHandler(ItemNotFoundException.class)
    public ResponseEntity<String> handleItemNotFound(ItemNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
}
