package com.mid_term.springecommerce.Controllers.ModuleUser;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    @GetMapping("/admin")
    public String renderAdmin() {

        return "admin";
    }
}
