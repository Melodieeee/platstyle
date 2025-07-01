package com.example.platstyle.repositories;

import com.example.platstyle.entities.Portfolio;
import com.example.platstyle.entities.Service;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PortfolioRepository  extends JpaRepository<Portfolio, Long> {

    List<Portfolio> findAllByService(Service service);

}
