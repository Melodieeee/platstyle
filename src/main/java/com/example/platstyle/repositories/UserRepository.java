package com.example.platstyle.repositories;

import com.example.platstyle.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<AppUser, Long> {

    List<AppUser> findAll();
    Optional<AppUser> findByEmail(String email);
    Optional<AppUser> findAllByUid(Long uid);

}
