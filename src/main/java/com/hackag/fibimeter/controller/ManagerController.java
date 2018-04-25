package com.hackag.fibimeter.controller;

import com.hackag.fibimeter.db.model.Employee;
import com.hackag.fibimeter.db.services.EmployeeService;
import com.hackag.fibimeter.db.services.FeedbackRoundService;
import com.hackag.fibimeter.dto.FeedbackRoundDto;
import com.hackag.fibimeter.dto.PeersStringOnlyDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.hackag.fibimeter.util.CredentialsHelper.loggedUserMail;

/**
 * @author Jakub Matus
 */
@Controller
@RequestMapping("/manager")
@Secured("ROLE_MANAGER")
public class ManagerController {

    @Autowired
    private FeedbackRoundService feedbackRoundService;
    @Autowired
    private EmployeeService employeeService;

    @RequestMapping(value = "/feedback-rounds", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Set<FeedbackRoundDto>> getFeedbackRounds() {
        Set<FeedbackRoundDto> feedbackRoundDtos =
            this.feedbackRoundService.getDtosForManager(loggedUserMail());
        return new ResponseEntity<>(feedbackRoundDtos, HttpStatus.OK);
    }

    /**
     * Returns all real peers (employees in the same Org Unit) of the logged in manager.
     * NOTE: this is already contained in {@link ManagerController#getFeedbackRounds()}.
     */
    @RequestMapping(value = "/peers", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<PeersStringOnlyDto> getPeers() {
        Employee employee = employeeService.findByEmailAddress(loggedUserMail());
        if (employee == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(this.employeeService.getPeersStringOfEmployee(employee), HttpStatus.OK);
    }

    @RequestMapping(value = "/select-peers/{feedbackRoundId}", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Map<String, Boolean>> selectPeers(@PathVariable("feedbackRoundId") Long feedbackRoundId,
                                                            @RequestBody PeersStringOnlyDto selectionPeers) {
        HashMap<String, Boolean> response = new HashMap<>();
        response.put(
            "success",
            this.feedbackRoundService.selectPeers(loggedUserMail(), feedbackRoundId, selectionPeers)
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
