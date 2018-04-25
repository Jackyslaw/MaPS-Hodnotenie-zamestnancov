package com.hackag.fibimeter.db.services.impl;

import com.hackag.fibimeter.db.model.Employee;
import com.hackag.fibimeter.db.model.FeedbackRound;
import com.hackag.fibimeter.db.services.EmailService;
import com.hackag.fibimeter.db.services.EmployeeService;
import com.hackag.fibimeter.mail.MailHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EmailServiceImpl implements EmailService {

    private enum Language {
        SLOVAK, ENGLISH
    }

    // Global settings
    private static final Language LANGUAGE = Language.SLOVAK;
    // TODO: get current ip + port
    private static final String DOMAIN = "http://localhost:8080";

    // Email contents
    private static final Map<Language, String> HEADER = new HashMap<>();
    private static final Map<Language, String> FOOTER = new HashMap<>();

    static {
        HEADER.put(Language.SLOVAK, "Ahoj!\nLink pre prístup na Fibimeter je: " + DOMAIN);
        HEADER.put(Language.ENGLISH, "Hello!\nYour link to access Fibimeter is: " + DOMAIN);

        FOOTER.put(Language.SLOVAK, "\n\nS pozdravom,\nTvoj Fibimeter\n");
        FOOTER.put(Language.ENGLISH, "\n\nBest regards,\nYours Fibimeter\n");
    }

    @Autowired
    private MailHelper mailHelper;
    @Autowired
    private EmployeeService employeeService;

    @Override
    public void sendNewFeedbackAccess(Employee employee) {
        String to = employee.getEmailAddress();
        String password = employee.getPassword();
        String login = employee.getEmailAddress();
        if (login == null || password == null) {
            employeeService.generatePassword(employee);
            password = employee.getPassword();
            login = employee.getEmailAddress();
        }
        StringBuilder mailBody = new StringBuilder();
        mailBody.append(HEADER.get(LANGUAGE) + "\n");
        mailBody.append(String.format("Tvoje prihlasovacie meno je: %s\n", login));
        mailBody.append(String.format("Tvoje heslo je: %s\n", password));
        mailBody.append(FOOTER.get(LANGUAGE));
        mailHelper.sendMail(to, "Fibimeter: New feedback available", mailBody.toString());
    }

    @Override
    public void sendNewFeedbackAccess(List<Employee> employees) {
        employees.forEach(this::sendNewFeedbackAccess);
    }

    @Override
    public void sendReminder(Employee employee) {
        //TODO
        throw new NotImplementedException();
    }

    @Override
    public void sendReminder(List<Employee> employees) {
        employees.forEach(this::sendReminder);
    }

    @Override
    public void sendNewManagerShouldSelectPeers(FeedbackRound feedbackRound) {
        Employee manager = feedbackRound.getManager();
        String subject;
        String message;
        if (feedbackRound.isAcceptedPeers()) {
            subject = "Fibimeter: Your peer selection was accepted";
            message = String.format("\nTvoj výber kolegov pre nové kolo spätnej väzby, v rámci ktorej ťa budú " +
                "hodnotiť ostatní zamestnanci, bol úspešne schválený.");
        } else if (null == feedbackRound.getRejectedPeersReason()) {
            subject = "Fibimeter: New peer selection available";
            message = String.format("\nJe k dispozícii nové kolo spätnej väzby, v rámci ktorej ťa budú hodnotiť " +
                "ostatní zamestnanci. Pre zlepšenie testovacej vzorky by si si mal pred jej spustením vybrať kolegov, " +
                "ktorí ťa poznajú najlepšie.\n");
        } else {
            subject = "Fibimeter: Your peer selection was denied";
            message = String.format("\nTvoj výber kolegov pre nové kolo spätnej väzby, v rámci ktorej ťa budú " +
                    "hodnotiť ostatní zamestnanci, bol zamietnutý administrátorom z nasledujúceho dôvodu:\n%s",
                feedbackRound.getRejectedPeersReason());
        }

        StringBuilder mailBody = new StringBuilder();
        mailBody.append(HEADER.get(LANGUAGE) + "\n");
        mailBody.append(String.format("Tvoje prihlasovacie meno je: %s\n", manager.getEmailAddress()));
        mailBody.append(String.format("Tvoje heslo je: %s\n", manager.getPassword()));
        mailBody.append(message);
        mailBody.append(FOOTER.get(LANGUAGE));
        mailHelper.sendMail(
            manager.getEmailAddress(),
            subject,
            mailBody.toString()
        );
    }
}
