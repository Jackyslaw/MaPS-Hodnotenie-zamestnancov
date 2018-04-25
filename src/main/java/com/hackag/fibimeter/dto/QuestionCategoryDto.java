package com.hackag.fibimeter.dto;

import com.hackag.fibimeter.db.model.QuestionCategory;

import java.util.Objects;

public class QuestionCategoryDto {

    private long questionCategoryId;
    private String categoryName;
    private String description;

    public QuestionCategoryDto() {
    }

    public QuestionCategoryDto(long questionCategoryId, String categoryName, String description) {
        this.questionCategoryId = questionCategoryId;
        this.categoryName = categoryName;
        this.description = description;
    }

    public long getQuestionCategoryId() {
        return questionCategoryId;
    }

    public void setQuestionCategoryId(long questionCategoryId) {
        this.questionCategoryId = questionCategoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof QuestionCategoryDto)) return false;
        QuestionCategoryDto that = (QuestionCategoryDto) o;
        return questionCategoryId == that.questionCategoryId &&
            Objects.equals(categoryName, that.categoryName) &&
            Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(questionCategoryId, categoryName, description);
    }

    @Override
    public String toString() {
        return "QuestionCategoryDto{" +
            "questionCategoryId=" + questionCategoryId +
            ", categoryName='" + categoryName + '\'' +
            ", description='" + description + '\'' +
            '}';
    }

    public static QuestionCategoryDto mapQuestionCategory(QuestionCategory questionCategory) {
        return new QuestionCategoryDto(
            questionCategory.getIdQuestionCategory(),
            questionCategory.getCategoryName(),
            questionCategory.getDescription()
        );
    }
}
