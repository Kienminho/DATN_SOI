package com.mid_term.springecommerce.APIController;

import com.mid_term.springecommerce.Models.Entity.Guarantee;
import com.mid_term.springecommerce.Models.Entity.RegisterGuarantee;
import com.mid_term.springecommerce.Models.RequestModel.RegisterGuaranteeRequest;
import com.mid_term.springecommerce.Repositorys.GuaranteeRepository;
import com.mid_term.springecommerce.Repositorys.RegisterGuaranteeRepository;
import com.mid_term.springecommerce.Utils.Enum.WarrantyStatus;
import com.mid_term.springecommerce.Utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/guarantee/")
public class GuaranteeController {
    @Autowired
    private GuaranteeRepository guaranteeRepository;

    @Autowired
    private RegisterGuaranteeRepository registerGuaranteeRepository;
    @GetMapping("get-guarantee-by-user")
    public Object getGuaranteeByUser() {
        try {
            List<Guarantee> guarantees = guaranteeRepository.getGuaranteeByUser(Utils.userLogin.getUsername());
            return Response.createSuccessResponseModel(guarantees.size(), guarantees);
        }
        catch (Exception e) {
            return Response.createErrorResponseModel(e.getMessage(), false);
        }
    }

    @PostMapping("register-guarantee")
    public Object registerGuarantee(@RequestBody RegisterGuaranteeRequest req) {
        try {
            //check date guarantee
            Guarantee guarantee = guaranteeRepository.getGuaranteeByCode(req.getCodeGuarantee());
            if(guarantee == null) {
                return Response.createErrorResponseModel("Mã bảo hành không tồn tại", false);
            }
            if(guarantee.getWarrantyPeriodInMonths() <= 0) {
                return Response.createErrorResponseModel("Sản phẩm không còn bảo hành", false);
            }

            //handle register guarantee
            WarrantyStatus status = WarrantyStatus.REGISTER;

            RegisterGuarantee registerGuarantee = new RegisterGuarantee(req.getCodeGuarantee(),Utils.userLogin.getId(), guarantee.getProductName(), Utils.userLogin.getFullName(), req.getAddress(), req.getPhone(), req.getDescription(), status.getDescription());
            registerGuaranteeRepository.save(registerGuarantee);
            return Response.createSuccessResponseModel(0, true);
        }
        catch (Exception e) {
            return Response.createErrorResponseModel(e.getMessage(), false);
        }
    }

    @GetMapping("get-history-register-guarantee")
    public Object getHistoryRegisterGuarantee() {
        try {
            List<RegisterGuarantee> registerGuarantees = registerGuaranteeRepository.getRegisterGuaranteeByIdUser(Utils.userLogin.getId());
            return Response.createSuccessResponseModel(registerGuarantees.size(), registerGuarantees);
        }
        catch (Exception e) {
            return Response.createErrorResponseModel(e.getMessage(), false);
        }
    }
}
