package com.example.platstyle.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Timeslot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tid;
    @Column(name="WEEK", length=10)
    private String week;
    @Column(name="STARTTIME", length=10)
    private String startTime;
    @Column(name="ENDTIME", length=10)
    private String endTime;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "uid", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private AppUser appUser;
}
