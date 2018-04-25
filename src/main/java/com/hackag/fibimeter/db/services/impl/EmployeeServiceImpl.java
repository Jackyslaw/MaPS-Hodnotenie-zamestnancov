package com.hackag.fibimeter.db.services.impl;

import com.hackag.fibimeter.db.model.Employee;
import com.hackag.fibimeter.db.model.enumeration.AuthorityName;
import com.hackag.fibimeter.db.repository.EmployeeRepository;
import com.hackag.fibimeter.db.repository.OrgUnitRepository;
import com.hackag.fibimeter.db.services.EmployeeService;
import com.hackag.fibimeter.dto.EmployeeDto;
import com.hackag.fibimeter.dto.PeersStringOnlyDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by JRUZ on 20. 11. 2017.
 */
@Service
public class EmployeeServiceImpl implements EmployeeService {

    private static Logger log = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private OrgUnitRepository orgUnitRepository;

    @Override
    public Employee findByEmailAddress(String emailAddress) {
        Employee employee = employeeRepository.findByEmailAddress(emailAddress);
        if (employee == null) {
            log.info("Service can't find employee with login (email address) %s", emailAddress);
            return null;
        }
        return employee;
    }

    @Override
    public Employee findById(long id) {
        return employeeRepository.findOne(id);
    }

    @Override
    public Employee generatePassword(Employee employee) {
        Random random = new Random();
        StringBuilder passwordBuilder = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            int character = random.nextInt('Z' - 'A') + 'A';
            if (random.nextBoolean()) {
                character += 'z' - 'Z';
            }
            passwordBuilder.append((char) character);
        }
        employee.setPassword(passwordBuilder.toString());
        employeeRepository.saveAndFlush(employee);
        return employee;
    }

    @Override
    public Set<AuthorityName> getAuthorities(Employee employee) {
        HashSet<AuthorityName> authorityNames = new HashSet<>();
        if (!"".equals(employee.getPassword())) {
            authorityNames.add(AuthorityName.ROLE_USER);
        }
        if (employee.isAdmin()) {
            authorityNames.add(AuthorityName.ROLE_ADMIN);
        }
        if (orgUnitRepository.findByManagerEquals(employee).size() > 0) {
            authorityNames.add(AuthorityName.ROLE_MANAGER);
        }
        return authorityNames;
    }

    @Override
    public Set<AuthorityName> getAuthorities(String emailAddress) {
        return getAuthorities(employeeRepository.findByEmailAddress(emailAddress));
    }

    @Override
    public Set<EmployeeDto> getAllManagersDto() {
        return this.employeeRepository
            .findByManagedOrgUnitsNotNull()
            .stream()
            .map(EmployeeDto::mapEmployee)
            .collect(Collectors.toSet());
    }

    @Override
    public Set<EmployeeDto> getEmployeeFeedbackAccessManagersByEmailAddress(String emailAddress) {
        return this.employeeRepository.findByEmailAddress(emailAddress)
            .getEmployeeFeedbackAccesses()
            .stream()
            .filter(feedbackAccess -> feedbackAccess.getFeedbackRound().isStarted())
            .map(feedbackAccess -> EmployeeDto.mapEmployee(feedbackAccess.getFeedbackRound().getManager()))
            .collect(Collectors.toSet());
    }

    @Override
    public PeersStringOnlyDto getPeersStringOfEmployee(Employee employee) {
        return new PeersStringOnlyDto(
            this.employeeRepository.findByOrgUnit(employee.getOrgUnit())
                .stream()
                //.filter(peer -> peer.getOrgUnit().equals(employee.getOrgUnit()))
                .map(Employee::getEmailAddress)
                .collect(Collectors.toList())
        );
    }
}
