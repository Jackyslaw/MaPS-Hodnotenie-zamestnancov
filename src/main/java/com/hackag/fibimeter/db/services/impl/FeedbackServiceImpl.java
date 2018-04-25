package com.hackag.fibimeter.db.services.impl;

import com.hackag.fibimeter.db.model.Employee;
import com.hackag.fibimeter.db.model.Feedback;
import com.hackag.fibimeter.db.model.FeedbackAccess;
import com.hackag.fibimeter.db.model.FeedbackRound;
import com.hackag.fibimeter.db.repository.*;
import com.hackag.fibimeter.db.services.FeedbackService;
import com.hackag.fibimeter.dto.AnswerListDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FeedbackServiceImpl implements FeedbackService {

    private static Logger log = LoggerFactory.getLogger(FeedbackServiceImpl.class);

    @Autowired
    private FeedbackRepository feedbackRepository;
    @Autowired
    private FeedbackRoundRepository feedbackRoundRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private FeedbackAccessRepository feedbackAccessRepository;

    @Override
    public boolean answer(String employeeEmail, AnswerListDto answerListDto) {
        Employee employee = employeeRepository.findByEmailAddress(employeeEmail);
        Employee manager = employeeRepository.findOne(answerListDto.getManagerId());

//        List<FeedbackRound> feedbackRounds = this.feedbackRoundRepository.findByManagerAndEmployee(
//            manager.getEmailAddress(),
//            employee.getEmailAddress()
//        );
//        if (feedbackRounds.size() == 0) {
//            log.warn("There are no FeedbackRounds for a Manager and Employee combination!");
//            return false;
//        }
//        if (feedbackRounds.size() > 1) {
//            log.error("There are two FeedbackRounds for the same Manager and Employee combination!");
//            return false;
//        }
//        FeedbackRound feedbackRound = feedbackRounds.get(0);
        FeedbackAccess feedbackAccess =
            this.feedbackAccessRepository.findByEmployeeAndFeedbackRound_Manager(employee, manager);
        FeedbackRound feedbackRound = feedbackAccess.getFeedbackRound();

        this.feedbackAccessRepository.delete(feedbackAccess);
        log.info("Using feedback access id " + feedbackAccess.getIdFeedbackAccess() + ", deleted the entry from DB");

        for (AnswerListDto.AnswerDto answerDto : answerListDto.getAnswers()) {
            Feedback feedback = new Feedback(
                manager,
                feedbackRound,
                questionRepository.getOne(answerDto.getQuestionId()),
                answerDto.getAnswer()
            );
            this.feedbackRepository.save(feedback);
            log.info(
                "Saved feedback of question id " + answerDto.getQuestionId()
                    + " with answer " + answerDto.getAnswer());
        }
        return true;
    }
}
