package com.hackag.fibimeter.controller;

import com.hackag.fibimeter.db.model.enumeration.AuthorityName;
import com.hackag.fibimeter.db.repository.EmployeeRepository;
import com.hackag.fibimeter.dto.RoleInformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Iterator;

/**
 * Created by Ruza on 16.10.2017.
 */
@Controller
public class MainController {

    @Autowired
    private EmployeeRepository employeeRepository;

    @RequestMapping(value = {"", "/", "/fibimeter", "/fibimeter/*"}, method = RequestMethod.GET)
    public String index() {
        return "pages/index";
    }

    @RequestMapping(value = {"/role"}, method = RequestMethod.GET)
    @ResponseBody
    public RoleInformation role() {
        Iterator iterator = SecurityContextHolder.getContext().getAuthentication().getAuthorities().iterator();
        boolean isAdmin = false, isUser = false, isManager = false;
        while (iterator.hasNext()) {
            String authority = iterator.next().toString();
            if (authority.equals(AuthorityName.ROLE_ADMIN.toString())) {
                isAdmin = true;
            }
            if (authority.equals(AuthorityName.ROLE_USER.toString())) {
                isUser = true;
            }
            if (authority.equals(AuthorityName.ROLE_MANAGER.toString())) {
                isManager = true;
            }
        }
        return new RoleInformation(isAdmin, isManager, isUser);
    }
}
