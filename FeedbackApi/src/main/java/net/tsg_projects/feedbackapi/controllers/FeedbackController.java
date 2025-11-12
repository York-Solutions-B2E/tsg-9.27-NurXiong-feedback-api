package net.tsg_projects.feedbackapi.controllers;


import java.time.Instant;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import net.tsg_projects.feedbackapi.dtos.FeedbackEntityDto;
import net.tsg_projects.feedbackapi.dtos.FeedbackRequest;
import net.tsg_projects.feedbackapi.dtos.FeedbackResponse;
import net.tsg_projects.feedbackapi.services.FeedbackService;

@CrossOrigin("http://localhost:5173")
@RestController
@RequestMapping("/api/v1")
public class FeedbackController {

    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @PostMapping("/feedback")
    public ResponseEntity<FeedbackResponse> postFeedback(@Valid @RequestBody FeedbackRequest feedbackRequest){
        FeedbackResponse response = feedbackService.handleFeedbackRequest(feedbackRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/feedback/{id}")
    public ResponseEntity<Map<String, FeedbackEntityDto>> getFeedback(@Valid @PathVariable String id){
        FeedbackEntityDto response  = feedbackService.getFeedback(id);
        return ResponseEntity.status(200).body(Map.of("Feedback", response));
    }

    @GetMapping("/feedback")
    public ResponseEntity<Map<String, Object>> getFeedbacks(@Valid @RequestParam(required = false) String memberId){
        List<FeedbackEntityDto> feedbackList = feedbackService.getFeedbackList(memberId);
        return ResponseEntity.status(200).body(Map.of("FeedbackList", feedbackList, "MemberId", memberId, "Count", feedbackList.size()));
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> getHealth(){
        return ResponseEntity.status(200).body(Map.of("Health", "Healthy - Server Listening...", "Status", "OK", "TimeStamp", Instant.now()));
    }

}
