package com.mid_term.springecommerce.APIController;

import com.mid_term.springecommerce.DTO.GuaranteeRequest;
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

            RegisterGuarantee rg  = registerGuaranteeRepository.getRegisterGuarantee(Utils.idUserLogin, req.getCodeGuarantee());
            if(rg != null ) {

                switch (rg.getStatus()) {
                    case REGISTER, HANDEL -> {
                        return Response.createErrorResponseModel("Bảo hành đang được xử lý, không thể đăng ký tiếp!", false);
                    }
                    case DONE ->  {
                        return Response.createErrorResponseModel("Sản phẩm đã được bảo hành thành công!", false);
                    }
                    case CANCELED ->  {
                        return Response.createErrorResponseModel("Sản phẩm không đủ điều kiện bảo hành!", false);
                    }
                }
            }
            RegisterGuarantee registerGuarantee = new RegisterGuarantee(req.getCodeGuarantee(),Utils.userLogin.getId(), guarantee.getProductName(), Utils.userLogin.getFullName(), req.getAddress(), req.getPhone(), req.getDescription(), WarrantyStatus.REGISTER);
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

    @GetMapping("get-all-guarantee")
    public Object getAllGuarantee() {
        try {
            List<RegisterGuarantee> guarantees = registerGuaranteeRepository.findAll();
            return Response.createSuccessResponseModel(guarantees.size(), guarantees);
        }
        catch (Exception e) {
            return Response.createErrorResponseModel(e.getMessage(), false);
        }
    }

    @PutMapping("update-guarantee")
    public Object updateGuarantee(@RequestBody GuaranteeRequest req) {
        try {
            RegisterGuarantee registerGuarantee = registerGuaranteeRepository.getReferenceById(req.getGuaranteeId());
            if(req.getGuaranteeDate() != null) {
                registerGuarantee.setGuaranteeDate(req.getGuaranteeDate());
                registerGuarantee.setStatus(req.getStatus());
            }
            else {
                registerGuarantee.setStatus(req.getStatus());
                registerGuarantee.setNameStaff(req.getNameStaff());
            }

            registerGuaranteeRepository.save(registerGuarantee);
            return Response.createSuccessResponseModel(0, true);
        }
        catch (Exception e) {
            return Response.createErrorResponseModel(e.getMessage(), false);
        }
    }

    @GetMapping("get-guarantee-by-employee")
    public Object getGuaranteeByEmployee() {
        try {
            List<RegisterGuarantee> registerGuarantees = registerGuaranteeRepository.getRegisterGuaranteeByIdStaff(Utils.staffAndShipperLogin.getFullName());
            return Response.createSuccessResponseModel(registerGuarantees.size(), registerGuarantees);
        }
        catch (Exception e) {
            return Response.createErrorResponseModel(e.getMessage(), false);
        }
    }
}
