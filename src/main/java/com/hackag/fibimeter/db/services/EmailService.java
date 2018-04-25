package com.hackag.fibimeter.db.services;

import com.hackag.fibimeter.db.model.Employee;
import com.hackag.fibimeter.db.model.FeedbackRound;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface EmailService {

    void sendNewFeedbackAccess(Employee employee);

    void sendNewFeedbackAccess(List<Employee> employees);

    void sendReminder(Employee employee);

    void sendReminder(List<Employee> employees);

    void sendNewManagerShouldSelectPeers(FeedbackRound feedbackRound);
}
