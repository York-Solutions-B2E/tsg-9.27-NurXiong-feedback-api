package net.tsg_projects.feedbackapi.services;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import net.tsg_projects.feedbackapi.FeedbackMapper.FeedbackMapper;
import net.tsg_projects.feedbackapi.Validation.ValidationError;
import net.tsg_projects.feedbackapi.Validation.ValidationException;
import net.tsg_projects.feedbackapi.dtos.FeedbackEntityDto;
import net.tsg_projects.feedbackapi.dtos.FeedbackRequest;
import net.tsg_projects.feedbackapi.dtos.FeedbackResponse;
import net.tsg_projects.feedbackapi.messaging.FeedbackEventPublisher;
import net.tsg_projects.feedbackapi.repositories.FeedbackRepository;
import net.tsg_projects.feedbackapi.repositories.entities.FeedbackEntity;

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

        // Will validate incoming request first
        // If no exceptions are thrown continue on
        FeedbackValidation(requestDto);

        // Validation Complete moving on to Entity creation
        FeedbackEntity feedback = feedbackMapper.toEntity(requestDto);
        feedbackRepository.save(feedback);

        feedbackEventPublisher.publishFeedback(feedback);

        return new FeedbackResponse(
                feedback.getId().toString(),
                feedback.getMemberId(),
                feedback.getProviderName(),
                feedback.getRating(),
                feedback.getComment(),
                feedback.getSubmittedAt()
        );

    }

    public void FeedbackValidation(FeedbackRequest requestDto){

        // Begin Validating incoming request
        // Will use If statements to populate list of validation errors
        List<ValidationError> errors = new ArrayList<>();

        if(requestDto == null){
            errors.add(new ValidationError("No Feedback", "Feedback Request can't be null"));
            throw new ValidationException(errors);
        }

        if(requestDto.getMemberId() == null || requestDto.getMemberId().isEmpty() || requestDto.getMemberId().isBlank() || requestDto.getMemberId().length() > 36){
            errors.add(new ValidationError("Member Id", "Member Id can't be empty and can't exceed 36 characters"));
        }

        if(requestDto.getProviderName() == null || requestDto.getProviderName().isEmpty() || requestDto.getProviderName().isBlank() || requestDto.getProviderName().length() > 80) {
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
    }

    public FeedbackEntityDto getFeedback(String ID){
        try {
                UUID entityId = UUID.fromString(ID);
                FeedbackEntity feedback = feedbackRepository.findById(entityId).orElse(null);

                if(feedback == null) {
                    // If the given UUID is not in the database throw RNFe
                    throw new ResourceNotFoundException("Feedback Not Found " + entityId);
                }
                else {
                    // If no exceptions are thrown just return an Object with feedback details
                    // Mapped from DB Entity --> Object
                    return feedbackMapper.toGetResponseDto(feedback);
                }

        } catch (IllegalArgumentException e){
            // If converting Id paramater to UUID failed throw VALIDATIONEXCEPTION -
            // Pass a new ValidationError object to map key and value
            throw new ValidationException(Collections.singletonList(new ValidationError("Feedback ID", "Invalid UUID format or empty ID")));
        } catch (ResourceNotFoundException e){
            // Finally catch and through exception we through in the if block
            // GlobalExceptionHandler listens for these exceptions and handles accordingly
            throw e;
        }

    }

    public List<FeedbackEntityDto> getFeedbackList(String memberId){

       try {
           if(memberId == null || memberId.isEmpty()){
               throw new ValidationException(Collections.singletonList(new ValidationError("Member Id", "Member Id can not be null or empty")));
           }

           List<FeedbackEntity> entityList = feedbackRepository.findAllByMemberId(memberId);

           if(entityList.isEmpty()){
               // then return only 200 status code OK in controller
               throw new ResourceNotFoundException("Feedback Not Found MemberId " + memberId);
           }  else {
               return entityList.stream().map(feedbackMapper::toGetResponseDto).toList();
           }

       } catch (ValidationException | ResourceNotFoundException e) {
                throw e;
       } catch (Exception e){
           // In case of anything unexpected
           throw new RuntimeException("Unexpected error while retrieving feedback for memberId: " + memberId, e);
       }

    }
}
