package com.mid_term.springecommerce.Models.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    private int totalPrice;

    @Column(columnDefinition = "datetime")
    private Date orderDateTime;

    @Column(columnDefinition = "nvarchar(75)")
    private String name;

    @Column(columnDefinition = "nvarchar(255)")
    private String address;

    @Column(name = "phone_number")
    private String phoneNumber;

    private String email;

    @Column(columnDefinition = "nvarchar(255)")
    private String requestNote;

    @Column(name = "city", columnDefinition = "nvarchar(50)")
    private String place;

    private String paymentMethod; //C- Cash, T-Banking transfer

    private Boolean issueInvoice;

    private int status; //, 1-Confirmed, 2-Delivering, 3-Delivered, 4-Cancelled
    public Order(int totalPrice, Date now, String name, String address, String phone, String email, String city, String paymentMethod) {
        this.totalPrice = totalPrice;
        this.orderDateTime = now;
        this.name = name;
        this.address = address;
        this.phoneNumber = phone;
        this.email = email;
        this.place = city;
        this.paymentMethod = paymentMethod;
        this.issueInvoice = false;
        this.status = 1;
    }
}
