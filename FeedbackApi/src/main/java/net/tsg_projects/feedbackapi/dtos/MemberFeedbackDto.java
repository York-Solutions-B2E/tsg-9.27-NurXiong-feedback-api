package net.tsg_projects.feedbackapi.dtos;


import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Response object containing feedback details for a member")
public class MemberFeedbackDto {

    @Schema(description = "List of feedback responses for member")
    private List<FeedbackEntityDto> FeedbackList;
    @Schema(description = "ID of the member")
    private String MemberId;
    @Schema(description = "Total count of feedback responses")
    private int Count;
}
