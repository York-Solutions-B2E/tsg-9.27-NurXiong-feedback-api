package net.tsg_projects.feedbackapi.controllers;

import java.time.Instant;
import java.util.Map;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;
import net.tsg_projects.feedbackapi.Validation.ValidationError;
import net.tsg_projects.feedbackapi.Validation.ValidationException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(ValidationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of(
                        "TimeStamp", Instant.now(),
                        "Status", 400,
                        "Errors", ex.getErrors()
                ));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleEmptyRequestException(HttpMessageNotReadableException ex) {
        ValidationError error = new ValidationError("InvalidRequestBody", "You must provide a request body!");
//        ValidationException validEx = new ValidationException(Collections.singletonList(error));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("Errors", error, "Status", 400, "Timestamp", Instant.now()));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleResourceNotFoundException(ResourceNotFoundException ex) {
//        ValidationError error = new ValidationError("ResourceNotFoundException", "Resource not found!");
        Map<String, Object> errorJson = Map.of(
                "Field", "Resource not found",
                "Message", ex.getMessage()
        );
        return ResponseEntity.status(404).body(Map.of("Error", errorJson, "Status", 404, "Timestamp", Instant.now()));
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNoHandlerFoundException(NoHandlerFoundException ex) {
        Map<String, Object> errorJson = Map.of(
                "Field", "404 Not Found",
                "Message", "No specified handler for that path!",
                "Status", 404
        );
        return ResponseEntity.status(404).body(errorJson);
    }
}
