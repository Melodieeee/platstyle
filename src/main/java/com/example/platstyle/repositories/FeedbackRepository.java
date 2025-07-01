package com.example.platstyle.repositories;

import com.example.platstyle.entities.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
}
