package com.hackag.fibimeter.dto;

/**
 * @author Jakub Matus
 */
public class FeedbackStartDto {

    private long managerId;
    private long dateStart;
    private long dateEnd;
    private QuestionListDto editedQuestions;
    private PeersStringOnlyDto adminSelectedPeers;
    private boolean managerCanAndShouldSelectPeers;

    public FeedbackStartDto() {
    }

    public FeedbackStartDto(long managerId, long dateStart, long dateEnd, QuestionListDto editedQuestions, PeersStringOnlyDto adminSelectedPeers, boolean managerCanAndShouldSelectPeers) {
        this.managerId = managerId;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.editedQuestions = editedQuestions;
        this.adminSelectedPeers = adminSelectedPeers;
        this.managerCanAndShouldSelectPeers = managerCanAndShouldSelectPeers;
    }

    public long getManagerId() {
        return managerId;
    }

    public void setManagerId(long managerId) {
        this.managerId = managerId;
    }

    public long getDateStart() {
        return dateStart;
    }

    public void setDateStart(long dateStart) {
        this.dateStart = dateStart;
    }

    public long getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(long dateEnd) {
        this.dateEnd = dateEnd;
    }

    public QuestionListDto getEditedQuestions() {
        return editedQuestions;
    }

    public void setEditedQuestions(QuestionListDto editedQuestions) {
        this.editedQuestions = editedQuestions;
    }

    public PeersStringOnlyDto getAdminSelectedPeers() {
        return adminSelectedPeers;
    }

    public void setAdminSelectedPeers(PeersStringOnlyDto adminSelectedPeers) {
        this.adminSelectedPeers = adminSelectedPeers;
    }

    public boolean isManagerCanAndShouldSelectPeers() {
        return managerCanAndShouldSelectPeers;
    }

    public void setManagerCanAndShouldSelectPeers(boolean managerCanAndShouldSelectPeers) {
        this.managerCanAndShouldSelectPeers = managerCanAndShouldSelectPeers;
    }
}
