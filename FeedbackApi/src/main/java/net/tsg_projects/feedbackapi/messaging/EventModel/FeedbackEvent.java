package net.tsg_projects.feedbackapi.messaging.EventModel;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackEvent {
    private String id;
    private String memberId;
    private String providerName;
    private Integer rating;
    private String comment;
    private Instant submittedAt;
    private int schemaVersion = 1;
}
