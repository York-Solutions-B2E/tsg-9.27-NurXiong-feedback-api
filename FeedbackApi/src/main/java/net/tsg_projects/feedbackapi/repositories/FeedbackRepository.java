package net.tsg_projects.feedbackapi.repositories;

import net.tsg_projects.feedbackapi.repositories.entities.FeedbackEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FeedbackRepository extends JpaRepository<FeedbackEntity, UUID> {
}
