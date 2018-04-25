package com.hackag.fibimeter.background;

import com.hackag.fibimeter.db.model.FeedbackRound;
import com.hackag.fibimeter.db.repository.FeedbackRoundRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

@Component
public class FeedbackRoundManagement {

    private static Logger log = LoggerFactory.getLogger(FeedbackRoundManagement.class);

    @Autowired
    private FeedbackRoundRepository feedbackRoundRepository;

    @Scheduled(fixedDelay = 5000)
    private void startOrStopFeedbackRounds() {
        log.trace("Checking scheduled feedback rounds to start or stop...");
        List<FeedbackRound> feedbackRounds = feedbackRoundRepository.findByStartedFalseOrFinishedFalse();
        for (FeedbackRound feedbackRound : feedbackRounds) {
            if (!feedbackRound.isStarted()
                && feedbackRound.getStartDate().toInstant(ZoneOffset.UTC).isBefore(ZonedDateTime.now().toInstant())) {
                log.info("Feedback round id " + feedbackRound.getIdFeedbackRound() + " set started on scheduled time");
                feedbackRound.setStarted(true);
                feedbackRoundRepository.save(feedbackRound);
            } else if (feedbackRound.isStarted()
                && !feedbackRound.isFinished()
                && feedbackRound.getFinishDate().toInstant(ZoneOffset.UTC).isBefore(ZonedDateTime.now().toInstant())) {
                log.info("Feedback round id " + feedbackRound.getIdFeedbackRound() + " set finished on scheduled time");
                feedbackRound.setFinished(true);
                feedbackRoundRepository.save(feedbackRound);
            }
        }
    }
}
