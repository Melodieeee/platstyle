package com.example.platstyle.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Customer {
    @Id
    private Long uid;
    private String street;
    private String street2;
    @Column(name="CITY", length=50)
    private String city;
    @Column(name="PROVINCE", length=50)
    private String province;
    @Column(name="POSTAL", length=7)
    private String postal;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date birthday;
    private String photo;

    public String getAddress() {
        if(postal==null) return "";
        return this.street+", "+city+", "+province+" "+postal;
    }
}
