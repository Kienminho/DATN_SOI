package com.mid_term.springecommerce.Models.RequestModel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemRequest {
    public Long productId;
    public int quantity;
    public int unitPrice;
}
