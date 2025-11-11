package net.tsg_projects.feedbackapi.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

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
