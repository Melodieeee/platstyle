package com.example.platstyle.repositories;

import com.example.platstyle.entities.AppUser;
import com.example.platstyle.entities.Order;
import com.example.platstyle.entities.Stylist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query(value = "select * from orders where uid = ?1 and status != 0",nativeQuery = true)
    List<Order> findAllByUid(long uid);
    @Query(value = "select * from orders where sid = ?1 and status != 0",nativeQuery = true)
    List<Order> findAllBySid(long sid);
    Optional<Order> findAllByAppUserAndStatus(AppUser appUser, int status);
    List<Order> findAllByStylistAndStatusBetween(Stylist stylist, int from, int to);
}
