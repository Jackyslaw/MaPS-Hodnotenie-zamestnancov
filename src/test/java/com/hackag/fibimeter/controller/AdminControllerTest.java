package com.hackag.fibimeter.controller;

import com.hackag.fibimeter.db.repository.OrgUnitRepository;
import com.hackag.fibimeter.dto.EmployeeDto;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class AdminControllerTest {

    @Autowired
    private AdminController adminController;
    @Autowired
    private OrgUnitRepository orgUnitRepository;

    @Test
    public void getAllManagers() {
        loginAdminSecurityContext();
        Set<EmployeeDto> allManagers = adminController.getAllManagers();
        Assert.assertTrue("No results, please populate the database first", allManagers.size() > 0);
        Assert.assertTrue(allManagers.stream().noneMatch(c -> c.getManagedOrgUnitIds().size() == 0));
        allManagers.forEach(dto ->
            dto.getManagedOrgUnitIds().forEach(
                managedOrgUnitId -> Assert.assertTrue(
                    orgUnitRepository.findOne(managedOrgUnitId)
                        .getManager()
                        .getIdEmployee()
                        .equals(dto.getIdEmployee())
                )
            )
        );
    }

    private void loginAdminSecurityContext() {
        SecurityContextHolder.getContext().setAuthentication(
            new UsernamePasswordAuthenticationToken("john.doe@t-systems.com", "password")
        );
    }
}
