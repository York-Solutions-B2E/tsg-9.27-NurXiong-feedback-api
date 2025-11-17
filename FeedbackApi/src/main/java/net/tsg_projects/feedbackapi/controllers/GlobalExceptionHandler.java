package net.tsg_projects.feedbackapi.controllers;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import net.tsg_projects.feedbackapi.Validation.ValidationError;
import net.tsg_projects.feedbackapi.Validation.ValidationException;
import net.tsg_projects.feedbackapi.dtos.ErrorResponse;


/**
 *
 * ControllerAdvice listens for any exceptions and handles the response sent to client
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     *
     * @param ex: Exception that was thrown
     * @return Map of validation erros
     */
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(ValidationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of(
                        "TimeStamp", Instant.now(),
                        "Status", 400,
                        "Errors", ex.getErrors()
                ));
    }

    /**
     *
     * @param ex: If request body was empty
     * @return Map
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleEmptyRequestException(HttpMessageNotReadableException ex) {
        ValidationError error = new ValidationError("InvalidRequestBody", "You must provide a request body!");
//        ValidationException validEx = new ValidationException(Collections.singletonList(error));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("Errors", error, "Status", 400, "Timestamp", Instant.now()));
    }

    /**
     *
     * @param ex: Exception thrown for resourceNotFound
     * @return Map of errors
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleResourceNotFoundException(ResourceNotFoundException ex) {
//        ValidationError error = new ValidationError("ResourceNotFoundException", "Resource not found!");
        Map<String, Object> errorJson = Map.of(
                "Field", "Resource not found",
                "Message", ex.getMessage(),
                "EmptyList", List.of()
        );
        return ResponseEntity.status(200).body(Map.of("NoFeedbacks", errorJson, "Status", 200, "Timestamp", Instant.now()));
    }


    /**
     *
     * @param ex
     * @return Returns custom error response for the @Valid annotation on endpoint methods
     * Used for testing controller
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, Object> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach((fieldError) -> {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        });
        ErrorResponse error = new ErrorResponse("Validation Failed @ Controller", errors);
        return ResponseEntity.status(400).body(error);
    }
}
