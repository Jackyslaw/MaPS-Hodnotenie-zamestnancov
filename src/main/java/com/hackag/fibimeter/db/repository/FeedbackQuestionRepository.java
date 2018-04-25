package com.hackag.fibimeter.db.repository;

import com.hackag.fibimeter.db.model.FeedbackRoundQuestion;
import com.hackag.fibimeter.db.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedbackQuestionRepository extends JpaRepository<FeedbackRoundQuestion, Long> {
    List<FeedbackRoundQuestion> findFeedbackRoundQuestionsByQuestionAndFeedbackRound_Manager_IdEmployeeOrderByFeedbackRound_StartDateDesc(Question question, long managerId);
}
