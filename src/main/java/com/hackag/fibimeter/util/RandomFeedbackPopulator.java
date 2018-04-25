package com.hackag.fibimeter.util;

import com.hackag.fibimeter.FibimeterApplication;
import com.hackag.fibimeter.db.model.*;
import com.hackag.fibimeter.db.model.enumeration.QuestionTarget;
import com.hackag.fibimeter.db.repository.*;
import com.hackag.fibimeter.db.services.EmailService;
import com.hackag.fibimeter.db.services.EmployeeService;
import com.hackag.fibimeter.db.services.QuestionService;
import com.hackag.fibimeter.mail.MailHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Experimental random data populator for easier testing during the development thanks to a more populated database.
 */
@Component
@Transactional
public class RandomFeedbackPopulator {

    @Autowired
    EmployeeService employeeService;
    @Autowired
    EmailService emailService;
    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    QuestionService questionService;
    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    QuestionCategoryRepository questionCategoryRepository;
    @Autowired
    FeedbackQuestionRepository feedbackQuestionRepository;
    @Autowired
    FeedbackRoundRepository feedbackRoundRepository;
    @Autowired
    FeedbackAccessRepository feedbackAccessRepository;

    public static void main(String[] args) {
        SpringApplication.run(FibimeterApplication.class, args);
        MailHelper.EMAILS_DISABLED = true;
        StaticContextAccessor.getBean(RandomFeedbackPopulator.class).populate(1L);
        MailHelper.EMAILS_DISABLED = false;
    }

    public void populate(long idEmployee) {
        // Generate new password if none was made yet
        Employee employee = employeeRepository.getOne(idEmployee);
        System.out.println("Name: " + employee.getFirstName() + " " + employee.getSurname());
        if (employee.getPassword() == null || "".equals(employee.getPassword())) {
            employeeService.generatePassword(employee);
            System.out.println("New login and password: " + employee.getEmailAddress() + " " + employee.getPassword());
        }

        // Generate some content
        Random random = new Random();
        for (int i = random.nextInt(10) + 4; i-- > 0; ) {
            QuestionCategory category = questionCategoryRepository.findByCategoryName("populatorCategory");
            if (category == null) {
                category = questionCategoryRepository.saveAndFlush(
                    new QuestionCategory("populatorCategory", "", new HashSet<>())
                );
            }
            Question question = questionRepository.saveAndFlush(
                new Question(
                    String.valueOf(random.nextInt(500)),
                    String.valueOf(random.nextInt(500)),
                    String.valueOf(random.nextInt(500)),
                    String.valueOf(random.nextInt(500)),
                    String.valueOf(random.nextInt(500)),
                    QuestionTarget.values()[Math.abs(random.nextInt(QuestionTarget.values().length))],
                    true,
                    category,
                    new HashSet<>(),
                    new HashSet<>()
                )
            );

            Set<FeedbackAccess> employeeFeedbackAccesses = employee.getEmployeeFeedbackAccesses();
            // Feedback access to self if no accesses exist yet
            if (employeeFeedbackAccesses.size() == 0) {
                employeeFeedbackAccesses.add(
                    feedbackAccessRepository.saveAndFlush(
                        new FeedbackAccess(
                            createNewFeedbackRound(employee, question),
                            employee
                        )
                    )
                );
            }
            // Feedback access to a random other employee
            List<Employee> allManagers = new ArrayList<>(employeeRepository.findByManagedOrgUnitsNotNull());
            // Only if there are more other employees
            if (allManagers.size() > 1) {
                Employee randomManager = allManagers.get(random.nextInt(allManagers.size()));
                // Only if the random manager isn't a self reference
                if (!employee.getIdEmployee().equals(randomManager.getIdEmployee())) {
                    FeedbackRound byManagerEquals = feedbackRoundRepository.findByManagerEquals(randomManager);
                    // Only if there is no such feedback access yet
                    if (byManagerEquals == null) {
                        employeeFeedbackAccesses.add(
                            feedbackAccessRepository.saveAndFlush(
                                new FeedbackAccess(createNewFeedbackRound(randomManager, question), employee)
                            )
                        );
                        System.out.println("Added feedback access to manager " + randomManager.getEmailAddress());
                    }
                }
            }

            // Print some informational statement at the end
            if (i == 0) {
                Set<FeedbackRoundQuestion> feedbackRoundQuestions = employee
                    .getEmployeeFeedbackAccesses().iterator().next().getFeedbackRound().getFeedbackRoundQuestions();
                System.out.println(feedbackRoundQuestions.size() + " question title(s) in last entry:");
                questionService.getQuestions(employee, 1L).forEach(question1 -> System.out.println(question1.getTitle()));
                System.out.println("Random feedback population for employee " + employee.getEmailAddress()
                    + " with password " + employee.getPassword() + " finished");
            }
        }
    }

    private FeedbackRound createNewFeedbackRound(Employee manager, Question question) {
        FeedbackRound feedbackRound = feedbackRoundRepository.saveAndFlush(
            new FeedbackRound(
                LocalDateTime.now(),
                LocalDateTime.now(),
                false,
                false,
                manager,
                new HashSet<>(),
                new HashSet<>(),
                new HashSet<>(),
                new HashSet<>(),
                false,
                new HashSet<>(),
                true,
                null,
                null
            )
        );
        FeedbackRoundQuestion feedbackRoundQuestionReverseAdd = feedbackQuestionRepository.saveAndFlush(
            new FeedbackRoundQuestion(
                feedbackRound, question
            )
        );
        feedbackRound.getFeedbackRoundQuestions().add(feedbackRoundQuestionReverseAdd);
        return feedbackRound;
    }
}
