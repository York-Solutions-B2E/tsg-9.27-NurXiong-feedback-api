package net.tsg_projects.feedbackapi.messagingTest;

import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import net.tsg_projects.feedbackapi.FeedbackMapper.FeedbackMapper;
import net.tsg_projects.feedbackapi.messaging.EventModel.FeedbackEvent;
import net.tsg_projects.feedbackapi.messaging.FeedbackEventPublisher;
import net.tsg_projects.feedbackapi.repositories.entities.FeedbackEntity;

@ExtendWith(MockitoExtension.class)
public class FeedbackEventPublisherTest {


    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Mock
    private FeedbackMapper feedbackMapper;

    @InjectMocks
    private FeedbackEventPublisher feedbackEventPublisher;

    @BeforeEach
    public void setup(){
        feedbackEventPublisher = new FeedbackEventPublisher(kafkaTemplate, "feedback-submitted",  feedbackMapper);
    }

    @Test
    public void publishEvent_Valid_ShouldPublishFeedbackEvent()
    {
        //Arrange
        FeedbackEntity feedbackEntity = new FeedbackEntity(
                UUID.randomUUID(),
                "mock-123",
                "Dr. York",
                5,
                "5 starts",
                Instant.now()
        );

        FeedbackEvent feedbackEvent = new FeedbackEvent();
        feedbackEvent.setId(feedbackEntity.getId().toString());
        when(feedbackMapper.toFeedbackEvent(feedbackEntity)).thenReturn(feedbackEvent);

        // Act
        feedbackEventPublisher.publishFeedback(feedbackEntity);

        //Assert
        verify(feedbackMapper).toFeedbackEvent(feedbackEntity);
        verify(kafkaTemplate).send(anyString(), eq(feedbackEvent.getId()), eq(feedbackEvent));
    }


}
