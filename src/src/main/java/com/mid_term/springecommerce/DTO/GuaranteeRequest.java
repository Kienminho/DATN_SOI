package com.mid_term.springecommerce.DTO;

import com.mid_term.springecommerce.Utils.Enum.WarrantyStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
public class GuaranteeRequest {
    public Long guaranteeId;
    public String nameStaff;
    public WarrantyStatus status;
    public Date guaranteeDate;
}
