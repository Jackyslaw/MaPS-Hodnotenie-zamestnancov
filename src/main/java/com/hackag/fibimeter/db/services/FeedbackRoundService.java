package com.hackag.fibimeter.db.services;

import com.hackag.fibimeter.dto.*;

import java.util.Set;

public interface FeedbackRoundService {

    boolean acceptPeersReview(long feedbackRoundId);

    PeersDto getPeersRequiringReviewAcceptance();

    boolean rejectPeersReview(long feedbackRoundId, String reason);

    boolean startFeedbackRound(FeedbackStartDto feedbackStartDto);

    Set<FeedbackRoundDto> getDtosForManager(String email);

    boolean selectPeers(String managerEmail, long feedbackRoundId, PeersStringOnlyDto selectionPeerEmails);

    Set<FeedbackRoundStatsDto> getAllStatsDto();
}
