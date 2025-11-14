package net.tsg_projects.feedbackapi.controllers;


import java.time.Instant;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import net.tsg_projects.feedbackapi.dtos.FeedbackEntityDto;
import net.tsg_projects.feedbackapi.dtos.FeedbackRequest;
import net.tsg_projects.feedbackapi.dtos.FeedbackResponse;
import net.tsg_projects.feedbackapi.dtos.MemberFeedbackDto;
import net.tsg_projects.feedbackapi.services.FeedbackService;

@CrossOrigin("http://localhost:5173")
@RestController
@RequestMapping("/api/v1")
public class FeedbackController {

    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    /**
     *
     *
     * @param feedbackRequest
     * @return status: 201, body: FeeedbackResponse
     */
    @ApiResponse(responseCode = "201", description = "Feedback created, Feedback Event published to " +
            "feedback-submitted topic.", content = { @Content(mediaType = "application/json",
                                        schema = @Schema(implementation = FeedbackResponse.class) ) })
    @Operation(summary = "Post member feedback, valid payload returns feedback response")
    @PostMapping("/feedback")
    public ResponseEntity<FeedbackResponse> postFeedback(@Valid @RequestBody FeedbackRequest feedbackRequest){
        FeedbackResponse response = feedbackService.handleFeedbackRequest(feedbackRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    /**
     *
     * @param id
     * @return status: 200, body: 1 feedback on success
     */
    @ApiResponse(responseCode = "200", description = "Single Feedback returned matching path Id",
                                        content = { @Content(mediaType = "application/json",
                                        schema = @Schema(implementation = FeedbackResponse.class) ) })
    @Operation(summary = "Get single Feedback using Path variable Id. Returns feedback response")
    @GetMapping("/feedback/{id}")
    public ResponseEntity<Map<String, FeedbackEntityDto>> getFeedbackById(
             @Valid @NotBlank @PathVariable String id){
        FeedbackEntityDto response  = feedbackService.getFeedback(id);
        return ResponseEntity.status(200).body(Map.of("Feedback", response));
    }


    /**
     *
     * @param memberId
     * @return status: 200, body: list of feedbacks for memberId
     */
    @ApiResponse(responseCode = "200", description = "Get list of feedbacks using Param memberId",
                                        content = { @Content(mediaType = "application/json",
                                        schema = @Schema(implementation = MemberFeedbackDto.class) ) })
    @Operation(summary = "Gets a list of feedback entries by memberId. Returns empty list if no entries")
    @GetMapping("/feedback")
    public ResponseEntity<Map<String, Object>> getFeedbacks(@RequestParam(required = false) String memberId){
        List<FeedbackEntityDto> feedbackList = feedbackService.getFeedbackList(memberId);
        return ResponseEntity.status(200).body(Map.of("FeedbackList", feedbackList, "MemberId", memberId, "Count", feedbackList.size()));
    }

    /**
     *
     * @return status: 200, basic health check
     */
    @ApiResponse(responseCode = "200", description = "Basic health endpoint",
                                        content = { @Content(mediaType = "application/json")})
    @Operation(summary = "Get basic health of server")
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> getHealth(){
        return ResponseEntity.status(200).body(Map.of("Name", "feedback-api", "Status", "UP", "TimeStamp", Instant.now()));
    }

}
