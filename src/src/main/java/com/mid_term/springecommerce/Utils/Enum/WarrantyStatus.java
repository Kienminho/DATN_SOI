package com.mid_term.springecommerce.Utils.Enum;

import lombok.Getter;

@Getter
public enum WarrantyStatus {
    REGISTER("Đã đăng kí bảo hành"),
    WAITING("Đang chờ xử lý"),
    HANDEL("Đang xử lý"),
    DONE("Đã xử lý");

    private final String description;

    WarrantyStatus(String description) {
        this.description = description;
    }
}


