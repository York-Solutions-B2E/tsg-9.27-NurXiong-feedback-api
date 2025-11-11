package net.tsg_projects.feedbackapi.Validation;

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
