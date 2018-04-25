package com.hackag.fibimeter.dto;

import com.hackag.fibimeter.db.model.Question;
import com.hackag.fibimeter.dto.enumeration.QuestionStateChange;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class QuestionListDto {

    public static class QuestionDto {

        public static class QuestionTextDto {

            private String self;
            private String underman;
            private String shipmate;
            private String chief;

            public QuestionTextDto() {
            }

            public QuestionTextDto(String self, String underman, String shipmate, String chief) {
                this.self = self;
                this.underman = underman;
                this.shipmate = shipmate;
                this.chief = chief;
            }

            public String getSelf() {
                return self;
            }

            public void setSelf(String self) {
                this.self = self;
            }

            public String getUnderman() {
                return underman;
            }

            public void setUnderman(String underman) {
                this.underman = underman;
            }

            public String getShipmate() {
                return shipmate;
            }

            public void setShipmate(String shipmate) {
                this.shipmate = shipmate;
            }

            public String getChief() {
                return chief;
            }

            public void setChief(String chief) {
                this.chief = chief;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (!(o instanceof QuestionTextDto)) return false;
                QuestionTextDto that = (QuestionTextDto) o;
                return Objects.equals(self, that.self) &&
                    Objects.equals(underman, that.underman) &&
                    Objects.equals(shipmate, that.shipmate) &&
                    Objects.equals(chief, that.chief);
            }

            @Override
            public int hashCode() {
                return Objects.hash(self, underman, shipmate, chief);
            }

            @Override
            public String toString() {
                final StringBuilder sb = new StringBuilder("QuestionTextDto{");
                sb.append("self='").append(self).append('\'');
                sb.append(", underman='").append(underman).append('\'');
                sb.append(", shipmate='").append(shipmate).append('\'');
                sb.append(", chief='").append(chief).append('\'');
                sb.append('}');
                return sb.toString();
            }
        }

        private Long id;
        private String title;
        private QuestionTextDto text;
        private Long questionCategoryId;
        private QuestionStateChange stateChange;

        public QuestionDto() {
        }

        public QuestionDto(Long id, String title, QuestionTextDto text, Long questionCategoryId, QuestionStateChange stateChange) {
            this.id = id;
            this.title = title;
            this.text = text;
            this.questionCategoryId = questionCategoryId;
            this.stateChange = stateChange;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public QuestionTextDto getText() {
            return text;
        }

        public void setText(QuestionTextDto text) {
            this.text = text;
        }

        public Long getQuestionCategoryId() {
            return questionCategoryId;
        }

        public void setQuestionCategoryId(Long questionCategoryId) {
            this.questionCategoryId = questionCategoryId;
        }

        public QuestionStateChange getStateChange() {
            return stateChange;
        }

        public void setStateChange(QuestionStateChange stateChange) {
            this.stateChange = stateChange;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof QuestionDto)) return false;
            QuestionDto that = (QuestionDto) o;
            return Objects.equals(id, that.id) &&
                Objects.equals(title, that.title) &&
                Objects.equals(text, that.text) &&
                Objects.equals(questionCategoryId, that.questionCategoryId) &&
                stateChange == that.stateChange;
        }

        @Override
        public String toString() {
            return "QuestionDto{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", text=" + text +
                ", questionCategoryId=" + questionCategoryId +
                ", stateChange=" + stateChange +
                '}';
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, title, text, questionCategoryId, stateChange);
        }

        public static QuestionListDto.QuestionDto mapQuestion(Question question) {
            return new QuestionListDto.QuestionDto(
                question.getIdQuestion(),
                question.getTitle(),
                new QuestionListDto.QuestionDto.QuestionTextDto(
                    question.getTextSelf(), question.getTextUnderman(), question.getTextShipmate(), question.getTextChief()
                ),
                question.getQuestionCategory().getIdQuestionCategory(),
                // If it was in DB, it's current
                QuestionStateChange.CURRENT
            );
        }
    }

    private List<QuestionDto> lower = new ArrayList<>();
    private List<QuestionDto> middle = new ArrayList<>();
    private List<QuestionDto> higher = new ArrayList<>();

    public QuestionListDto() {
    }

    public QuestionListDto(List<QuestionDto> lower, List<QuestionDto> middle, List<QuestionDto> higher) {
        this.lower = lower;
        this.middle = middle;
        this.higher = higher;
    }

    public List<QuestionDto> getLower() {
        return lower;
    }

    public void setLower(List<QuestionDto> lower) {
        this.lower = lower;
    }

    public List<QuestionDto> getMiddle() {
        return middle;
    }

    public void setMiddle(List<QuestionDto> middle) {
        this.middle = middle;
    }

    public List<QuestionDto> getHigher() {
        return higher;
    }

    public void setHigher(List<QuestionDto> higher) {
        this.higher = higher;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof QuestionListDto)) return false;
        QuestionListDto that = (QuestionListDto) o;
        return Objects.equals(lower, that.lower) &&
            Objects.equals(middle, that.middle) &&
            Objects.equals(higher, that.higher);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lower, middle, higher);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("QuestionListDto{");
        sb.append("lower=").append(lower);
        sb.append(", middle=").append(middle);
        sb.append(", higher=").append(higher);
        sb.append('}');
        return sb.toString();
    }
}
