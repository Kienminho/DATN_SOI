package com.mid_term.springecommerce.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderOfShipperRequest {
    public Long orderId;
    public Long shipperId;
    public int status;
}
