package com.mid_term.springecommerce.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class InvoiceItemResponse {
    private String invoiceCode;
    private String nameProduct;
    private int quantity;
    private int unitPrice;
    private Date createdDate;
}
