package com.mid_term.springecommerce.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRequest {
    private String id;
    private String name;
    private String price;
    private String description;
}
