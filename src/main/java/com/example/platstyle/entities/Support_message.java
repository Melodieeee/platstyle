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
public class Support_message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long smid;
    private String message;
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createDate;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "suid", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private AppUser sender;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sid", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Support support;

    public String getSenderName() {
        return sender.getFirstName();
    }
}
