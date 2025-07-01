package com.example.platstyle.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Service {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sid;
    @Column(name="GENDER", length=10)
    private String gender;
    @Column(name="serviceName", length=50)
    private String serviceName;
    private double minPrice;
    private double maxPrice;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "uid", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private AppUser appUser;
    @OneToMany(mappedBy="service",cascade = CascadeType.PERSIST)
    private List<Portfolio> portfolios;

    public String getStylistName() {
        return appUser.getFirstName()+ " " + appUser.getLastName();
    }

    public long getStylistId() {
        return this.appUser.getStylistId();
    }
    public String getGender() {
        if(this.gender.equals("F")) return "Female";
        else if(this.gender.equals("M")) return "Male";
        else return "No";
    }
}
