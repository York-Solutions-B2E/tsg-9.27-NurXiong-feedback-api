package net.tsg_projects.feedbackapi.messaging.EventModel;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
