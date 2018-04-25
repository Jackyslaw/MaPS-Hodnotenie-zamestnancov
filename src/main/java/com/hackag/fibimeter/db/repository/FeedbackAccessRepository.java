package com.hackag.fibimeter.db.repository;

import com.hackag.fibimeter.db.model.Employee;
import com.hackag.fibimeter.db.model.FeedbackAccess;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackAccessRepository extends JpaRepository<FeedbackAccess, Long> {

    FeedbackAccess findByEmployeeAndFeedbackRound_Manager(Employee employee, Employee manager);
}
