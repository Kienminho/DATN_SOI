package com.mid_term.springecommerce.Models.RequestModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@Getter
@Setter
public class RegisterGuaranteeRequest {
    private String codeGuarantee;
    private String nameProduct;
    private String nameCustomer;
    private String address;
    private String phone;
    private Date ComplainDate;
    private Date GuaranteeDate;
    private String description;
    private String status;
}
