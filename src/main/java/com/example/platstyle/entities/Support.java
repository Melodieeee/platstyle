package com.example.platstyle.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Support {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sid;
    private String category;
    private String subject;
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createDate;
    @Column(columnDefinition = "boolean default false")
    private boolean status;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "uid", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private AppUser appUser;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "oid", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Order order;
    @OneToMany(mappedBy="support",cascade = CascadeType.ALL)
    private List<Support_message> messages;
    public String getOrderNum() {
        if(this.order!=null) {
            return order.getOrderNum();
        } else {
            return "";
        }
    }

    public String getUserName() {
        return appUser.getFirstName();
    }

    public String getFormatCreateDate(){
        return  new SimpleDateFormat("yyyy/MM/dd").format(this.createDate);
    }
}
