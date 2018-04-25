package com.hackag.fibimeter.dto;

public class FeedbackQuestionDto {

    private String text;
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public FeedbackQuestionDto() {
    }

    public FeedbackQuestionDto(long id, String text) {
        this.text = text;
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
