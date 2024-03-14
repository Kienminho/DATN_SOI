package com.mid_term.springecommerce.Models.RequestModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CategoryRequest {
    private Long id;
    private String name;
    private String description;
}
