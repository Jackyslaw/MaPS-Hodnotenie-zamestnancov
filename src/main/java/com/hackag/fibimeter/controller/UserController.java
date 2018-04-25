package com.hackag.fibimeter.controller;

import com.hackag.fibimeter.db.model.Employee;
import com.hackag.fibimeter.db.services.EmployeeService;
import com.hackag.fibimeter.db.services.FeedbackService;
import com.hackag.fibimeter.db.services.QuestionService;
import com.hackag.fibimeter.dto.AnswerListDto;
import com.hackag.fibimeter.dto.EmployeeDto;
import com.hackag.fibimeter.dto.FeedbackQuestionDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.hackag.fibimeter.util.CredentialsHelper.loggedUserMail;

@Controller
@RequestMapping("/user")
@Secured("ROLE_USER")
public class UserController {

    private static Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private QuestionService questionService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private FeedbackService feedbackService;

    @RequestMapping(value = "/valuation", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Set<EmployeeDto>> getValuatedManagersByEmployeeId() {
        Employee employee = employeeService.findByEmailAddress(loggedUserMail());
        if (employee == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(
            employee
                .getFeedbacks()
                .stream()
                .map(feedback -> EmployeeDto.mapEmployee(feedback.getManager()))
                .collect(Collectors.toSet()),
            HttpStatus.OK);
    }

    @RequestMapping(value = "/available-feedbacks", method = RequestMethod.GET)
    @ResponseBody
    public Set<EmployeeDto> getFeedbackAccessManagers() {
        return this.employeeService.getEmployeeFeedbackAccessManagersByEmailAddress(loggedUserMail());
    }

    @RequestMapping(value = "/questions", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<FeedbackQuestionDto>> getQuestions(HttpServletRequest request, @RequestParam Long managerId) {
        if (managerId == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(
            questionService.getQuestionListDto(
                employeeService.findByEmailAddress(request.getUserPrincipal().getName()), managerId
            ), HttpStatus.OK);
    }

    @RequestMapping(value = "/answer", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Map<String, Boolean>> answer(@RequestBody AnswerListDto answerListDto) {
        HashMap<String, Boolean> response = new HashMap<>();
        response.put("success", this.feedbackService.answer(loggedUserMail(), answerListDto));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
