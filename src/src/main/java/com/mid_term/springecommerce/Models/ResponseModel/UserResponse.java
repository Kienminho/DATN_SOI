package com.mid_term.springecommerce.Models.ResponseModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserResponse {
    private String username;
    private String fullName;
    private String email;
    private String phone;
    private String address;
    private String gender;
}
