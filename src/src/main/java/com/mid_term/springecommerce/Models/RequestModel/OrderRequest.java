package com.mid_term.springecommerce.Models.RequestModel;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class OrderRequest {
    public String name;
    public String address;
    public String phone;
    public String email;
    public String city;
    public int totalPrice;

    public List<OrderItemRequest> orderItems;
}

