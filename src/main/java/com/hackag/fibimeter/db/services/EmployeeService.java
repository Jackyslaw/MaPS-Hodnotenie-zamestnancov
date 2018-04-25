package com.hackag.fibimeter.db.services;

import com.hackag.fibimeter.db.model.Employee;
import com.hackag.fibimeter.db.model.enumeration.AuthorityName;
import com.hackag.fibimeter.dto.EmployeeDto;
import com.hackag.fibimeter.dto.PeersStringOnlyDto;

import java.util.Set;

/**
 * Created by JRUZ on 20. 11. 2017.
 */
public interface EmployeeService {

    /**
     * Return {@link Employee} with email address (login) passed as parameter
     *
     * @param emailAddress user login
     * @return null if login does not match login of any Employee
     */
    Employee findByEmailAddress(String emailAddress);

    /**
     * Return {@link Employee} with id passed as parameter
     *
     * @param id
     * @return null if id does not match any Employee.id
     */
    Employee findById(long id);

    /**
     * Return {@link Employee} passed as parameter with filled password
     *
     * @param employee
     * @return
     */
    Employee generatePassword(Employee employee);

    /**
     * Return authorities of a user.
     *
     * @param employee
     * @return
     */
    Set<AuthorityName> getAuthorities(Employee employee);

    /**
     * Return authorities of a user located by login.
     *
     * @param emailAddress login
     * @return
     */
    Set<AuthorityName> getAuthorities(String emailAddress);

    /**
     * Return all employees that are managers in a DTO.
     *
     * @return all managers in a DTO
     */
    Set<EmployeeDto> getAllManagersDto();

    /**
     * Return all managers that are accessible to an employee (selected by his email address login) for a new feedback.
     *
     * @param emailAddress login of employee whose feedback accesses are being queried
     * @return managers that the employee has access to create feedback for
     */
    Set<EmployeeDto> getEmployeeFeedbackAccessManagersByEmailAddress(String emailAddress);

    PeersStringOnlyDto getPeersStringOfEmployee(Employee employee);
}
