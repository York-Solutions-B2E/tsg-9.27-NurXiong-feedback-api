package net.tsg_projects.feedbackapi.services;


import net.tsg_projects.feedbackapi.FeedbackMapper.FeedbackMapper;
import net.tsg_projects.feedbackapi.Validation.ValidationError;
import net.tsg_projects.feedbackapi.dtos.FeedbackRequest;
import net.tsg_projects.feedbackapi.dtos.FeedbackResponse;
import net.tsg_projects.feedbackapi.messaging.FeedbackEventPublisher;
import net.tsg_projects.feedbackapi.repositories.FeedbackRepository;
import net.tsg_projects.feedbackapi.repositories.entities.FeedbackEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final FeedbackEventPublisher feedbackEventPublisher;
    private final FeedbackMapper feedbackMapper;

    public FeedbackService(FeedbackRepository feedbackRepository, FeedbackMapper feedbackMapper, FeedbackEventPublisher feedbackEventPublisher) {
        this.feedbackRepository = feedbackRepository;
        this.feedbackMapper = feedbackMapper;
        this.feedbackEventPublisher = feedbackEventPublisher;
    }

    public FeedbackResponse handleFeedbackRequest(FeedbackRequest requestDto){

        // Begin Validating incoming request
        // Will use If statements to populate list of validation errors
        List<ValidationError> errors = new ArrayList<>();

        if(requestDto == null) errors.add(new ValidationError("No Feedback", "Feedback Request can't be null"));

        if(requestDto.getMemberId() == null || requestDto.getMemberId().isEmpty() || requestDto.getMemberId().length() > 36){
            errors.add(new ValidationError("Member Id", "Member Id can't be empty and can't exceed 36 characters"));
        }

        if(requestDto.getProviderName() == null || requestDto.getProviderName().isEmpty() || requestDto.getProviderName().length() > 80) {
            errors.add(new ValidationError("Provider Name", "Provider Name can't be empty and can't exceed 80 characters"));
        }

        if (requestDto.getRating() <= 0 || requestDto.getRating() > 5) {
            errors.add(new ValidationError("Rating", "Rating can't be less than 1 or greater than 5"));
        }

        if(requestDto.getComment() == null || requestDto.getComment().isEmpty() || requestDto.getComment().length() > 200) {
            errors.add(new ValidationError("Comment", "Comment can't be empty and can't exceed 200 characters"));
        }

        // If the list is not empty throw new Validation Exception
        if(!errors.isEmpty()){
            throw new ValidationException(errors);
        }

        // Validation Complete moving on to Entity creation
        FeedbackEntity feedback = feedbackMapper.toEntity(requestDto);
        feedbackRepository.save(feedback);

        feedbackEventPublisher.publishFeedback(feedback);

        return new FeedbackResponse(feedback.getMemberId(), feedback.getSubmittedAt());

    }
}
