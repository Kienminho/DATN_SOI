package com.mid_term.springecommerce.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartRequest {
    public Long id;
    public Long idProduct;
    public Long idSalePeople;
    public String imageLink;
    public String name;
    public int quantity;
    public int salePrice;
    public int totalMoney;

    public CartRequest(String name, int quantity, int salePrice, int totalMoney) {
        this.name = name;
        this.quantity = quantity;
        this.salePrice = salePrice;
        this.totalMoney = totalMoney;
    }
}
