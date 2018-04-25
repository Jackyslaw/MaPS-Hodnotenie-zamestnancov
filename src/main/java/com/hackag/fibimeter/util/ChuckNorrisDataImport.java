package com.hackag.fibimeter.util;

import com.hackag.fibimeter.FibimeterApplication;
import com.hackag.fibimeter.db.model.Employee;
import com.hackag.fibimeter.db.model.OrgUnit;
import com.hackag.fibimeter.db.model.Question;
import com.hackag.fibimeter.db.model.QuestionCategory;
import com.hackag.fibimeter.db.model.enumeration.QuestionTarget;
import com.hackag.fibimeter.db.repository.EmployeeRepository;
import com.hackag.fibimeter.db.repository.OrgUnitRepository;
import com.hackag.fibimeter.db.repository.QuestionCategoryRepository;
import com.hackag.fibimeter.db.repository.QuestionRepository;
import com.hackag.fibimeter.db.services.FeedbackRoundService;
import com.hackag.fibimeter.db.services.QuestionCategoryService;
import com.hackag.fibimeter.mail.MailHelper;
import org.springframework.boot.SpringApplication;

import java.io.IOException;
import java.util.Set;

public class ChuckNorrisDataImport {

    private static EmployeeRepository employeeRepository;
    private static OrgUnitRepository orgUnitRepository;
    private static QuestionCategoryRepository questionCategoryRepository;
    private static QuestionRepository questionRepository;
    private static QuestionCategoryService questionCategoryService;
    private static FeedbackRoundService feedbackRoundService;

    public static void main(String[] args) throws IOException {
        SpringApplication springApplication = new SpringApplication(FibimeterApplication.class);
        springApplication.setAdditionalProfiles("create-drop-db");
        springApplication.run();


        ChuckNorrisDataImport.employeeRepository = StaticContextAccessor.getBean(EmployeeRepository.class);
        ChuckNorrisDataImport.orgUnitRepository = StaticContextAccessor.getBean(OrgUnitRepository.class);
        ChuckNorrisDataImport.questionCategoryRepository = StaticContextAccessor.getBean(QuestionCategoryRepository.class);
        ChuckNorrisDataImport.questionRepository = StaticContextAccessor.getBean(QuestionRepository.class);
        ChuckNorrisDataImport.questionCategoryService = StaticContextAccessor.getBean(QuestionCategoryService.class);
        ChuckNorrisDataImport.feedbackRoundService = StaticContextAccessor.getBean(FeedbackRoundService.class);

        MailHelper.EMAILS_DISABLED = true;

        importEmploye();
        importQuestion();
        importFeedbackRounds();

        StaticContextAccessor.getBean(RandomFeedbackPopulator.class).populate(1L);
        StaticContextAccessor.getBean(RandomFeedbackPopulator.class).populate(2L);


        MailHelper.EMAILS_DISABLED = false;
    }

    private static void importFeedbackRounds() {
        creatFeedbackRound("managerMail");
    }

    private static void creatFeedbackRound(String managerMail) {
        Employee employee = employeeRepository.findByEmailAddress(managerMail);

    }

    private static void importQuestion() {
        crateCategory("Value orientation and sense of responsibility", "n/a");
        crateCategory("Leadership/strategic thinking", "n/a");
        crateCategory("Customer orientation", "n/a");
        crateCategory("Stakeholders managment", "n/a");

        crateQuestion("title", "textSelf", "textUnderman", "textShipmate", "textChief", "Stakeholders managment", QuestionTarget.LOWER);
        crateQuestion("title1", "textSelf", "textUnderman", "textShipmate", "textChief", "Stakeholders managment", QuestionTarget.LOWER);
        crateQuestion("title2", "textSelf", "textUnderman", "textShipmate", "textChief", "Leadership/strategic thinking", QuestionTarget.MIDDLE);
        crateQuestion("title3", "textSelf", "textUnderman", "textShipmate", "textChief", "Customer orientation", QuestionTarget.LOWER);
        crateQuestion("title4", "textSelf", "textUnderman", "textShipmate", "textChief", "Stakeholders managment", QuestionTarget.HIGHER);
        crateQuestion("title5", "textSelf", "textUnderman", "textShipmate", "textChief", "Stakeholders managment", QuestionTarget.HIGHER);
        crateQuestion("title6", "textSelf", "textUnderman", "textShipmate", "textChief", "Stakeholders managment", QuestionTarget.LOWER);


    }

    private static void crateQuestion(String title, String textSelf, String textUnderman, String textShipmate, String textChief, String category, QuestionTarget questionTarget) {
        Question question = new Question();

        question.setTitle(title);
        question.setTextSelf(textSelf);
        question.setTextUnderman(textUnderman);
        question.setTextChief(textChief);
        question.setQuestionTarget(questionTarget);
        questionRepository.save(question);

        questionCategoryService.addQuestionToCategory(question.getIdQuestion(), category);


    }

    private static void crateCategory(String name, String descrtiption) {
        QuestionCategory questionCategory = new QuestionCategory();
        questionCategory.setCategoryName(name);
        questionCategory.setDescription(descrtiption);
        questionCategoryRepository.save(questionCategory);
    }


    private static void importEmploye() {
        addUser("Pavel", "Novotný", "pavel.novotny@t-systems.com");             //Test Automation
        addUser("Marek ", "Šimák", "marek.simak@t-systems.com");                //Test Automation
        addUser("Rudolf", "Kníže", "rudolf.knize@t-systems.com");              //Public relations
        addUser("Matouš", "Pavel", "matous.pavel@t-systems.com");              //
        addUser("Kamil", "Mika", "kamil.mika@t-systems.com");                  //Develop
        addUser("Vlastimil", "Mika", "vlastimil.mika@t-systems.com");          //Develop
        addUser("Hedvika", "Nováková", "hedvika.novakova@t-systems.com");
        addUser("Radka", "Christovová", "radka.christovová@t-systems.com");
        addUser("Bohumila", "Nesvadbová", "bohumila.nesvadbova@t-systems.com");//Human resources
        addUser("Pavlína", "Němcová", "pavlina.nemcova@t-systems.com");        //Human resources

        addAdmin("Jakub", "Nejedlý", "jakub.nejedly@t-systems.com");

        addManager("Jan", "Sauer", "jan.sauer@t-systems.com", "Test Automation", "pavel.novotny@t-systems.com", "marek.simak@t-systems.com");
        addManager("Anežka", "Hamplová", "anezka.hamplova@t-systems.com", "Human resources", "pavlina.nemcova@t-systems.com", "bohumila.nesvadbova@t-systems.com");
        addManager("Petr", "Hnilo", "petr.hnilo@t-systems.com", "Develop", "vlastimil.mika@t-systems.com", "kamil.mika@t-systems.com");

        addAdminManager("Jan", "Kunetka", "jan.kunetka@t-systems.com", "Public relations", "rudolf.knize@t-systems.com", "matous.pavel@t-systems.com");
    }

    private static void addUser(String firstname, String lastname, String email) {
        Employee employeeToAdd = new Employee();
        employeeToAdd.setFirstName(firstname);
        employeeToAdd.setSurname(lastname);
        employeeToAdd.setAdmin(false);
        employeeToAdd.setEmailAddress(email);
        employeeToAdd.setPassword("password");
        employeeRepository.save(employeeToAdd);
    }

    private static void addAdmin(String firstname, String lastname, String email) {
        Employee employeeToAdd = new Employee();
        employeeToAdd.setFirstName(firstname);
        employeeToAdd.setSurname(lastname);
        employeeToAdd.setAdmin(true);
        employeeToAdd.setEmailAddress(email);
        employeeToAdd.setPassword("password");
        employeeRepository.save(employeeToAdd);
    }

    private static void addManager(String firstname, String lastname, String email, String orgUnitName, String... peersMail) {
        Employee employeeToAdd = new Employee();
        OrgUnit orgUnitToAdd = new OrgUnit();

        employeeToAdd = new Employee();
        orgUnitToAdd = new OrgUnit();

        orgUnitToAdd.setName(orgUnitName);
        orgUnitToAdd.setManager(employeeToAdd);

        employeeToAdd.setFirstName(firstname);
        employeeToAdd.setSurname(lastname);
        employeeToAdd.setAdmin(false);
        employeeToAdd.setEmailAddress(email);
        employeeToAdd.setPassword("password");
        Set<Employee> peers = orgUnitToAdd.getEmployees();

        employeeToAdd.setOrgUnit(orgUnitToAdd);


        orgUnitRepository.save(orgUnitToAdd);
        employeeRepository.save(employeeToAdd);

        orgUnitToAdd = (OrgUnit) orgUnitRepository.findByName(orgUnitName);
        for (String peerMail : peersMail) {
            Employee peer = employeeRepository.findByEmailAddress(peerMail);
            peers.add(peer);
            peer.setOrgUnit(orgUnitToAdd);
        }
        orgUnitToAdd.setEmployees(peers);

        for (Employee e : peers
            ) {
            employeeRepository.save(e);

        }
        orgUnitRepository.save(orgUnitToAdd);

    }

    private static void addAdminManager(String firstname, String lastname, String email, String orgUnitName, String... peersMail) {
        Employee employeeToAdd = new Employee();
        OrgUnit orgUnitToAdd = new OrgUnit();

        employeeToAdd = new Employee();
        orgUnitToAdd = new OrgUnit();

        orgUnitToAdd.setName(orgUnitName);
        orgUnitToAdd.setManager(employeeToAdd);

        employeeToAdd.setFirstName(firstname);
        employeeToAdd.setSurname(lastname);
        employeeToAdd.setAdmin(true);
        employeeToAdd.setEmailAddress(email);
        employeeToAdd.setPassword("password");
        Set<Employee> peers = orgUnitToAdd.getEmployees();

        employeeToAdd.setOrgUnit(orgUnitToAdd);


        orgUnitRepository.save(orgUnitToAdd);
        employeeRepository.save(employeeToAdd);

        orgUnitToAdd = (OrgUnit) orgUnitRepository.findByName(orgUnitName);
        for (String peerMail : peersMail) {
            Employee peer = employeeRepository.findByEmailAddress(peerMail);
            peers.add(peer);
            peer.setOrgUnit(orgUnitToAdd);
        }
        orgUnitToAdd.setEmployees(peers);

        for (Employee e : peers
            ) {
            employeeRepository.save(e);

        }
        orgUnitRepository.save(orgUnitToAdd);

    }


}
