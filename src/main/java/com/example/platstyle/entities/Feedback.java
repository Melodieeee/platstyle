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
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fid;
    private String comment;
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createDate;
    private double rate;
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "oid", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Order order;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sid", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Stylist stylist;
}
