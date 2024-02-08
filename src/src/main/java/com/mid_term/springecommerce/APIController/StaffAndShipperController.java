package com.mid_term.springecommerce.APIController;

import com.mid_term.springecommerce.Models.Entity.Role;
import com.mid_term.springecommerce.Models.Entity.StaffAndShipper;
import com.mid_term.springecommerce.Repositorys.RoleRepository;
import com.mid_term.springecommerce.Services.MailService;
import com.mid_term.springecommerce.Utils.Utils;
import com.mid_term.springecommerce.Repositorys.StaffAndShipperRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/api/staff-and-shipper/")
public class StaffAndShipperController {
    @Autowired
    private StaffAndShipperRepository staffAndShipperRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private MailService mailService;

    @PostMapping("create-staff")
    public Object createStaff(@RequestBody Map<String, String> req) {
        String fullName = req.get("fullName");
        String email = req.get("email");
        String address = req.get("address");
        String phoneNumber = req.get("phoneNumber");
        StaffAndShipper existUser = staffAndShipperRepository.getUserByEmail(email);
        if (existUser != null) {
            return Response.createErrorResponseModel("Nhân viên đã tồn tại", false);
        }

        try {
            Role r = roleRepository.getRoleById(5L);
            StaffAndShipper user = new StaffAndShipper(
                    Utils.GetUserNameByEmail(email),
                    Utils.GetUserNameByEmail(email),
                    r,
                    fullName,
                    email,
                    address,
                    phoneNumber,
                    null,
                    Utils.GenerateRandomToken(100),
                    new Date()

            );
            staffAndShipperRepository.save(user);
            mailService.sendMail(user.getActivationToken(), user.getEmail(), user.getUserName());
            return Response.createSuccessResponseModel(0, true);
        } catch (Exception ex) {
            return Response.createErrorResponseModel("Vui lòng đợi, hệ thống đang gặp vấn đề", ex.getMessage());
        }
    }

    @DeleteMapping("delete/{id}")
    public Object deleteStaff(@PathVariable Long id) {
        try {
            StaffAndShipper s = staffAndShipperRepository.getUserById(id);
            if (s == null) {
                return Response.createErrorResponseModel("Nhân viên không tồn tại", false);
            }
            s.setIsDeleted(!s.getIsDeleted());
            staffAndShipperRepository.save(s);
            return Response.createSuccessResponseModel(0, true);
        } catch (Exception ex) {
            return Response.createErrorResponseModel("Vui lòng đợi, hệ thống đang gặp vấn đề", ex.getMessage());
        }
    }

    @PostMapping("reactive")
    public Object reactiveStaff(@RequestBody Map<String, String> req) {
        Long id = Long.parseLong(req.get("id"));
        StaffAndShipper existUser = staffAndShipperRepository.getUserById(id);
        if (existUser == null) {
            return Response.createErrorResponseModel("Nhân viên không tồn tại", false);
        }

        try {
            existUser.setActivationToken(Utils.GenerateRandomToken(100));
            existUser.setActivationExpires(new Date());
            staffAndShipperRepository.save(existUser);
            mailService.sendMail(existUser.getActivationToken(), existUser.getEmail(), existUser.getUserName());
            return Response.createSuccessResponseModel(0, true);
        } catch (Exception ex) {
            return Response.createErrorResponseModel("Vui lòng đợi, hệ thống đang gặp vấn đề", ex.getMessage());
        }
    }

    @GetMapping("get-all-staff")
    public Object getAllStaff() {
        try {
            return Response.createSuccessResponseModel(0, staffAndShipperRepository.getAllUser());
        } catch (Exception ex) {
            return Response.createErrorResponseModel("Vui lòng đợi, hệ thống đang gặp vấn đề", ex.getMessage());
        }
    }

}
