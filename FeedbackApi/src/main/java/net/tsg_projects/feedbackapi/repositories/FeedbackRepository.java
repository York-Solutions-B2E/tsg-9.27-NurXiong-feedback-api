package net.tsg_projects.feedbackapi.repositories;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import net.tsg_projects.feedbackapi.repositories.entities.FeedbackEntity;


/**
 *
 * Repository layer responsible for interacting with database
 */
@Repository
public interface FeedbackRepository extends JpaRepository<FeedbackEntity, UUID> {
    List<FeedbackEntity> findAllByMemberId(String memberId);
}
