package com.hackag.fibimeter.db.model;

import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idFeedback;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Employee manager;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private FeedbackRound feedbackRound;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Question question;
    private int answer;

    public Feedback() {
    }

    public Feedback(Employee manager, FeedbackRound feedbackRound, Question question, int answer) {
        this.manager = manager;
        this.feedbackRound = feedbackRound;
        this.question = question;
        this.answer = answer;
    }

    public Long getIdFeedback() {
        return idFeedback;
    }

    public void setIdFeedback(Long idFeedback) {
        this.idFeedback = idFeedback;
    }

    public Employee getManager() {
        return manager;
    }

    public void setManager(Employee manager) {
        this.manager = manager;
    }

    public FeedbackRound getFeedbackRound() {
        return feedbackRound;
    }

    public void setFeedbackRound(FeedbackRound feedbackRound) {
        this.feedbackRound = feedbackRound;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public int getAnswer() {
        return answer;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }

    @Override
    public boolean equals(Object o) {
        Assert.notNull(idFeedback, "The business key should always be defined");
        if (this == o) return true;
        if (!(o instanceof Feedback)) return false;
        Feedback feedback = (Feedback) o;
        return Objects.equals(idFeedback, feedback.idFeedback);
    }

    @Override
    public int hashCode() {
        if (idFeedback == null) {
            LoggerFactory.getLogger(Feedback.class).error("The business key should always be defined");
        }
        return Objects.hash(idFeedback);
    }
}
