package com.hackag.fibimeter.db.services;

import com.hackag.fibimeter.db.model.Employee;
import com.hackag.fibimeter.db.model.Question;
import com.hackag.fibimeter.dto.FeedbackQuestionDto;
import com.hackag.fibimeter.dto.QuestionListDto;

import java.util.List;

public interface QuestionService {

    /**
     * Return question list for rated manager situated for employee.
     *
     * @param managerId Id of rated manager
     * @param employee  {@link Employee} who will answer
     * @return List of questions
     */
    List<Question> getQuestions(Employee employee, long managerId);

    /**
     * Return list of valid questions that are meant to be used in next test round in {@link QuestionListDto}.
     *
     * @return Valid questions in {@link QuestionListDto}
     */
    QuestionListDto getValidQuestionListDto();

    /**
     * Return question list in {@link QuestionListDto} for rated manager situated for employee.
     *
     * @param employee  {@link Employee} who will answer
     * @param managerId Id of rated manager
     * @return filled {@link QuestionListDto}
     */
    List<FeedbackQuestionDto> getQuestionListDto(Employee employee, long managerId);

    /**
     * Modifies existing Question entities based on representational {@link QuestionListDto},
     * creating new valid Question instances on modification and removing valid flag from previous ones.
     *
     * @param questionListDto Dto with modified Question instances with their ID representing original question.
     */
    List<Question> updateQuestions(QuestionListDto questionListDto);
}
