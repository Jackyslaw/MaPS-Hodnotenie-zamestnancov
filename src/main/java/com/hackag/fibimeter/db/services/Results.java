package com.hackag.fibimeter.db.services;

import java.util.Map;

/**
 * @author Jakub Matus
 */
public interface Results {

    /**
     * Get average result of questions in category
     *
     * @param managerId
     * @param categoryName
     * @return null if Manager with managerId or Category with category name does not exist
     */
    long getAverageResultOfCategory(long managerId, String categoryName);

    /**
     * Get result of every single question in category as Map
     *
     * @param managerId
     * @param categoryName
     * @return null if Manager with managerId or Category with category name does not exist
     */
    Map<String, Long> getEveryQuestionInCategoryResult(long managerId, String categoryName);

    /**
     * Get result of every single question as Map
     *
     * @param managerId
     * @param categoryName
     * @return null if Manager with managerId or Category with category name does not exist
     */
    Map<String, Long> getEveryQuestionInResult(long managerId, String categoryName);
}
