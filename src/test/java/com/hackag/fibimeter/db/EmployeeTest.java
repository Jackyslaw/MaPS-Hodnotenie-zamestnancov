package com.hackag.fibimeter.db;

import com.hackag.fibimeter.db.model.Employee;
import com.hackag.fibimeter.db.model.FeedbackAccess;
import com.hackag.fibimeter.db.model.FeedbackRound;
import com.hackag.fibimeter.db.model.enumeration.Gender;
import com.hackag.fibimeter.db.repository.EmployeeRepository;
import com.hackag.fibimeter.db.repository.FeedbackAccessRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.HashSet;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class EmployeeTest {

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private FeedbackAccessRepository feedbackAccessRepository;

    private long employeeId;
    private long managerId;

    private static final String EMPLOYEE_EMAIL = "employee@not-t-systems.com";
    private static final String EMPLOYEE_PASSWORD = "iWorkForBread";
    private static final String MANAGER_EMAIL = "manager@not-t-systems.com";
    private static final String MANAGER_PASSWORD = "waterfall4ever";
    private static final String MANAGER_NAME = "Carl";
    private static final String MANAGER_SURNAME = "Man";

    @Before
    public void setUp() {
        employeeId = employeeRepository.saveAndFlush(new Employee(
            EMPLOYEE_PASSWORD,
            false,
            MANAGER_NAME,
            MANAGER_SURNAME,
            "academicTitle",
            Gender.M,
            LocalDateTime.now(),
            MANAGER_NAME + " " + MANAGER_SURNAME,
            EMPLOYEE_EMAIL,
            "street",
            "city",
            "12345",
            "CODE",
            null,
            new HashSet<>(),
            LocalDateTime.now(),
            null,
            "jobTitle",
            0L,
            "function",
            "managementLevel",
            new HashSet<>(),
            new HashSet<>(),
            new HashSet<>(),
            new HashSet<>(),
            new HashSet<>()
        )).getIdEmployee();
        managerId = employeeRepository.saveAndFlush(new Employee(
            MANAGER_PASSWORD,
            false,
            "firstName",
            "surname",
            "academicTitle",
            Gender.M,
            LocalDateTime.now(),
            "displayName",
            MANAGER_EMAIL,
            "street",
            "city",
            "12345",
            "CODE",
            null,
            new HashSet<>(),
            LocalDateTime.now(),
            null,
            "jobTitle",
            0L,
            "function",
            "managementLevel",
            new HashSet<>(),
            new HashSet<>(),
            new HashSet<>(),
            new HashSet<>(),
            new HashSet<>()
        )).getIdEmployee();
    }

    @Test
    public void getManagerEmailAsEmployee() {
        Assert.assertEquals(MANAGER_EMAIL, employeeRepository.findOne(managerId).getEmailAddress());
    }

    @Test
    public void feedbackTest() {
        Employee employee = employeeRepository.findOne(employeeId);
        employee.getEmployeeFeedbackAccesses()
            .add(
                new FeedbackAccess(
                    new FeedbackRound(
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        false,
                        false,
                        employeeRepository.getOne(managerId),
                        new HashSet<>(),
                        new HashSet<>(),
                        new HashSet<>(),
                        new HashSet<>(),
                        false,
                        new HashSet<>(),
                        true,
                        null,
                        null
                    ),
                    employeeRepository.getOne(employeeId)
                )
            );
        employeeRepository.saveAndFlush(employee);
        // Current issue in test: feedback access not created by cascade + reference has to be mapped manually both ways
        FeedbackAccess feedbackAccess = feedbackAccessRepository.findAll().get(0);
        Assert.assertTrue(
            "Creating a new FeedbackAccess entry didn't reflect in a created FeedbackRound.",
            feedbackAccess.getFeedbackRound().getFeedbackAccesses().contains(feedbackAccess)
        );
        Assert.assertTrue(feedbackAccess.getFeedbackRound().getFeedbackAccesses().contains(feedbackAccess));
    }
}

