package com.hackag.fibimeter.dto;

import com.hackag.fibimeter.db.model.FeedbackRound;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

public class FeedbackRoundStatsDto {

    public static class ManagerPreviewDto {

        private long idManager;
        private String firstName;
        private String surname;

        public ManagerPreviewDto() {
        }

        public ManagerPreviewDto(long idManager, String firstName, String surname) {
            this.idManager = idManager;
            this.firstName = firstName;
            this.surname = surname;
        }

        public long getIdManager() {
            return idManager;
        }

        public void setIdManager(long idManager) {
            this.idManager = idManager;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getSurname() {
            return surname;
        }

        public void setSurname(String surname) {
            this.surname = surname;
        }
    }

    private long idFeedbackRound;
    private LocalDateTime startDate;
    private LocalDateTime finishDate;
    private boolean started;
    private boolean finished;
    private ManagerPreviewDto manager;
    private Set<FeedbackDto> feedbacks;

    public FeedbackRoundStatsDto() {
    }

    public FeedbackRoundStatsDto(long idFeedbackRound, LocalDateTime startDate, LocalDateTime finishDate, boolean started, boolean finished, ManagerPreviewDto manager, Set<FeedbackDto> feedbacks) {
        this.idFeedbackRound = idFeedbackRound;
        this.startDate = startDate;
        this.finishDate = finishDate;
        this.started = started;
        this.finished = finished;
        this.manager = manager;
        this.feedbacks = feedbacks;
    }

    public long getIdFeedbackRound() {
        return idFeedbackRound;
    }

    public void setIdFeedbackRound(long idFeedbackRound) {
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

    public ManagerPreviewDto getManager() {
        return manager;
    }

    public void setManager(ManagerPreviewDto manager) {
        this.manager = manager;
    }

    public Set<FeedbackDto> getFeedbacks() {
        return feedbacks;
    }

    public void setFeedbacks(Set<FeedbackDto> feedbacks) {
        this.feedbacks = feedbacks;
    }

    public static FeedbackRoundStatsDto mapFeedbackRound(FeedbackRound feedbackRound) {
        return new FeedbackRoundStatsDto(
            feedbackRound.getIdFeedbackRound(),
            feedbackRound.getStartDate(),
            feedbackRound.getFinishDate(),
            feedbackRound.isStarted(),
            feedbackRound.isFinished(),
            new ManagerPreviewDto(
                feedbackRound.getManager().getIdEmployee(),
                feedbackRound.getManager().getFirstName(),
                feedbackRound.getManager().getSurname()
            ),
            feedbackRound.getFeedbacks().stream().map(FeedbackDto::mapFeedback).collect(Collectors.toSet())
        );
    }
}
