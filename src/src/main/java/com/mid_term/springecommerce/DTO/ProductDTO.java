package com.mid_term.springecommerce.DTO;

import lombok.*;

import java.sql.Date;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    private String name;
    private String categoryName;
    String image;
    private String description;
    private int quantity;
    private Date createdDate;
    private int importPrice;
    private int priceSale;

    public ProductDTO(String name,String categoryName, String description, int quantity, Date createdDate, int importPrice, int priceSale) {
        this.name = name;
        this.categoryName = categoryName;
        this.description = description;
        this.quantity = quantity;
        this.createdDate = createdDate;
        this.importPrice = importPrice;
        this.priceSale = priceSale;
    }
}
