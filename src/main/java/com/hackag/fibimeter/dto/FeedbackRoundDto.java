package com.hackag.fibimeter.dto;

import com.hackag.fibimeter.db.model.Employee;
import com.hackag.fibimeter.db.model.FeedbackRound;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class FeedbackRoundDto {

    private Long idFeedbackRound;
    private LocalDateTime startDate;
    private LocalDateTime finishDate;
    private Long idManager;
    private Set<String> adminSelectedPeerEmails;
    private boolean managerCanAndShouldSelectPeers;
    private Set<String> peerEmails;
    private boolean acceptedPeers;
    private String rejectedPeersReason;
    private String pdfReportPath;

    public FeedbackRoundDto() {
    }

    public FeedbackRoundDto(Long idFeedbackRound, LocalDateTime startDate, LocalDateTime finishDate, Long idManager, Set<String> adminSelectedPeerEmails, boolean managerCanAndShouldSelectPeers, Set<String> peerEmails, boolean acceptedPeers, String rejectedPeersReason, String pdfReportPath) {
        this.idFeedbackRound = idFeedbackRound;
        this.startDate = startDate;
        this.finishDate = finishDate;
        this.idManager = idManager;
        this.adminSelectedPeerEmails = adminSelectedPeerEmails;
        this.managerCanAndShouldSelectPeers = managerCanAndShouldSelectPeers;
        this.peerEmails = peerEmails;
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

    public Long getIdManager() {
        return idManager;
    }

    public void setIdManager(Long idManager) {
        this.idManager = idManager;
    }

    public Set<String> getAdminSelectedPeerEmails() {
        return adminSelectedPeerEmails;
    }

    public void setAdminSelectedPeerEmails(Set<String> adminSelectedPeerEmails) {
        this.adminSelectedPeerEmails = adminSelectedPeerEmails;
    }

    public boolean isManagerCanAndShouldSelectPeers() {
        return managerCanAndShouldSelectPeers;
    }

    public void setManagerCanAndShouldSelectPeers(boolean managerCanAndShouldSelectPeers) {
        this.managerCanAndShouldSelectPeers = managerCanAndShouldSelectPeers;
    }

    public Set<String> getPeerEmails() {
        return peerEmails;
    }

    public void setPeerEmails(Set<String> peerEmails) {
        this.peerEmails = peerEmails;
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
        if (this == o) return true;
        if (!(o instanceof FeedbackRoundDto)) return false;
        FeedbackRoundDto that = (FeedbackRoundDto) o;
        return managerCanAndShouldSelectPeers == that.managerCanAndShouldSelectPeers &&
            acceptedPeers == that.acceptedPeers &&
            Objects.equals(idFeedbackRound, that.idFeedbackRound) &&
            Objects.equals(startDate, that.startDate) &&
            Objects.equals(finishDate, that.finishDate) &&
            Objects.equals(idManager, that.idManager) &&
            Objects.equals(adminSelectedPeerEmails, that.adminSelectedPeerEmails) &&
            Objects.equals(peerEmails, that.peerEmails) &&
            Objects.equals(rejectedPeersReason, that.rejectedPeersReason) &&
            Objects.equals(pdfReportPath, that.pdfReportPath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idFeedbackRound, startDate, finishDate, idManager, adminSelectedPeerEmails, managerCanAndShouldSelectPeers, peerEmails, acceptedPeers, rejectedPeersReason, pdfReportPath);
    }

    @Override
    public String toString() {
        return "FeedbackRoundDto{" +
            "idFeedbackRound=" + idFeedbackRound +
            ", startDate=" + startDate +
            ", finishDate=" + finishDate +
            ", idManager=" + idManager +
            ", adminSelectedPeerEmails=" + adminSelectedPeerEmails +
            ", managerCanAndShouldSelectPeers=" + managerCanAndShouldSelectPeers +
            ", peerEmails=" + peerEmails +
            ", acceptedPeers=" + acceptedPeers +
            ", rejectedPeersReason='" + rejectedPeersReason + '\'' +
            ", pdfReportPath='" + pdfReportPath + '\'' +
            '}';
    }

    public static FeedbackRoundDto mapFeedbackRound(FeedbackRound feedbackRound) {
        return new FeedbackRoundDto(
            feedbackRound.getIdFeedbackRound(),
            feedbackRound.getStartDate(),
            feedbackRound.getFinishDate(),
            feedbackRound.getManager().getIdEmployee(),
            feedbackRound.getAdminSelectedPeers().stream().map(Employee::getEmailAddress).collect(Collectors.toSet()),
            feedbackRound.isManagerCanAndShouldSelectPeers(),
            feedbackRound.getPeers().stream().map(Employee::getEmailAddress).collect(Collectors.toSet()),
            feedbackRound.isAcceptedPeers(),
            feedbackRound.getRejectedPeersReason(),
            feedbackRound.getPdfReportPath()
        );
    }
}
