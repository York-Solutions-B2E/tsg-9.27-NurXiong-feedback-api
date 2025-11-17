package net.tsg_projects.feedbackapi.FeedbackMapper;

import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.tsg_projects.feedbackapi.dtos.FeedbackEntityDto;
import net.tsg_projects.feedbackapi.dtos.FeedbackRequest;
import net.tsg_projects.feedbackapi.dtos.FeedbackResponse;
import net.tsg_projects.feedbackapi.messaging.EventModel.FeedbackEvent;
import net.tsg_projects.feedbackapi.repositories.entities.FeedbackEntity;


/**
 *
 * Mapper helper class responsible for easily mapping java objects to dto's
 */
@Component
public class FeedbackMapper {

    private final ObjectMapper objectMapper;

    public FeedbackMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public FeedbackEntity toEntity(FeedbackRequest feedbackRequest) {
        return objectMapper.convertValue(feedbackRequest, FeedbackEntity.class);

    }

    public FeedbackRequest toFeedbackRequest(FeedbackEntity feedbackEntity) {
        return objectMapper.convertValue(feedbackEntity, FeedbackRequest.class);
    }

    public FeedbackResponse toFeedbackResponse(FeedbackEntity feedbackEntity) {
        return objectMapper.convertValue(feedbackEntity, FeedbackResponse.class);

    }

    public FeedbackEvent toFeedbackEvent(FeedbackEntity feedbackEntity) {
        return objectMapper.convertValue(feedbackEntity, FeedbackEvent.class);
    }

    public Object toGetResponseObject(FeedbackEntity feedbackEntity) {
        return objectMapper.convertValue(feedbackEntity, Object.class);
    }

    public FeedbackEntityDto toGetResponseDto(FeedbackEntity feedbackEntity) {
        return objectMapper.convertValue(feedbackEntity, FeedbackEntityDto.class);
    }
}
