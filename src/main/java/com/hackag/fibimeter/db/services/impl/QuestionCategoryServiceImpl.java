package com.hackag.fibimeter.db.services.impl;

import com.hackag.fibimeter.db.model.Question;
import com.hackag.fibimeter.db.model.QuestionCategory;
import com.hackag.fibimeter.db.repository.QuestionCategoryRepository;
import com.hackag.fibimeter.db.repository.QuestionRepository;
import com.hackag.fibimeter.db.services.QuestionCategoryService;
import com.hackag.fibimeter.dto.QuestionCategoryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class QuestionCategoryServiceImpl implements QuestionCategoryService {

    @Autowired
    private QuestionCategoryRepository questionCategoryRepository;
    @Autowired
    private QuestionRepository questionRepository;

    @Override
    public QuestionCategory getQuestionCategoryByName(String name) {
        return questionCategoryRepository.findByCategoryName(name);
    }

    @Override
    public Set<String> getAllNames() {
        return questionCategoryRepository.findAll()
            .stream()
            .map(QuestionCategory::getCategoryName)
            .collect(Collectors.toSet());
    }

    @Override
    public Set<QuestionCategoryDto> getAllDtos() {
        return questionCategoryRepository.findAll()
            .stream()
            .map(QuestionCategoryDto::mapQuestionCategory)
            .collect(Collectors.toSet());
    }

    @Override
    @Transactional
    public void addQuestionToCategory(long questionID, String categoryName) {
        QuestionCategory questionCategory = questionCategoryRepository.findByCategoryName(categoryName);

        Question question = questionRepository.findOne(questionID);
        questionCategory.getQuestions().add(question);
        question.setQuestionCategory(questionCategory);
        questionCategoryRepository.save(questionCategory);
        questionRepository.save(question);
    }
}
