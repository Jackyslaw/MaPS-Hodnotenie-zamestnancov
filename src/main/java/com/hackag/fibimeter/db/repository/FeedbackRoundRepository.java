package com.hackag.fibimeter.db.repository;

import com.hackag.fibimeter.db.model.Employee;
import com.hackag.fibimeter.db.model.FeedbackRound;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedbackRoundRepository extends JpaRepository<FeedbackRound, Long> {

    FeedbackRound findByManagerEquals(Employee employee);

    List<FeedbackRound> findByPeersNotNullAndAcceptedPeersIsFalse();

    List<FeedbackRound> findByPeersNotNullAndManagerEquals(Employee employee);

    List<FeedbackRound> findByManager_EmailAddress(String managerEmailAddress);

    List<FeedbackRound> findByStartedFalseOrFinishedFalse();

//    @Query(
//        "SELECT FeedbackRound " +
//            "FROM FeedbackRound " +
//            "JOIN FeedbackRound.feedbackAccesses access " +
//            "WHERE FeedbackRound.manager.emailAddress = :managerEmail " +
//            "AND access.employee.emailAddress = :employeeAddress")
//    List<FeedbackRound> findByManagerAndEmployee(
//        @Param("managerEmail") String managerEmail,
//        @Param("employeeEmail") String employeeEmail
//    );
}
