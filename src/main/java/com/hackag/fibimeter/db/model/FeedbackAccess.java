package com.hackag.fibimeter.db.model;

import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class FeedbackAccess {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idFeedbackAccess;
    @ManyToOne(fetch = FetchType.LAZY)
    private FeedbackRound feedbackRound;
    @ManyToOne
    private Employee employee;

    public FeedbackAccess() {
    }

    public FeedbackAccess(FeedbackRound feedbackRound, Employee employee) {
        this.feedbackRound = feedbackRound;
        this.employee = employee;
    }

    public Long getIdFeedbackAccess() {
        return idFeedbackAccess;
    }

    public void setIdFeedbackAccess(Long idFeedbackAccess) {
        this.idFeedbackAccess = idFeedbackAccess;
    }

    public FeedbackRound getFeedbackRound() {
        return feedbackRound;
    }

    public void setFeedbackRound(FeedbackRound feedbackRound) {
        this.feedbackRound = feedbackRound;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    @Override
    public boolean equals(Object o) {
        Assert.notNull(idFeedbackAccess, "The business key should always be defined");
        if (this == o) return true;
        if (!(o instanceof FeedbackAccess)) return false;
        FeedbackAccess that = (FeedbackAccess) o;
        return Objects.equals(idFeedbackAccess, that.idFeedbackAccess);
    }

    @Override
    public int hashCode() {
        if (idFeedbackAccess == null) {
            LoggerFactory.getLogger(FeedbackAccess.class).error("The business key should always be defined");
        }
        return Objects.hash(idFeedbackAccess);
    }
}
