package com.hackag.fibimeter.db.repository;

import com.hackag.fibimeter.db.model.QuestionCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionCategoryRepository extends JpaRepository<QuestionCategory, Long> {
    QuestionCategory findByCategoryName(String name);
}
