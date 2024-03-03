package com.mid_term.springecommerce.Controllers.ModuleShipperAndStaff;

import com.mid_term.springecommerce.Models.Entity.StaffAndShipper;
import com.mid_term.springecommerce.Repositorys.StaffAndShipperRepository;
import com.mid_term.springecommerce.Utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dashboard/auth")
public class M2_AuthController {
    @Autowired
    private StaffAndShipperRepository s;
    @GetMapping("/login")
    public String renderLogin() {
        return "Module2/login";
    }

    @GetMapping("active/{token}")
    public String handleActive(@PathVariable String token, Model model) {

        StaffAndShipper user = s.getUserByToken(token);
        if(user == null) {
            model.addAttribute("message", "Invalid activation token!");
            model.addAttribute("isSuccess", false);
        }
        long currentTime = System.currentTimeMillis();
        long activationExpiresTime = user.getActivationExpires().getTime();
        long differenceInMinutes = (currentTime - activationExpiresTime) / (1000 * 60);
        if(differenceInMinutes <=1) {
            user.setIsActivated(true);
            s.save(user);
            model.addAttribute("message", "Kích hoạt thành công, bạn sẽ được chuyển hướng sau vài giây.");
            model.addAttribute("isSuccess", true);
        }
        else {
            model.addAttribute("message", "Hết thời gian kích hoạt, vui lòng liên hệ với admin để cấp lại.");
            model.addAttribute("isSuccess", false);
        }
        return "Module2/active";
    }

    @GetMapping("logout")
    public String handelLogout() {
        Utils.isLogin = false;
        Utils.idUserLogin = 0L;
        Utils.userNameLogin = "";
        Utils.staffAndShipperLogin = null;
        return "redirect:/dashboard/auth/login";
    }

    @GetMapping("register")
    public String renderRegister() {
        return "Module2/register";
    }

    @GetMapping("change_password/{id}")
    public String renderChangePassword(@PathVariable String id) {
        return "Module2/change-password";
    }
}
