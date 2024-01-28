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
public class RegisterGuarantee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long idUser;
    private String codeGuarantee;

    @Column(columnDefinition = "nvarchar(100)")
    private String nameProduct;

    @Column(columnDefinition = "nvarchar(50)")
    private String nameCustomer;

    @Column(columnDefinition = "nvarchar(255)")
    private String address;

    private String phone;

    private Date complainDate = new Date();

    private Date guaranteeDate = null;

    @Column(columnDefinition = "nvarchar(255)")
    private String description;

    @Column(columnDefinition = "nvarchar(80)")
    private String status;

    public RegisterGuarantee(String codeGuarantee, Long idUser, String nameProduct, String nameCustomer, String address, String phone, String description, String status) {
        this.codeGuarantee = codeGuarantee;
        this.idUser = idUser;
        this.nameProduct = nameProduct;
        this.nameCustomer = nameCustomer;
        this.address = address;
        this.phone = phone;
        this.description = description;
        this.status = status;
    }
}
