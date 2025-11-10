package net.tsg_projects.feedbackapi.services;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.tsg_projects.feedbackapi.Validation.ValidationError;
import org.springframework.boot.context.properties.bind.validation.ValidationErrors;
import java.util.List;
public class ValidationException extends RuntimeException {

    private final List<ValidationError> errors;

    public ValidationException(List<ValidationError> errors) {
        super("Validation failed for feedback request");
        this.errors = errors;
    }

    public List<ValidationError> getErrors() {
        return errors;
    }
}
