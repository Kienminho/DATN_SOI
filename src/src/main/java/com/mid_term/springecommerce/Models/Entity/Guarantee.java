package com.mid_term.springecommerce.Models.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class Guarantee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String codeGuarantee;

    @Column(columnDefinition = "nvarchar(100)")
    private String userName;

    private Long productId;

    @Column(columnDefinition = "nvarchar(100)")
    private String productName;

    private Date purchaseDate;

    private Date expirationDate;

    private int warrantyPeriodInMonths;

    public Guarantee(String codeGuarantee,String userName, Long productId, String productName, Date purchaseDate, Date expirationDate, int warrantyPeriodInMonths) {
        this.codeGuarantee = codeGuarantee;
        this.userName = userName;
        this.productId = productId;
        this.productName = productName;
        this.purchaseDate = purchaseDate;
        this.expirationDate = expirationDate;
        this.warrantyPeriodInMonths = warrantyPeriodInMonths;
    }
}
