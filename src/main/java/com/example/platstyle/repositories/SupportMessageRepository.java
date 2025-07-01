package com.example.platstyle.repositories;

import com.example.platstyle.entities.Support_message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupportMessageRepository extends JpaRepository<Support_message, Long> {
}
