package net.tsg_projects.feedbackapi.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import net.tsg_projects.feedbackapi.FeedbackMapper.FeedbackMapper;
import net.tsg_projects.feedbackapi.messaging.EventModel.FeedbackEvent;
import net.tsg_projects.feedbackapi.repositories.entities.FeedbackEntity;

@Component
public class FeedbackEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(FeedbackEventPublisher.class);
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final FeedbackMapper feedbackMapper;

    // Topic will be feedback-submitted
    private final String submittedTopic;

    public FeedbackEventPublisher(KafkaTemplate<String, Object> kafkaTemplate, @Value("${app.kafka.submittedTopic}") String submittedTopic, FeedbackMapper feedbackMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.submittedTopic = submittedTopic;
        this.feedbackMapper = feedbackMapper;
    }

    public void publishFeedback(FeedbackEntity entity){
        FeedbackEvent event = feedbackMapper.toFeedbackEvent(entity);
        event.setId(entity.getId().toString());

        log.info("""
                
                
                ********************** Kakfa Publishing **********************\
                
                [PUBLISHING] (FeedbackEvent) \
                
                [TOPIC-NAME] ---> {}\s
                
                [EVENT-ID]: ---> {}
                
                ****************************************************************""",submittedTopic, event.getId());
        kafkaTemplate.send(submittedTopic, event.getId(), event);

    }
}
