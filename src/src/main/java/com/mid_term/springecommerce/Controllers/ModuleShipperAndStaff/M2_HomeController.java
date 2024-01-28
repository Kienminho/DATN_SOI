package com.mid_term.springecommerce.Controllers.ModuleShipperAndStaff;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/system")
public class M2_HomeController {

    @GetMapping({"/", "/index"})
    public String renderIndex()
    {
        //return "Module2/index";
        return "Module2/employeeManager";
    }

    @GetMapping("/employee-manager")
    public String renderManageEmployee() {
        return "Module2/employeeManager";
    }

    @GetMapping("/product-manager")
    public String RenderProductManager() {
        /*if (!Utils.isLogin)
            return "redirect:/auth/login";*/
        return "Module2/productManager";
    }
}
