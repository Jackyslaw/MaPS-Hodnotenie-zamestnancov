package com.hackag.fibimeter.dto;

import com.hackag.fibimeter.db.model.Feedback;

public class FeedbackDto {

    public static class FeedbackQuestionDto {

        private String title;
        private String category;

        public FeedbackQuestionDto() {
        }

        public FeedbackQuestionDto(String title, String category) {
            this.title = title;
            this.category = category;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }
    }

    private FeedbackQuestionDto question;
    private int answer;

    public FeedbackDto() {
    }

    public FeedbackDto(FeedbackQuestionDto question, int answer) {
        this.question = question;
        this.answer = answer;
    }

    public FeedbackQuestionDto getQuestion() {
        return question;
    }

    public void setQuestion(FeedbackQuestionDto question) {
        this.question = question;
    }

    public int getAnswer() {
        return answer;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }

    public static FeedbackDto mapFeedback(Feedback feedback) {
        return new FeedbackDto(
            new FeedbackQuestionDto(
                feedback.getQuestion().getTitle(),
                feedback.getQuestion().getQuestionCategory().getCategoryName()
            ),
            feedback.getAnswer()
        );
    }
}
