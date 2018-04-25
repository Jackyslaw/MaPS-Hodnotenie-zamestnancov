package com.hackag.fibimeter.db.services;

import com.hackag.fibimeter.dto.AnswerListDto;

public interface FeedbackService {

    boolean answer(String employeeEmail, AnswerListDto answerListDto);
}
