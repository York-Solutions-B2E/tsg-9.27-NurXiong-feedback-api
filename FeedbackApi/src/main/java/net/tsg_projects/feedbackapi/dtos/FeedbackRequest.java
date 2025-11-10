package net.tsg_projects.feedbackapi.dtos;

public class FeedbackRequest {
    private String memberId;
    private String providerName;
    private int rating;
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
