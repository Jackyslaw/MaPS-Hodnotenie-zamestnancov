package com.hackag.fibimeter.db.services;

import com.hackag.fibimeter.db.model.QuestionCategory;
import com.hackag.fibimeter.dto.QuestionCategoryDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 * @author Jakub Matus
 */
public interface QuestionCategoryService {

    /**
     * Gets {@link QuestionCategory} by name passed as parameter
     *
     * @param name Question category name
     * @return null if {@link QuestionCategory} with passed name does not exist
     */
    QuestionCategory getQuestionCategoryByName(String name);

    /**
     * Gets all question category names.
     *
     * @return names of all question categories.
     */
    Set<String> getAllNames();

    /**
     * Gets all question category names.
     *
     * @return names of all question categories.
     */
    Set<QuestionCategoryDto> getAllDtos();

    /**
     * add question to category
     *
     * @param questionId
     * @param categoryName
     */
    @Transactional
    void addQuestionToCategory(long questionId, String categoryName);
}
