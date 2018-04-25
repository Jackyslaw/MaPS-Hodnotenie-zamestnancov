package com.hackag.fibimeter.dto;

import java.util.Objects;
import java.util.Set;

public class AnswerListDto {

    public static class AnswerDto {

        private long questionId;
        private int answer;

        public AnswerDto() {
        }

        public AnswerDto(long questionId, int answer) {
            this.questionId = questionId;
            this.answer = answer;
        }

        public long getQuestionId() {
            return questionId;
        }

        public void setQuestionId(long questionId) {
            this.questionId = questionId;
        }

        public int getAnswer() {
            return answer;
        }

        public void setAnswer(int answer) {
            this.answer = answer;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof AnswerDto)) return false;
            AnswerDto answerDto = (AnswerDto) o;
            return questionId == answerDto.questionId &&
                answer == answerDto.answer;
        }

        @Override
        public int hashCode() {
            return Objects.hash(questionId, answer);
        }
    }

    private long managerId;
    private Set<AnswerDto> answers;

    public AnswerListDto() {
    }

    public AnswerListDto(long managerId, Set<AnswerDto> answers) {
        this.managerId = managerId;
        this.answers = answers;
    }

    public long getManagerId() {
        return managerId;
    }

    public void setManagerId(long managerId) {
        this.managerId = managerId;
    }

    public Set<AnswerDto> getAnswers() {
        return answers;
    }

    public void setAnswers(Set<AnswerDto> answers) {
        this.answers = answers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AnswerListDto)) return false;
        AnswerListDto that = (AnswerListDto) o;
        return managerId == that.managerId &&
            Objects.equals(answers, that.answers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(managerId, answers);
    }
}
