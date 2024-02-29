package com.mid_term.springecommerce.Models.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class OrderOfShipper {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    public Long shipperId;
    @Column(columnDefinition = "varchar(15)")
    public String customerPhone;
    @Column(columnDefinition = "nvarchar(50)")
    public String customerName;
    @Column(columnDefinition = "nvarchar(255)")
    public String customerAddress;
    @Column(columnDefinition = "varchar(5)")
    public String paymentMethod;
    public int orderMoney;
    public int collectionMoney;
    public int status; // 0: chưa giao, 1: đã giao, 2: đã hủy
    @Column(columnDefinition = "datetime")
    public Date createdDate;

    public OrderOfShipper(Long shipperId,String customerPhone, String customerName, String customerAddress,String paymentMethod, int orderMoney, int collectionMoney, int status, Date createdDate) {
        this.shipperId = shipperId;
        this.customerPhone = customerPhone;
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.paymentMethod = paymentMethod;
        this.orderMoney = orderMoney;
        this.collectionMoney = collectionMoney;
        this.status = status;
        this.createdDate = createdDate;
    }
}
