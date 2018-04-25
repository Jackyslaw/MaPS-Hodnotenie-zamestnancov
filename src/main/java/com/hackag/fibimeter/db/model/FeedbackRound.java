package com.hackag.fibimeter.db.model;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@Entity
public class FeedbackRound {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idFeedbackRound;
    private LocalDateTime startDate;
    private LocalDateTime finishDate;
    private boolean started;
    private boolean finished;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Employee manager;
    @OneToMany(mappedBy = "feedbackRound", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Fetch(value = FetchMode.SUBSELECT)
    @NotNull
    private Set<Feedback> feedbacks;
    @OneToMany(mappedBy = "feedbackRound", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Fetch(value = FetchMode.SUBSELECT)
    @NotNull
    private Set<FeedbackAccess> feedbackAccesses;
    @OneToMany(mappedBy = "feedbackRound", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Fetch(value = FetchMode.SUBSELECT)
    @NotNull
    private Set<FeedbackRoundQuestion> feedbackRoundQuestions;
    // Manager's own selected peers that are supposed to be accepted by an admin
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @NotNull
    private Set<Employee> adminSelectedPeers;
    private boolean managerCanAndShouldSelectPeers;
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @NotNull
    private Set<Employee> peers;
    // True if admin has accepted adminSelectedPeers
    private boolean acceptedPeers;
    private String rejectedPeersReason;
    private String pdfReportPath;

    public FeedbackRound() {
    }

    public FeedbackRound(LocalDateTime startDate, LocalDateTime finishDate, boolean started, boolean finished, Employee manager, Set<Feedback> feedbacks, Set<FeedbackAccess> feedbackAccesses, Set<FeedbackRoundQuestion> feedbackRoundQuestions, Set<Employee> adminSelectedPeers, boolean managerCanAndShouldSelectPeers, Set<Employee> peers, boolean acceptedPeers, String rejectedPeersReason, String pdfReportPath) {
        this.startDate = startDate;
        this.finishDate = finishDate;
        this.started = started;
        this.finished = finished;
        this.manager = manager;
        this.feedbacks = feedbacks;
        this.feedbackAccesses = feedbackAccesses;
        this.feedbackRoundQuestions = feedbackRoundQuestions;
        this.adminSelectedPeers = adminSelectedPeers;
        this.managerCanAndShouldSelectPeers = managerCanAndShouldSelectPeers;
        this.peers = peers;
        this.acceptedPeers = acceptedPeers;
        this.rejectedPeersReason = rejectedPeersReason;
        this.pdfReportPath = pdfReportPath;
    }

    public Long getIdFeedbackRound() {
        return idFeedbackRound;
    }

    public void setIdFeedbackRound(Long idFeedbackRound) {
        this.idFeedbackRound = idFeedbackRound;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(LocalDateTime finishDate) {
        this.finishDate = finishDate;
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public Employee getManager() {
        return manager;
    }

    public void setManager(Employee manager) {
        this.manager = manager;
    }

    public Set<Feedback> getFeedbacks() {
        return feedbacks;
    }

    public void setFeedbacks(Set<Feedback> feedbacks) {
        this.feedbacks = feedbacks;
    }

    public Set<FeedbackAccess> getFeedbackAccesses() {
        return feedbackAccesses;
    }

    public void setFeedbackAccesses(Set<FeedbackAccess> feedbackAccesses) {
        this.feedbackAccesses = feedbackAccesses;
    }

    public Set<FeedbackRoundQuestion> getFeedbackRoundQuestions() {
        return feedbackRoundQuestions;
    }

    public void setFeedbackRoundQuestions(Set<FeedbackRoundQuestion> feedbackRoundQuestions) {
        this.feedbackRoundQuestions = feedbackRoundQuestions;
    }

    public Set<Employee> getAdminSelectedPeers() {
        return adminSelectedPeers;
    }

    public void setAdminSelectedPeers(Set<Employee> adminSelectedPeers) {
        this.adminSelectedPeers = adminSelectedPeers;
    }

    public boolean isManagerCanAndShouldSelectPeers() {
        return managerCanAndShouldSelectPeers;
    }

    public void setManagerCanAndShouldSelectPeers(boolean managerCanAndShouldSelectPeers) {
        this.managerCanAndShouldSelectPeers = managerCanAndShouldSelectPeers;
    }

    public Set<Employee> getPeers() {
        return peers;
    }

    public void setPeers(Set<Employee> peers) {
        this.peers = peers;
    }

    public boolean isAcceptedPeers() {
        return acceptedPeers;
    }

    public void setAcceptedPeers(boolean acceptedPeers) {
        this.acceptedPeers = acceptedPeers;
    }

    public String getRejectedPeersReason() {
        return rejectedPeersReason;
    }

    public void setRejectedPeersReason(String rejectedPeersReason) {
        this.rejectedPeersReason = rejectedPeersReason;
    }

    public String getPdfReportPath() {
        return pdfReportPath;
    }

    public void setPdfReportPath(String pdfReportPath) {
        this.pdfReportPath = pdfReportPath;
    }

    @Override
    public boolean equals(Object o) {
        Assert.notNull(idFeedbackRound, "The business key should always be defined");
        if (this == o) return true;
        if (!(o instanceof FeedbackRound)) return false;
        FeedbackRound that = (FeedbackRound) o;
        return Objects.equals(idFeedbackRound, that.idFeedbackRound);
    }

    @Override
    public int hashCode() {
        if (idFeedbackRound == null) {
            LoggerFactory.getLogger(FeedbackRound.class).error("The business key should always be defined");
        }
        return Objects.hash(idFeedbackRound);
    }
}
