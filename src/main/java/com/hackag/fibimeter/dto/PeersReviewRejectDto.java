package com.hackag.fibimeter.dto;

public class PeersReviewRejectDto {

    private String reason;

    public PeersReviewRejectDto() {
    }

    public PeersReviewRejectDto(String reason) {
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
