package com.hackag.fibimeter.db.model;

import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.util.Objects;

/**
 * Joining table that defines {@link Question}s available to a {@link FeedbackRound}.
 */
@Entity
public class FeedbackRoundQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idFeedbackQuestion;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private FeedbackRound feedbackRound;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Question question;

    public FeedbackRoundQuestion() {
    }

    public FeedbackRoundQuestion(FeedbackRound feedbackRound, Question question) {
        this.feedbackRound = feedbackRound;
        this.question = question;
    }

    public Long getIdFeedbackQuestion() {
        return idFeedbackQuestion;
    }

    public void setIdFeedbackQuestion(Long idFeedbackQuestion) {
        this.idFeedbackQuestion = idFeedbackQuestion;
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

    @Override
    public boolean equals(Object o) {
        Assert.notNull(idFeedbackQuestion, "The business key should always be defined");
        if (this == o) return true;
        if (!(o instanceof FeedbackRoundQuestion)) return false;
        FeedbackRoundQuestion that = (FeedbackRoundQuestion) o;
        return Objects.equals(idFeedbackQuestion, that.idFeedbackQuestion);
    }

    @Override
    public int hashCode() {
        if (idFeedbackQuestion == null) {
            LoggerFactory.getLogger(FeedbackRoundQuestion.class).error("The business key should always be defined");
        }
        return Objects.hash(idFeedbackQuestion);
    }
}
