package net.tsg_projects.feedbackapi.dtos;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackResponse {
    private String id;
    private String memberId;
    private String providerName;
    private int rating;
    private String comment;
    private Instant submittedAt;
}
