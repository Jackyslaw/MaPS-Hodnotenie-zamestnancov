package com.hackag.fibimeter.db.services.impl;

import com.hackag.fibimeter.db.model.*;
import com.hackag.fibimeter.db.model.enumeration.QuestionTarget;
import com.hackag.fibimeter.db.repository.*;
import com.hackag.fibimeter.db.services.EmailService;
import com.hackag.fibimeter.db.services.FeedbackRoundService;
import com.hackag.fibimeter.db.services.QuestionService;
import com.hackag.fibimeter.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

@Service
public class FeedbackRoundServiceImpl implements FeedbackRoundService {

    private static Logger log = LoggerFactory.getLogger(FeedbackRoundServiceImpl.class);

    @Autowired
    private FeedbackRoundRepository feedbackRoundRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private FeedbackQuestionRepository feedbackQuestionRepository;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private FeedbackAccessRepository feedbackAccessRepository;
    @Autowired
    private OrgUnitRepository orgUnitRepository;
    @Autowired
    private EmailService emailService;

    @Override
    public boolean startFeedbackRound(FeedbackStartDto feedbackStartDto) {
        Set<FeedbackRoundQuestion> feedbackRoundQuestions =
            getFeedbackQuestionsFromEditedQuestions(feedbackStartDto.getEditedQuestions());

        List<String> adminSelectedPeers = feedbackStartDto.getAdminSelectedPeers().getPeers();
        FeedbackRound feedbackRound = new FeedbackRound(
            // TODO: note: https://stackoverflow.com/questions/25384680/how-to-convert-a-unix-timestamp-to-localdatetime-without-conversion
            LocalDateTime.ofInstant(
                Instant.ofEpochMilli(feedbackStartDto.getDateStart()), TimeZone.getDefault().toZoneId()
            ),
            LocalDateTime.ofInstant(
                Instant.ofEpochMilli(feedbackStartDto.getDateEnd()), TimeZone.getDefault().toZoneId()
            ),
            false,
            false,
            employeeRepository.getOne(feedbackStartDto.getManagerId()),
            new HashSet<>(),
            new HashSet<>(),
            feedbackRoundQuestions,
            adminSelectedPeers == null ? new HashSet<>() : adminSelectedPeers
                .stream()
                .map(employeeRepository::findByEmailAddress)
                .collect(Collectors.toSet()),
            feedbackStartDto.isManagerCanAndShouldSelectPeers(),
            new HashSet<>(),
            // If required to select peers then the peers aren't accepted by default
            !feedbackStartDto.isManagerCanAndShouldSelectPeers(),
            null,
            null
        );

        // Saving reverse mapping direction of questions too
        feedbackRoundQuestions.forEach(feedbackQuestion -> feedbackQuestion.setFeedbackRound(feedbackRound));
        feedbackRound.setFeedbackRoundQuestions(feedbackRoundQuestions);
        return saveAndProcessNewFeedbackRound(feedbackRound);
    }

    @Override
    public Set<FeedbackRoundDto> getDtosForManager(String email) {
        return this.feedbackRoundRepository
            .findByManager_EmailAddress(email)
            .stream()
            .map(FeedbackRoundDto::mapFeedbackRound)
            .collect(Collectors.toSet());
    }

    @Override
    public boolean selectPeers(String managerEmail, long feedbackRoundId, PeersStringOnlyDto selectionPeerEmails) {
        try {
            FeedbackRound feedbackRound = this.feedbackRoundRepository.findOne(feedbackRoundId);
            feedbackRound.setAdminSelectedPeers(
                selectionPeerEmails.getPeers() == null ? new HashSet<>() : selectionPeerEmails.getPeers().stream().map(
                    employeeRepository::findByEmailAddress
                ).collect(Collectors.toSet())
            );
            feedbackRound.setAcceptedPeers(false);
            feedbackRound.setManagerCanAndShouldSelectPeers(false);
            this.feedbackRoundRepository.save(feedbackRound);
            return true;
        } catch (Exception e) {
            log.error(e.toString());
            return false;
        }
    }

    @Override
    public Set<FeedbackRoundStatsDto> getAllStatsDto() {
        return this.feedbackRoundRepository
            .findAll()
            .stream()
            .map(FeedbackRoundStatsDto::mapFeedbackRound)
            .collect(Collectors.toSet());
    }

    private Set<FeedbackRoundQuestion> getFeedbackQuestionsFromEditedQuestions(QuestionListDto editedQuestions) {
        HashSet<FeedbackRoundQuestion> feedbackRoundQuestions = new HashSet<>();
        List<Question> questions = this.questionService.updateQuestions(editedQuestions);
        for (Question question : questions) {
            // NOTE: The Feedback Question doesn't have FeedbackRound yet, it's set outside!
            FeedbackRoundQuestion feedbackRoundQuestion = new FeedbackRoundQuestion(null, question);
            feedbackQuestionRepository.save(feedbackRoundQuestion);
            feedbackRoundQuestions.add(feedbackRoundQuestion);
        }
        return feedbackRoundQuestions;
    }

    @Deprecated
    private void forEachQuestionDto(
        QuestionListDto editedQuestions, BiConsumer<QuestionListDto.QuestionDto, QuestionTarget> consumer
    ) {
        editedQuestions.getLower().forEach(questionDto -> consumer.accept(questionDto, QuestionTarget.LOWER));
        editedQuestions.getMiddle().forEach(questionDto -> consumer.accept(questionDto, QuestionTarget.MIDDLE));
        editedQuestions.getHigher().forEach(questionDto -> consumer.accept(questionDto, QuestionTarget.HIGHER));
    }

    @Override
    public boolean acceptPeersReview(long feedbackRoundId) {
        // Low priority TODO: take care of hazard if modification happens right before acceptance.
        // This would be solved if frontend sent the whole accepted list of peers as a parameter for transactional check
        FeedbackRound feedbackRound = this.feedbackRoundRepository.findOne(feedbackRoundId);
        feedbackRound.setAcceptedPeers(true);
        return saveAndProcessNewFeedbackRound(feedbackRound);
    }

    @Override
    public PeersDto getPeersRequiringReviewAcceptance() {
        // Wrap the result into List Dto
        return new PeersDto(
            // Select all rounds with peers that still need to be accepted
            this.feedbackRoundRepository.findByPeersNotNullAndAcceptedPeersIsFalse()
                .stream()
                .map(feedbackRound ->
                    // Map the round details and all peers per each round
                    new PeersDto.FeedbackRoundPeersDto(
                        feedbackRound.getIdFeedbackRound(),
                        EmployeeDto.mapEmployee(feedbackRound.getManager()),
                        new PeersStringOnlyDto(
                            feedbackRound.getPeers()
                                .stream()
                                .map(Employee::getEmailAddress)
                                .collect(Collectors.toList())
                        )
                    )
                )
                .collect(Collectors.toList())
        );
    }

    @Override
    public boolean rejectPeersReview(long feedbackRoundId, String reason) {
        FeedbackRound feedbackRound = this.feedbackRoundRepository.findOne(feedbackRoundId);
        feedbackRound.setRejectedPeersReason(reason);
        feedbackRound.setAcceptedPeers(false);
        return saveAndProcessNewFeedbackRound(feedbackRound);
    }

    private boolean saveAndProcessNewFeedbackRound(FeedbackRound feedbackRound) {
        try {
            this.feedbackRoundRepository.save(feedbackRound);
            Set<Employee> employeesWithAccess = new HashSet<>();

            for (OrgUnit orgUnit : orgUnitRepository.findByManagerEquals(feedbackRound.getManager())) {
                employeesWithAccess.addAll(this.employeeRepository.findByOrgUnit(orgUnit));
            }

            for (Employee e : employeesWithAccess) {
                feedbackAccessRepository.save(new FeedbackAccess(feedbackRound, e));
            }
            // Low priority TODO rollback transaction if error on email
            if (feedbackRound.isManagerCanAndShouldSelectPeers()) {
                this.emailService.sendNewManagerShouldSelectPeers(feedbackRound);
            }
            this.emailService.sendNewFeedbackAccess(new ArrayList<>(employeesWithAccess));
            return true;
        } catch (Exception e) {
            log.error(e.toString());
            return false;
        }
    }
}
