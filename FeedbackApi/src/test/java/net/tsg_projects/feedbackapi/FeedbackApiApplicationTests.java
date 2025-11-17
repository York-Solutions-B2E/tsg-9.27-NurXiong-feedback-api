package net.tsg_projects.feedbackapi;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
@Disabled("contextLoads test disabled because we don't need full context startup during tests")
class FeedbackApiApplicationTests {

    @Test
    void contextLoads() {
    }

}
