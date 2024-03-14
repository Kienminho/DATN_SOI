package com.mid_term.springecommerce.Controllers.ModuleShipperAndStaff;

import com.mid_term.springecommerce.Utils.Utils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dashboard")
public class M2_HomeController {
    @GetMapping({"", "/"})
    public String checkLogin() {
        if (!Utils.isLogin)
            return "redirect:/dashboard/auth/login";
        return "Module2/index";
    }

    @GetMapping("/index")
    public String renderIndex()
    {
        if (!Utils.isLogin)
            return "redirect:/dashboard/auth/login";
        return "Module2/index";
    }

    @GetMapping("/employee-manager")
    public String renderManageEmployee() {
        if (!Utils.isLogin)
            return "redirect:/dashboard/auth/login";
        return "Module2/employee-manager";
    }

    @GetMapping("/warranty-manager")
    public String renderManageWarranty() {
        if (!Utils.isLogin)
            return "redirect:/dashboard/auth/login";
        return "Module2/warranty-manager";
    }

    @GetMapping("/shipper-manager")
    public String renderManageShipper() {
        if (!Utils.isLogin)
            return "redirect:/dashboard/auth/login";
        return "Module2/shipper-manager";
    }

    @GetMapping("/product-manager")
    public String RenderProductManager() {
        if (!Utils.isLogin)
            return "redirect:/dashboard/auth/login";
        return "Module2/product-manager";
    }

    @GetMapping("/category-manager")
    public String RenderCategoryManager() {
        if (!Utils.isLogin)
            return "redirect:/dashboard/auth/login";
        return "Module2/category-manager";
    }

    @GetMapping("/order-manager")
    public String RenderOrderManager() {
        if (!Utils.isLogin)
            return "redirect:/dashboard/auth/login";
        return "Module2/order-manager";
    }

    @GetMapping("/payment")
    public String RenderPayment() {
        if (!Utils.isLogin)
            return "redirect:/dashboard/auth/login";
        return "Module2/payment";
    }

    @GetMapping("/pay-success/{id}")
    public String RenderPaymentSuccess(@PathVariable String id, Model model) {
        String downloadLink = "http://localhost:8080/"+ id+".pdf";
        model.addAttribute("downloadLink", downloadLink);
        model.addAttribute("id", id);
        return "Module2/success-pay";
    }

    @GetMapping("/shipper")
    public String RenderShipper() {
        if (!Utils.isLogin)
            return "redirect:/dashboard/auth/login";
        return "Module2/shipper";
    }

    @GetMapping("/receive-warranty")
    public String RenderReceiveWarranty() {
        if (!Utils.isLogin)
            return "redirect:/dashboard/auth/login";
        return "Module2/receive-warranty";
    }

    @GetMapping("/my-profile")
    public String RenderInfo() {
        if (!Utils.isLogin)
            return "redirect:/dashboard/auth/login";
        return "Module2/account-setting";
    }
}
