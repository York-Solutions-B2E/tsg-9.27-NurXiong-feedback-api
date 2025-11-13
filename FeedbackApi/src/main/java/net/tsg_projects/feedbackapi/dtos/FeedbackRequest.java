package net.tsg_projects.feedbackapi.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackRequest {

    @NotBlank
    @Size(max = 36)
    private String memberId;

    @NotBlank
    @Size(max = 80)
    private String providerName;

    @Min(1)
    @Max(5)
    private int rating;

    @NotBlank
    @Size(max = 200)
    private String comment;



}
