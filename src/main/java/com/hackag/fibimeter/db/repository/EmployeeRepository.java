package com.hackag.fibimeter.db.repository;

import com.hackag.fibimeter.db.model.Employee;
import com.hackag.fibimeter.db.model.OrgUnit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Employee findByEmailAddress(String login);

    Set<Employee> findAllBySurname(String surname);

    Set<Employee> findByManagedOrgUnitsNotNull();

    Set<Employee> findByOrgUnit(OrgUnit orgUnit);
}
