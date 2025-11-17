package net.tsg_projects.feedbackapi.controllerTest;


import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.tsg_projects.feedbackapi.controllers.FeedbackController;
import net.tsg_projects.feedbackapi.dtos.FeedbackEntityDto;
import net.tsg_projects.feedbackapi.dtos.FeedbackRequest;
import net.tsg_projects.feedbackapi.dtos.FeedbackResponse;
import net.tsg_projects.feedbackapi.securityConfig.SecurityConfig;
import net.tsg_projects.feedbackapi.services.FeedbackService;

@ActiveProfiles("test")
@WebMvcTest(FeedbackController.class)
@Import(SecurityConfig.class)
public class FeedbackControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FeedbackService feedbackService;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    public void tesPostFeedback_HappyPath() throws Exception {
        FeedbackRequest mockRequest = new FeedbackRequest(
                "m-123",
                "Dr. Mock",
                5,
                "Great"
        );

       FeedbackResponse mockResponse = new FeedbackResponse(
                UUID.randomUUID().toString(),
                mockRequest.getMemberId(),
                mockRequest.getProviderName(),
                mockRequest.getRating(),
                mockRequest.getComment(),
                Instant.now()
        );

        when(feedbackService.handleFeedbackRequest(any())).thenReturn(mockResponse);



        mockMvc.perform(post("/api/v1/feedback")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mockRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(mockResponse.getId()))
                .andExpect(jsonPath("$.memberId").value(mockRequest.getMemberId()))
                .andExpect(jsonPath("$.providerName").value(mockRequest.getProviderName()))
                .andExpect(jsonPath("$.rating").value(mockRequest.getRating()))
                .andExpect(jsonPath("$.comment").value(mockRequest.getComment()));

        verify(feedbackService, times(1)).handleFeedbackRequest(any(FeedbackRequest.class));
    }

    @Test
    public void testGetFeedbackById_HappyPath() throws Exception {

        UUID feedbackId = UUID.randomUUID();

        FeedbackEntityDto mockResponse = new FeedbackEntityDto(
                feedbackId,
                "M12345",
                "Dr. Smith",
                5,
                "Excellent care!",
                Instant.now()
        );

        when(feedbackService.getFeedback(any())).thenReturn(mockResponse);

        mockMvc.perform(get("/api/v1/feedback/" + feedbackId))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.Feedback.id").value(mockResponse.getId().toString()))
                .andExpect(jsonPath("$.Feedback.memberId").value(mockResponse.getMemberId()))
                .andExpect(jsonPath("$.Feedback.providerName").value(mockResponse.getProviderName()))
                .andExpect(jsonPath("$.Feedback.rating").value(mockResponse.getRating()))
                .andExpect(jsonPath("$.Feedback.comment").value(mockResponse.getComment()));

        verify(feedbackService, times(1)).getFeedback(any());

    }

    @Test
    public void testGetFeedbackListByMemberId_HappyPath() throws Exception {

        UUID feedbackId = UUID.randomUUID();
        FeedbackEntityDto mockResponse = new FeedbackEntityDto(
                feedbackId,
                "M12345",
                "Dr. Smith",
                5,
                "Excellent care!",
                Instant.now()
        );

        when(feedbackService.getFeedbackList("M12345")).thenReturn(List.of(mockResponse));

        mockMvc.perform(get("/api/v1/feedback").param("memberId", "M12345").
                        contentType(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andExpect(status().isOk()).
                andExpect(jsonPath("$.FeedbackList.[0].id").value(mockResponse.getId().toString())).
                andExpect(jsonPath("$.FeedbackList.[0].memberId").value(mockResponse.getMemberId()));

        verify(feedbackService, times(1)).getFeedbackList("M12345");

    }

    @Test
    public void testHealth_HappyPath() throws Exception {

        mockMvc.perform(get("/api/v1/health")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Status").value("UP"))
                .andExpect(jsonPath("$.Name").value("feedback-api"));
        verifyNoInteractions(feedbackService);
    }


    @Test
    public void PostFeedback_ValidationError_ShouldReturn400() throws Exception {
        FeedbackRequest mockRequest = new FeedbackRequest("", "", 6, "");

        mockMvc.perform(post("/api/v1/feedback").
                contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mockRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation Failed @ Controller"))
                .andExpect(jsonPath("$.errors").isMap());

        verifyNoInteractions(feedbackService);

    }

    @Test
    public void GetFeedbackById_MissingPathVariable_ShouldReturn400() throws Exception {
        mockMvc.perform(get("/api/v1/feedback/ "))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(feedbackService);
    }

}
