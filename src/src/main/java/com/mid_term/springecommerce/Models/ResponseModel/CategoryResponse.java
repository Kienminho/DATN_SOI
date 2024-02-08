package com.mid_term.springecommerce.Models.ResponseModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class CategoryResponse {
    private Long id;
    private String categoryName;
    private String description;
    private Long totalProducts;
    private Date createdDate;
}
