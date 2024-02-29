package com.mid_term.springecommerce.Controllers.ModuleShipperAndStaff;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dashboard")
public class M2_HomeController {

    @GetMapping({"/", "/home"})
    public String renderIndex()
    {
        //return "Module2/index";
        return "Module2/index";
    }

    @GetMapping("/employee-manager")
    public String renderManageEmployee() {
        return "Module2/employeeManager";
    }

    @GetMapping("/warranty-manager")
    public String renderManageWarranty() {
        return "Module2/warrantyManager";
    }

    @GetMapping("/shipper-manager")
    public String renderManageShipper() {
        return "Module2/shipperManager";
    }

    @GetMapping("/product-manager")
    public String RenderProductManager() {
        /*if (!Utils.isLogin)
            return "redirect:/auth/login";*/
        return "Module2/productManager";
    }

    @GetMapping("/category-manager")
    public String RenderCategoryManager() {
        /*if (!Utils.isLogin)
            return "redirect:/auth/login";*/
        return "Module2/categoryManager";
    }

    @GetMapping("/order-manager")
    public String RenderOrderManager() {
        /*if (!Utils.isLogin)
            return "redirect:/auth/login";*/
        return "Module2/orderManager";
    }

    @GetMapping("/payment")
    public String RenderPayment() {
        /*if (!Utils.isLogin)
            return "redirect:/auth/login";*/
        return "Module2/payment";
    }

    @GetMapping("/pay-success/{id}")
    public String RenderPaymentSuccess(@PathVariable String id, Model model) {
        String downloadLink = "http://localhost:8080/"+ id+".pdf";
        model.addAttribute("downloadLink", downloadLink);
        model.addAttribute("id", id);
        return "Module2/successPay";
    }

    @GetMapping("/shipper")
    public String RenderShipper() {
        /*if (!Utils.isLogin)
            return "redirect:/auth/login";*/
        return "Module2/shipper";
    }
}
