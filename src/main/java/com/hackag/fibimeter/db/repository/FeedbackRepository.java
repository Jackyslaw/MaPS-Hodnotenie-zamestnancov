package com.hackag.fibimeter.db.repository;

import com.hackag.fibimeter.db.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
}
