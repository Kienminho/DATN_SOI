package com.mid_term.springecommerce.DTO;

import com.mid_term.springecommerce.Models.Entity.Cart;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class InvoiceRequest {
    private String phoneNumber;
    private String fullName;
    private String address;
    private int quantity;
    private int totalMoney;
    private int receiveMoney;
    private int moneyBack;
    private List<CartRequest> items;
}
