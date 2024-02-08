package com.mid_term.springecommerce.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRequest {
    private Long barCode;
    private String name;
    private int importPrice;
    private int priceSale;
    private int quantityNumber;
}
