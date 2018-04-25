package com.hackag.fibimeter.dto;

import java.util.List;

/**
 * @author Jakub Matus
 */
public class PeersDto {

    public static class FeedbackRoundPeersDto {

        private long feedbackRoundId;
        private EmployeeDto manager;
        private PeersStringOnlyDto peers;

        public FeedbackRoundPeersDto() {
        }

        public FeedbackRoundPeersDto(long feedbackRoundId, EmployeeDto manager, PeersStringOnlyDto peers) {
            this.feedbackRoundId = feedbackRoundId;
            this.manager = manager;
            this.peers = peers;
        }

        public long getFeedbackRoundId() {
            return feedbackRoundId;
        }

        public void setFeedbackRoundId(long feedbackRoundId) {
            this.feedbackRoundId = feedbackRoundId;
        }

        public EmployeeDto getManager() {
            return manager;
        }

        public void setManager(EmployeeDto manager) {
            this.manager = manager;
        }

        public PeersStringOnlyDto getPeers() {
            return peers;
        }

        public void setPeers(PeersStringOnlyDto peers) {
            this.peers = peers;
        }
    }

    private List<FeedbackRoundPeersDto> peers;

    public PeersDto(List<FeedbackRoundPeersDto> peers) {
        this.peers = peers;
    }

    public List<FeedbackRoundPeersDto> getPeers() {
        return peers;
    }

    /**
     * @param peers the peers to set
     * @see #peers
     */
    public void setPeers(List<FeedbackRoundPeersDto> peers) {
        this.peers = peers;
    }
}
