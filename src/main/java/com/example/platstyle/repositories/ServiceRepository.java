package com.example.platstyle.repositories;

import com.example.platstyle.entities.Customer;
import com.example.platstyle.entities.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ServiceRepository  extends JpaRepository<Service, Long> {
    @Query(value = "select * from service  where uid = ?1",nativeQuery = true)
    List<Service> findAllByUid (long uid);

    @Query(value = "select * from service  where uid = ?1 AND gender = ?2",nativeQuery = true)
    List<Service> findAllByUidANDGender(long uid, String gender);
    Optional<Service> findById(Long sid);

}
