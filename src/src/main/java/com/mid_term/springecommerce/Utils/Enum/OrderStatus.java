package com.mid_term.springecommerce.Utils.Enum;

import lombok.Getter;
@Getter
public enum OrderStatus {
    WAITING("Đang chờ xác nhận"),
    HANDEL("Đang vận chuyển"),
    DONE("Hoàn thành");

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }
}
