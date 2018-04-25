package com.hackag.fibimeter.controller;

import com.hackag.fibimeter.db.services.EmployeeService;
import com.hackag.fibimeter.db.services.FeedbackRoundService;
import com.hackag.fibimeter.db.services.QuestionCategoryService;
import com.hackag.fibimeter.db.services.QuestionService;
import com.hackag.fibimeter.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Controller
@RequestMapping("/admin")
@Secured("ROLE_ADMIN")
public class AdminController {

    private static Logger log = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private QuestionCategoryService questionCategoryService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private FeedbackRoundService feedbackRoundService;

    @RequestMapping(value = "/stats", method = RequestMethod.GET)
    @ResponseBody
    public Set<FeedbackRoundStatsDto> getStats() {
        return this.feedbackRoundService.getAllStatsDto();
    }

    @RequestMapping(value = "/question-categories", method = RequestMethod.GET)
    @ResponseBody
    public Set<QuestionCategoryDto> getQuestionCategories() {
        return this.questionCategoryService.getAllDtos();
    }

    @RequestMapping(value = "/all-managers", method = RequestMethod.GET)
    @ResponseBody
    public Set<EmployeeDto> getAllManagers() {
        return employeeService.getAllManagersDto();
    }

    @RequestMapping(value = "/valid-questions", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<QuestionListDto> getValidQuestions() {
        return new ResponseEntity<>(questionService.getValidQuestionListDto(), HttpStatus.OK);
    }

    @RequestMapping(value = "/valid-questions", method = RequestMethod.POST, headers = {"content-type=application/json"})
    @ResponseBody
    public ResponseEntity<QuestionListDto> setValidQuestions(@RequestBody QuestionListDto questionListDto) {
        this.questionService.updateQuestions(questionListDto);
        return new ResponseEntity<>(questionService.getValidQuestionListDto(), HttpStatus.OK);
    }

    @RequestMapping(value = "/start-feedbackround", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Map<String, Boolean>> startFeedbackRound(@RequestBody FeedbackStartDto feedbackStartDto) {
        HashMap<String, Boolean> response = new HashMap<>();
        response.put("success", this.feedbackRoundService.startFeedbackRound(feedbackStartDto));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping("/peers-review/get")
    @ResponseBody
    public ResponseEntity<PeersDto> getPeersReview() {
        return new ResponseEntity<>(this.feedbackRoundService.getPeersRequiringReviewAcceptance(), HttpStatus.OK);
    }

    @RequestMapping(value = "/peers-review/accept/{feedbackRoundId}", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Map<String, Boolean>> acceptPeersReviewFeedbackRound(
        @PathVariable("feedbackRoundId") Long feedbackRoundId
    ) {
        HashMap<String, Boolean> response = new HashMap<>();
        response.put("success", this.feedbackRoundService.acceptPeersReview(feedbackRoundId));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/peers-review/reject/{feedbackRoundId}", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Map<String, Boolean>> rejectPeersReviewFeedbackRound(
        @RequestBody PeersReviewRejectDto rejectDto, @PathVariable("feedbackRoundId") Long feedbackRoundId
    ) {
        HashMap<String, Boolean> response = new HashMap<>();
        response.put("success", this.feedbackRoundService.rejectPeersReview(feedbackRoundId, rejectDto.getReason()));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
