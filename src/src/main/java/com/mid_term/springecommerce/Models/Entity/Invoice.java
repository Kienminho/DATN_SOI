package com.mid_term.springecommerce.Models.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table
public class Invoice {
    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private String invoiceCode;
    private String nameCustomer;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private StaffAndShipper salesStaff;
    private int receiveMoney;
    private int excessMoney;
    private int totalMoney;
    private int quantity;
    @Column(columnDefinition = "datetime")
    private Date createdDate;

    public Invoice(String invoiceCode, String customer, StaffAndShipper salesStaff, int receiveMoney, int excessMoney, int totalMoney, int quantity, Date createdDate) {
        this.invoiceCode = invoiceCode;
        this.nameCustomer = customer;
        this.salesStaff = salesStaff;
        this.receiveMoney = receiveMoney;
        this.excessMoney = excessMoney;
        this.totalMoney = totalMoney;
        this.quantity = quantity;
        this.createdDate = createdDate;
    }
}
