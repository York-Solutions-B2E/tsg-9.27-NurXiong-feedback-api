package net.tsg_projects.feedbackapi.serviceTest;

import net.tsg_projects.feedbackapi.FeedbackMapper.FeedbackMapper;
import net.tsg_projects.feedbackapi.Validation.ValidationError;
import net.tsg_projects.feedbackapi.Validation.ValidationException;
import net.tsg_projects.feedbackapi.dtos.FeedbackRequest;
import net.tsg_projects.feedbackapi.dtos.FeedbackResponse;
import net.tsg_projects.feedbackapi.messaging.FeedbackEventPublisher;
import net.tsg_projects.feedbackapi.repositories.FeedbackRepository;
import net.tsg_projects.feedbackapi.repositories.entities.FeedbackEntity;
import net.tsg_projects.feedbackapi.services.FeedbackService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FeedbackServiceTest {

    @Mock
    private FeedbackEventPublisher publisher;
    @Mock
    private FeedbackMapper mapper;
    @Mock
    private FeedbackRepository feedbackRepository;

    @InjectMocks
    private FeedbackService service;

    @Test
    public void handleFeedbackRequest_ValidRequest_ShouldCallPublisher_ReturnsFeedbackResponse() {

        FeedbackRequest request = new FeedbackRequest(
                "M12345",
                "Dr. Smith",
                5,
                "Excellent service and very professional."
        );


        // Create a mock FeedbackEntity that your mapper and repository will return

        FeedbackEntity mockEntity = new FeedbackEntity();
        mockEntity.setId(UUID.randomUUID());
        mockEntity.setMemberId(request.getMemberId());  // <-- match request value
        mockEntity.setProviderName(request.getProviderName());
        mockEntity.setRating(request.getRating());
        mockEntity.setComment(request.getComment());
        mockEntity.setSubmittedAt(Instant.now());


        // Stub the mapper and repository
        when(mapper.toEntity(any(FeedbackRequest.class))).thenReturn(mockEntity);
        when(feedbackRepository.save(any(FeedbackEntity.class))).thenReturn(mockEntity);

        FeedbackResponse response = service.handleFeedbackRequest(request);

        verify(publisher, times(1)).publishFeedback(any(FeedbackEntity.class));

        //Assert each field is not null except and matches request
        //(response.Id, response.Timestamp) will only have assert statements since request doesn't contain those fields
        assertNotNull(response.getId());
        assertNotNull(response.getMemberId());
        assertNotNull(response.getProviderName());

        // Can't null check int primitive type. Instead verify not zero meaning value is present
        assertNotEquals(0, response.getRating());
        assertNotNull(response.getComment());
        assertNotNull(response.getSubmittedAt());

        assertEquals(mockEntity.getId().toString(), response.getId());
        assertEquals(request.getMemberId(), response.getMemberId());
        assertEquals(response.getProviderName(), request.getProviderName());
        assertEquals(response.getRating(), request.getRating());
        assertEquals(response.getComment(), request.getComment());
        //Verify timestamp is in the past
        assertTrue(response.getSubmittedAt().isBefore(Instant.now()));


    }

    @Test
    public void handleFeedbackRequest_InvalidMemberId_ShouldIncludeMemberIdError() {
        FeedbackRequest invalidRequest = new FeedbackRequest("", "Dr. Smith", 5, "invalid memberId");

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            service.handleFeedbackRequest(invalidRequest);
        });

        List<ValidationError> errors = exception.getErrors();

        assertTrue(errors.stream().anyMatch(error -> error.getField().equals("Member Id")));

    }

    @Test
    public void handleFeedbackRequest_InvalidProviderName_ShouldIncludeProviderNameError() {

        FeedbackRequest invalidRequest = new FeedbackRequest("M12345", "", 5, "invalid providerName");

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            service.handleFeedbackRequest(invalidRequest);
        });
        List<ValidationError> errors = exception.getErrors();

        assertTrue(errors.stream().anyMatch(error -> error.getField().equals("Provider Name")));

    }

    @Test
    public void handleFeedbackRequest_InvalidRating_ShouldIncludeRatingError() {
        FeedbackRequest invalidRequest = new FeedbackRequest("M12345", "Dr. Smith", 6, "invalid rating");
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            service.handleFeedbackRequest(invalidRequest);
        });
        List<ValidationError> errors = exception.getErrors();
        assertTrue(errors.stream().anyMatch(error -> error.getField().equals("Rating")));
    }

    @Test
    public void handleFeedbackRequest_InvalidComment_ShouldIncludeCommentError() {
        FeedbackRequest invalidRequest = new FeedbackRequest("M12345", "Dr. Smith", 5, "");
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            service.handleFeedbackRequest(invalidRequest);
        });
        List<ValidationError> errors = exception.getErrors();
        assertTrue(errors.stream().anyMatch(error -> error.getField().equals("Comment")));
    }

    @Test
    public void shouldThrowValidationExceptionForEachInvalidRequest() {
        List<FeedbackRequest> invalidRequests = List.of(
                new FeedbackRequest(" ", "Dr. Smith", 5, "Great service!"),
                new FeedbackRequest("M12345", "  ", 4, "Provider name missing"),
                new FeedbackRequest("M12345", "Dr. Smith", 8, "Too high rating"),
                new FeedbackRequest("M12345", "Dr. Smith", 5, "")
        );

        for (FeedbackRequest request : invalidRequests) {
            // Expecting each invalidRequest to throw ValidationException
                ValidationException exception = assertThrows(ValidationException.class, () -> {
                    service.handleFeedbackRequest(request);
            });
            // If the exception objects list doesn't contain a ValidationError for this request Test fails
                assertFalse(exception.getErrors().isEmpty(), "Expected validation errors for request: " + request);
        }
    }

}


