package net.tsg_projects.feedbackapi.repositories.entities;


import java.time.Instant;
import java.util.UUID;
import org.hibernate.annotations.UuidGenerator;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

/**
 *
 * FeedbackEntity used to store feedback data into the database
 * All relevant fields have validation
 *
 */
@Entity
@Data
@Table(name = "feedback")
public class FeedbackEntity {

    @Column(unique = true)
    @Id
    @UuidGenerator(style = UuidGenerator.Style.TIME)
    private  UUID id;

    @Column(nullable = false, length = 36)
    private String memberId;

    @Column(nullable = false, length = 80)
    private String providerName;

    @Column(nullable = false)
    @Min(1)
    @Max(5)
    private Integer rating;

    @Column(nullable = false, length = 200)
    private String comment;

    @Column(nullable = false)
    private Instant submittedAt = Instant.now();

    public FeedbackEntity(UUID id, String memberId, String providerName, Integer rating, String comment, Instant submittedAt) {
        this.id = id;
        this.memberId = memberId;
        this.providerName = providerName;
        this.rating = rating;
        this.comment = comment;
        this.submittedAt = submittedAt;
    }

    public FeedbackEntity() {}

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Instant getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(Instant submittedAt) {
        this.submittedAt = submittedAt;
    }
}
