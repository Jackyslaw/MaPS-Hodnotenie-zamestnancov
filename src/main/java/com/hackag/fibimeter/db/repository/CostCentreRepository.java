package com.hackag.fibimeter.db.repository;

import com.hackag.fibimeter.db.model.CostCentre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CostCentreRepository extends JpaRepository<CostCentre, Long> {
}
