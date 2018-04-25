package com.hackag.fibimeter.db.repository;

import com.hackag.fibimeter.db.model.Employee;
import com.hackag.fibimeter.db.model.OrgUnit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface OrgUnitRepository extends JpaRepository<OrgUnit, Long> {

    Set<OrgUnit> findByManagerEquals(Employee manager);

    OrgUnit findByName(String name);
}
