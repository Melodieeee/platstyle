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
@Entity(name="orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long oid;
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createDate;
    private String note;
    private String address;
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date scheduleDate;
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date arriveTime;
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date finishTime;
    private double totalPrice;
    private double tip;
    private int status;

    // 0 without payment
    // 1 waiting stylist
    /*
    customer 			stylist
1. 				   	  confirm request
2. confirm arrive
3. 				      setPrice
4. confirm Price
5.					  confirm service
6. confirm service
7. complete feedback
8. complete no feedback button
99. cancel
98. Stylist doesn't arrive
97. Stylist doesn't finish service
96. user feel doesn't finish service
*/
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "uid", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private AppUser appUser;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sid", nullable = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Stylist stylist;
    @OneToMany(mappedBy="order",cascade = CascadeType.PERSIST)
    private List<Order_service> services;

    @OneToMany(mappedBy="order",cascade = CascadeType.PERSIST)
    private List<Support> supports;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Payment payment;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Feedback feedback;

    public String getStylistName() {
        return services.get(0).getService().getStylistName();
    }

    public String getServiceName() {
        return services.get(0).getService().getServiceName();
    }

    public String getOrderNum() {
        return String.format("%05d", this.getOid());
    }

    public String getDate() {
        return new SimpleDateFormat("MM/dd").format(this.createDate);
    }

    public String getFormatScheduleDate() {
        return new SimpleDateFormat("yyyy/MM/dd HH:mm").format(this.scheduleDate);
    }

    public String getCustomerStatus() {
        String txtStatus="";
        switch (this.status) {
            case 1:
                txtStatus = "Waiting Confirm";
                break;
            case 2:
                txtStatus = "Please Confirm";
                break;
            case 3:
                txtStatus = "Waiting Set";
                break;
            case 4:
                txtStatus = "Please Confirm";
                break;
            case 5:
                txtStatus = "Service Providing";
                break;
            case 6:
                txtStatus = "Please Confirm";
                break;
            case 7:
                txtStatus = "Complete";
                break;
            case 8:
                txtStatus = "Complete";
                break;
            case 99:
                txtStatus = "Cancel";
                break;
            case 98:
                txtStatus = "Absent";
                break;

        }
        return txtStatus;
    }

    public String getStylistStatus() {
        String txtStatus="";
        switch (this.status) {
            case 1:
                txtStatus = "Please Confirm";
                break;
            case 2:
                txtStatus = "Waiting Confirm";
                break;
            case 3:
                txtStatus = "Please Set";
                break;
            case 4:
                txtStatus = "Waiting Confirm";
                break;
            case 5:
                txtStatus = "Service Providing";
                break;
            case 6:
                txtStatus = "Waiting Confirm";
                break;
            case 7:
                txtStatus = "Complete";
                break;
            case 8:
                txtStatus = "Complete";
                break;
            case 99:
                txtStatus = "Cancel";
                break;
            case 98:
                txtStatus = "Absent";
                break;
        }
        return txtStatus;
    }

}
