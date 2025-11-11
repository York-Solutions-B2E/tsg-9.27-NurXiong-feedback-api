package net.tsg_projects.feedbackapi.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackEntityDto {
    private UUID id;
    private String memberId;
    private String providerName;
    private int rating;
    private String comment;
    private Instant submittedAt;
}
