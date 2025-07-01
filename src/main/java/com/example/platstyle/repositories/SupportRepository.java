package com.example.platstyle.repositories;

import com.example.platstyle.entities.AppUser;
import com.example.platstyle.entities.Support;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SupportRepository extends JpaRepository<Support, Long> {
    Optional<Support> findAllBySid(Long sid);
    List<Support> findAllByAppUser(AppUser appUser);

}
