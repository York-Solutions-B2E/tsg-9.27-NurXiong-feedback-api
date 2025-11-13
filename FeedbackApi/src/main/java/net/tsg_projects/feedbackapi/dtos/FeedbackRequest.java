package net.tsg_projects.feedbackapi.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class FeedbackRequest {

    @NotBlank
    @Size(max = 36)
    private String memberId;

    @NotBlank
    @Size(max = 80)
    private String providerName;

    @Min(1)
    @Max(5)
    private int rating;

    @NotBlank
    @Size(max = 200)
    private String comment;


    public FeedbackRequest(String memberId, String providerName, int rating, String comment) {
        this.memberId = memberId;
        this.providerName = providerName;
        this.rating = rating;
        this.comment = comment;
    }

    public FeedbackRequest(){}

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

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
