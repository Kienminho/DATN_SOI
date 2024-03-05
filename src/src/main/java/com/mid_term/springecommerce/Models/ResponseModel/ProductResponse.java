package com.mid_term.springecommerce.Models.ResponseModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProductResponse {
    public Long id;
    public String name;
    public String imageUrl;
    public String salePrice;
}
