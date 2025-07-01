package com.example.platstyle.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Order_service {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long osid;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sid", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Service service;
    private String lengthOfHair;
    private double price;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "oid", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Order order;
}