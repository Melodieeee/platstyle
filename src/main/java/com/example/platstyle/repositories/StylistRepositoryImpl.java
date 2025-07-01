package com.example.platstyle.repositories;

import com.example.platstyle.entities.Stylist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class StylistRepositoryImpl implements StylistRepositoryCustom {

    @Autowired
    @Lazy
    StylistRepository stylistRepository;
    @PersistenceContext
    private EntityManager entityManager;

    public List<Object[]> findByFilter(String sql) {
        List<Object[]> list = entityManager.createNativeQuery(sql).getResultList();
        return list;
    }
}
