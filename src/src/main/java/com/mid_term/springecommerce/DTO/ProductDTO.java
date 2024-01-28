package com.mid_term.springecommerce.DTO;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class ProductDTO {

    private String name;
    private String category;
    private String description;
    private int price;

}
