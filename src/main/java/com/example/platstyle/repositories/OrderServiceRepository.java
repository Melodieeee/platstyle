package com.example.platstyle.repositories;

import com.example.platstyle.entities.Order;
import com.example.platstyle.entities.Order_service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.Optional;

public interface OrderServiceRepository extends JpaRepository<Order_service, Long> {
    @Override
    Optional<Order_service> findById(Long aLong);

    @Modifying
    @Query(value="delete from order_service where oid = ?1", nativeQuery = true)
    @Transactional
    void deleteByOid(Order order);
}
