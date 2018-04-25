package com.hackag.fibimeter.db.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class QuestionCategory {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idQuestionCategory;
    @Column(unique = true)
    private String categoryName;
    private String description;
    @JsonIgnore
    @OneToMany(mappedBy = "questionCategory", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Fetch(value = FetchMode.SUBSELECT)
    @NotNull
    private Set<Question> questions = new HashSet<>();

    public QuestionCategory() {
    }

    public QuestionCategory(String categoryName, String description, Set<Question> questions) {
        this.categoryName = categoryName;
        this.description = description;
        this.questions = questions;
    }

    public Long getIdQuestionCategory() {
        return idQuestionCategory;
    }

    public void setIdQuestionCategory(Long idQuestionCategory) {
        this.idQuestionCategory = idQuestionCategory;
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

    public Set<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(Set<Question> questions) {
        this.questions = questions;
    }

    @Override
    public boolean equals(Object o) {
        Assert.notNull(idQuestionCategory, "The business key should always be defined");
        if (this == o) return true;
        if (!(o instanceof QuestionCategory)) return false;
        QuestionCategory that = (QuestionCategory) o;
        return Objects.equals(idQuestionCategory, that.idQuestionCategory);
    }

    @Override
    public int hashCode() {
        if (idQuestionCategory == null) {
            LoggerFactory.getLogger(QuestionCategory.class).error("The business key should always be defined");
        }
        return Objects.hash(idQuestionCategory);
    }
}
