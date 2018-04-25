package com.hackag.fibimeter.db.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hackag.fibimeter.db.model.enumeration.QuestionTarget;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idQuestion;
    private String title;
    private String textSelf;
    private String textUnderman;
    private String textShipmate;
    private String textChief;
    private QuestionTarget questionTarget;
    private boolean valid;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private QuestionCategory questionCategory;
    @JsonIgnore
    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Fetch(value = FetchMode.SUBSELECT)
    private Set<FeedbackRoundQuestion> feedbackRoundQuestions = new HashSet<>();
    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Fetch(value = FetchMode.SUBSELECT)
    private Set<Feedback> feedbacks = new HashSet<>();

    public Question() {
    }

    public Question(String title, String textSelf, String textUnderman, String textShipmate, String textChief, QuestionTarget questionTarget, boolean valid, QuestionCategory questionCategory, Set<FeedbackRoundQuestion> feedbackRoundQuestions, Set<Feedback> feedbacks) {
        this.title = title;
        this.textSelf = textSelf;
        this.textUnderman = textUnderman;
        this.textShipmate = textShipmate;
        this.textChief = textChief;
        this.questionTarget = questionTarget;
        this.valid = valid;
        this.questionCategory = questionCategory;
        this.feedbackRoundQuestions = feedbackRoundQuestions;
        this.feedbacks = feedbacks;
    }

    public Long getIdQuestion() {
        return idQuestion;
    }

    public void setIdQuestion(Long idQuestion) {
        this.idQuestion = idQuestion;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTextSelf() {
        return textSelf;
    }

    public void setTextSelf(String textSelf) {
        this.textSelf = textSelf;
    }

    public String getTextUnderman() {
        return textUnderman;
    }

    public void setTextUnderman(String textUnderman) {
        this.textUnderman = textUnderman;
    }

    public String getTextShipmate() {
        return textShipmate;
    }

    public void setTextShipmate(String textShipmate) {
        this.textShipmate = textShipmate;
    }

    public String getTextChief() {
        return textChief;
    }

    public void setTextChief(String textChief) {
        this.textChief = textChief;
    }

    public QuestionTarget getQuestionTarget() {
        return questionTarget;
    }

    public void setQuestionTarget(QuestionTarget questionTarget) {
        this.questionTarget = questionTarget;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public QuestionCategory getQuestionCategory() {
        return questionCategory;
    }

    public void setQuestionCategory(QuestionCategory questionCategory) {
        this.questionCategory = questionCategory;
    }

    public Set<FeedbackRoundQuestion> getFeedbackRoundQuestions() {
        return feedbackRoundQuestions;
    }

    public void setFeedbackRoundQuestions(Set<FeedbackRoundQuestion> feedbackRoundQuestions) {
        this.feedbackRoundQuestions = feedbackRoundQuestions;
    }

    public Set<Feedback> getFeedbacks() {
        return feedbacks;
    }

    public void setFeedbacks(Set<Feedback> feedbacks) {
        this.feedbacks = feedbacks;
    }

    @Override
    public boolean equals(Object o) {
        Assert.notNull(idQuestion, "The business key should always be defined");
        if (this == o) return true;
        if (!(o instanceof Question)) return false;
        Question question = (Question) o;
        return Objects.equals(idQuestion, question.idQuestion);
    }

    @Override
    public int hashCode() {
        if (idQuestion == null) {
            LoggerFactory.getLogger(Question.class).error("The business key should always be defined");
        }
        return Objects.hash(idQuestion);
    }
}
