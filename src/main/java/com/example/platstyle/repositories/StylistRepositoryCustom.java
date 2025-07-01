package com.example.platstyle.repositories;

import com.example.platstyle.entities.Stylist;

import java.util.List;

public interface StylistRepositoryCustom {
    List<Object[]> findByFilter(String sql);
}
