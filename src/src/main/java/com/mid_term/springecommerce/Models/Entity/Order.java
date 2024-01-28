package com.mid_term.springecommerce.Models.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

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

    private LocalDateTime orderDateTime;

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

    public Order(int totalPrice, LocalDateTime now, String name, String address, String phone, String email, String city) {
        this.totalPrice = totalPrice;
        this.orderDateTime = now;
        this.name = name;
        this.address = address;
        this.phoneNumber = phone;
        this.email = email;
        this.place = city;
    }
}
