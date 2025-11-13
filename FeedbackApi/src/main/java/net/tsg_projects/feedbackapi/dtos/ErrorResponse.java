package net.tsg_projects.feedbackapi.dtos;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ErrorResponse {
    private String message;
    private  Map<String, Object> errors;
}
