package com.hackag.fibimeter.db.services.impl;

import com.hackag.fibimeter.db.model.Employee;
import com.hackag.fibimeter.db.model.FeedbackRound;
import com.hackag.fibimeter.db.model.OrgUnit;
import com.hackag.fibimeter.db.model.Question;
import com.hackag.fibimeter.db.model.enumeration.QuestionTarget;
import com.hackag.fibimeter.db.repository.FeedbackQuestionRepository;
import com.hackag.fibimeter.db.repository.QuestionCategoryRepository;
import com.hackag.fibimeter.db.repository.QuestionRepository;
import com.hackag.fibimeter.db.services.QuestionService;
import com.hackag.fibimeter.dto.FeedbackQuestionDto;
import com.hackag.fibimeter.dto.QuestionListDto;
import io.jsonwebtoken.lang.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Consumer;

@Service
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private QuestionCategoryRepository questionCategoryRepository;
    @Autowired
    private FeedbackQuestionRepository feedbackQuestionRepository;

    @Override
    public List<Question> getQuestions(Employee employee, long managerID) {
        List<Question> questions = new ArrayList<>();
        employee.getEmployeeFeedbackAccesses().parallelStream()
            .filter(access -> access.getFeedbackRound().getManager().getIdEmployee() == managerID)
            // Manager can only have one feedback access per employee
            .findFirst()
            .ifPresent(
                access -> access.getFeedbackRound().getFeedbackRoundQuestions().forEach(
                    feedbackQuestion -> questions.add(feedbackQuestion.getQuestion())
                )
            );
        return questions;
    }

    @Override
    public QuestionListDto getValidQuestionListDto() {
        QuestionListDto questionListResult = new QuestionListDto();
        this.questionRepository.findByValid(true).forEach(
            question -> addQuestionToListDto(questionListResult, question)
        );
        return questionListResult;
    }

    @Override
    public List<FeedbackQuestionDto> getQuestionListDto(Employee employee, long managerID) {
        Assert.notNull(employee);
        List<FeedbackQuestionDto> questionListResult = new ArrayList<>();
        // TODO: optimize to select employee by ID
        this.questionRepository.findAll()
            .stream()
            .filter(question -> question.getFeedbackRoundQuestions().stream().anyMatch(
                feedbackQuestion -> feedbackQuestion.getFeedbackRound().getFeedbackAccesses().stream().anyMatch(
                    feedbackAccess ->
                        employee.equals(feedbackAccess.getEmployee())
                            &&
                            managerID == feedbackAccess.getFeedbackRound().getManager().getIdEmployee()
                )
            ))
            .forEach(
                question -> questionListResult.add(new FeedbackQuestionDto(question.getIdQuestion(), getSuitableFeedbackQuestion(employee, managerID, question)))
            );
        return questionListResult;
    }

    private String getSuitableFeedbackQuestion(Employee employee, long managerID, Question question) {
        FeedbackRound feedbackRound = feedbackQuestionRepository.findFeedbackRoundQuestionsByQuestionAndFeedbackRound_Manager_IdEmployeeOrderByFeedbackRound_StartDateDesc(question, managerID).get(0).getFeedbackRound();
        Set<OrgUnit> units = feedbackRound.getManager().getManagedOrgUnits();
        if (employee.equals(feedbackRound.getManager())) {
            return question.getTextSelf();
        }
        for (OrgUnit unit : units) {
            if (unit.getEmployees().contains(employee)) {
                return question.getTextUnderman();
            }
        }
        OrgUnit unit = employee.getOrgUnit();
        if (unit.getEmployees().contains(feedbackRound.getManager())) {
            return question.getTextChief();
        }
        return question.getTextShipmate();
    }

    @Override
    public List<Question> updateQuestions(QuestionListDto questionListDto) {
        ArrayList<Question> questions = new ArrayList<>();
        Consumer<Question> consumer = new Consumer<Question>() {
            @Override
            public void accept(Question question) {
                questions.add(question);
            }
        };

        questionListDto.getHigher().forEach(
            questionDto -> updateQuestion(questionDto, QuestionTarget.HIGHER).ifPresent(consumer)
        );
        questionListDto.getMiddle().forEach(
            questionDto -> updateQuestion(questionDto, QuestionTarget.MIDDLE).ifPresent(consumer)
        );
        questionListDto.getLower().forEach(
            questionDto -> updateQuestion(questionDto, QuestionTarget.LOWER).ifPresent(consumer)
        );
        return questions;
    }

    private Optional<Question> updateQuestion(QuestionListDto.QuestionDto questionDto, QuestionTarget questionTarget) {
        switch (questionDto.getStateChange()) {
            case CURRENT:
                return Optional.of(this.questionRepository.findOne(questionDto.getId()));
            case NEW:
                return Optional.of(saveQuestion(questionDto, questionTarget));
            case CHANGED:
                setInvalidQuestion(questionDto.getId());
                return Optional.of(saveQuestion(questionDto, questionTarget));
            case DELETED:
                setInvalidQuestion(questionDto.getId());
                return Optional.empty();
            default:
                throw new IllegalArgumentException("State change unknown, cannot update question");
        }
    }

    private void setInvalidQuestion(long questionId) {
        Question question = this.questionRepository.getOne(questionId);
        question.setValid(false);
        this.questionRepository.save(question);
    }

    private Question saveQuestion(QuestionListDto.QuestionDto questionDto, QuestionTarget questionTarget) {
        return this.questionRepository.save(new Question(
            questionDto.getTitle(),
            questionDto.getText().getSelf(),
            questionDto.getText().getUnderman(),
            questionDto.getText().getShipmate(),
            questionDto.getText().getChief(),
            questionTarget,
            true,
            this.questionCategoryRepository.getOne(questionDto.getQuestionCategoryId()),
            new HashSet<>(),
            new HashSet<>()
        ));
    }

    private void addQuestionToListDto(QuestionListDto questionList, Question question) {
        switch (question.getQuestionTarget()) {
            case LOWER:
                questionList.getLower().add(QuestionListDto.QuestionDto.mapQuestion(question));
                break;
            case MIDDLE:
                questionList.getMiddle().add(QuestionListDto.QuestionDto.mapQuestion(question));
                break;
            case HIGHER:
                questionList.getHigher().add(QuestionListDto.QuestionDto.mapQuestion(question));
                break;
        }
    }
}
