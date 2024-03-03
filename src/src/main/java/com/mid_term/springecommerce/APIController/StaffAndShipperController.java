package com.mid_term.springecommerce.APIController;

import com.mid_term.springecommerce.DTO.OrderOfShipperRequest;
import com.mid_term.springecommerce.DTO.StaffAndShipperDTO;
import com.mid_term.springecommerce.Models.Entity.*;
import com.mid_term.springecommerce.Repositorys.*;
import com.mid_term.springecommerce.Services.MailService;
import com.mid_term.springecommerce.Utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/staff-and-shipper/")
public class StaffAndShipperController {
    @Autowired
    private UserRepository user;
    @Autowired
    private StaffAndShipperRepository staffAndShipperRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderOfShipperRepository orderOfShipperRepository;

    @Autowired
    private InvoiceRepository invoice;

    @Autowired
    private MailService mailService;

    @PostMapping("register-shipper")
    public Object registerShipper(@RequestBody Map<String, String> req) {
        String fullName = req.get("username");
        String email = req.get("email");
        String password = req.get("password");
        StaffAndShipper existUser = staffAndShipperRepository.getUserByEmail(email);
        if (existUser != null) {
            return Response.createErrorResponseModel("Nhân viên đã tồn tại", false);
        }

        try {
            Role r = roleRepository.getRoleById(6L);
            StaffAndShipper user = new StaffAndShipper(
                    fullName,
                    email,
                    password,
                    r,
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

    @PostMapping("login")
    public Object handleLogin(@RequestBody Map<String, String> req) {
        String userName = req.get("username");
        String password = req.get("password");

        //check user exist
        StaffAndShipperDTO existUser = staffAndShipperRepository.getUserByName(userName);
        if (existUser == null) {
            return Response.createErrorResponseModel("Nhân viên chưa đăng ký , vui lòng liên hệ với admin", false);
        }
        if (!existUser.getIsActivated() || existUser.getIsDeleted()) {
            return Response.createErrorResponseModel("Tài khoản chưa kích hoạt hoặc đã bị khoá , vui lòng liên hệ với admin", false);
        }

        //check first login
        if (existUser.getFirstLogin()) {
            if (password.equals(existUser.getPassword())) {
                return Response.createResponseModel(304,
                        "Lần đầu tiên đăng nhập, vui lòng đổi mật khẩu để tiếp tục truy cập hệ thống",
                        Map.of(
                                "urlRedirect", "/dashboard/auth/change_password/" + existUser.getId(),
                                "token", existUser.getActivationToken()
                        )
                );
            } else {
                return Response.createErrorResponseModel("Mật khẩu sai, vui lòng thử lại", false);
            }
        }

        // so sánh mật khẩu
        boolean match = Utils.verifyPassword(password, existUser.getPassword());
        if (match) {
            if (existUser.getIsDeleted()) {
                return Response.createErrorResponseModel("Tài khoản đã bị khoá, vui lòng liên hệ admin.", false);
            }
            Utils.userNameLogin = existUser.getUserName();
            Utils.idUserLogin = existUser.getId();
            Utils.staffAndShipperLogin =  existUser;
            Utils.isLogin = true;
            return Response.createSuccessResponseModel(0, Map.of(
                    "urlRedirect", "/dashboard/index"
            ));
        }
        return Response.createErrorResponseModel("Mật khẩu sai, vui lòng thử lại", false);
    }

    @PostMapping("change_password")
    public Object handleChangePassword(@RequestBody Map<String, String> req) {
        Long id = Long.parseLong(req.get("id"));
        String password = req.get("password");

        //find user
        StaffAndShipper u = staffAndShipperRepository.getUserById(id);
        if (u == null) {
            return Response.createErrorResponseModel("Không tìm thấy dữ liệu", false);
        }

        String hashPassword = Utils.hashPassword(password);
        u.setFirstLogin(false);
        u.setPassword(hashPassword);
        staffAndShipperRepository.save(u);
        return Response.createSuccessResponseModel(0, true);
    }

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

    @GetMapping("get-list-shipper")
    public Object getListShipper() {
        try {
            return Response.createSuccessResponseModel(0, staffAndShipperRepository.getAllShipperActive());
        } catch (Exception ex) {
            return Response.createErrorResponseModel("Vui lòng đợi, hệ thống đang gặp vấn đề", ex.getMessage());
        }
    }

    @PostMapping("send-order-to-shipper")
    public Object sendOrderToShipper(@RequestBody OrderOfShipperRequest req) {

        try {
            Order order = orderRepository.getDetailById(req.orderId);
            StaffAndShipper existUser = staffAndShipperRepository.getUserById(req.shipperId);
            if (order == null || existUser == null) {
                return Response.createErrorResponseModel("Không tìm thấy dữ liệu!", false);
            }
            order.setStatus(2);
            orderRepository.save(order);
            int collectionMoney = 0;
            if (order.getPaymentMethod().equals("C")) {
                collectionMoney = order.getTotalPrice();
            }

            OrderOfShipper orderOfShipper = new OrderOfShipper(
                    req.shipperId,
                    order.getPhoneNumber(),
                    order.getName(),
                    order.getAddress(),
                    order.getPaymentMethod(),
                    order.getTotalPrice(),
                    collectionMoney,
                    0,
                    new Date()
            );

            orderOfShipperRepository.save(orderOfShipper);

            return Response.createSuccessResponseModel(0, true);
        } catch (Exception ex) {
            return Response.createErrorResponseModel("Vui lòng đợi, hệ thống đang gặp vấn đề", ex.getMessage());
        }
    }

    @GetMapping("get-all-shippers")
    public Object getAllShippers() {
        try {
            List<StaffAndShipper> data = staffAndShipperRepository.getAllShipper();
            return Response.createSuccessResponseModel(data.size(), data);
        } catch (Exception ex) {
            return Response.createErrorResponseModel("Vui lòng đợi, hệ thống đang gặp vấn đề", ex.getMessage());
        }
    }

    @GetMapping("get-order-by-shipper-id")
    public Object getOrderByShipperId() {
        try {
            List<OrderOfShipper> list = orderOfShipperRepository.getOrderByShipperId(Utils.idUserLogin);
            return Response.createSuccessResponseModel(list.size(), list);
        } catch (Exception ex) {
            return Response.createErrorResponseModel("Vui lòng đợi, hệ thống đang gặp vấn đề", ex.getMessage());
        }
    }

    @GetMapping("get-order-by-shipper-id/{id}")
    public Object getOrderByShipperId(@PathVariable Long id) {
        try {
            List<OrderOfShipper> list = orderOfShipperRepository.getOrderByShipperId(id);
            return Response.createSuccessResponseModel(list.size(), list);
        } catch (Exception ex) {
            return Response.createErrorResponseModel("Vui lòng đợi, hệ thống đang gặp vấn đề", ex.getMessage());
        }
    }

    @PutMapping("update-status-order")
    public Object updateStatusOrder(@RequestBody OrderOfShipperRequest req) {
        try {
            int result = orderOfShipperRepository.updateStatusOrder(req.orderId, req.status);
            if (result > 0) {
                return Response.createSuccessResponseModel(0, true);
            }
            return Response.createErrorResponseModel("Cập nhật trạng thái thất bại!", false);
        } catch (Exception ex) {
            return Response.createErrorResponseModel("Vui lòng đợi, hệ thống đang gặp vấn đề", ex.getMessage());
        }
    }

    @GetMapping("statistic")
    public Object statistic() {
        try {
            List<StaffAndShipper> listStaff = staffAndShipperRepository.getAllUser();
            List<StaffAndShipper> listShipper = staffAndShipperRepository.getAllShipper();
            List<User> listUser = user.getAllUser();
            List<Invoice> data = invoice.findAll();

            int totalMoney = 0;
            for (Invoice i: data) {
                totalMoney += i.getTotalMoney();
            }
            return Response.createSuccessResponseModel(0, Map.of(
                    "totalStaff", listStaff.size(),
                    "totalShipper", listShipper.size(),
                    "totalUser", listUser.size(),
                    "totalMoney", totalMoney,
                    "totalOrder", data.size()
            ));
        } catch (Exception ex) {
            return Response.createErrorResponseModel("Vui lòng đợi, hệ thống đang gặp vấn đề", ex.getMessage());
        }
    }

}
