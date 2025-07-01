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
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pid;
    private String cardNumber;
    private String expireDate;
    private String cardHolder;
    private String securityCode;
    private String postalCode;
    private double amount;
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createDate;
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "oid", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Order order;
}
