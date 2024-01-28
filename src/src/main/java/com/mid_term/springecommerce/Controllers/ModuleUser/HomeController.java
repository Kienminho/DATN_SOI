package com.mid_term.springecommerce.Controllers.ModuleUser;

import com.mid_term.springecommerce.Utils.Utils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping({"/", "/home"})
    public String renderIndex(Model model)
    {
        model.addAttribute("username", Utils.userLogin);

        return "index";
    }

    @GetMapping("/contact")
    public String renderContact() {
        return "contact";
    }

    @GetMapping("/shop")
    public String renderShop() {
        return "shop";
    }

    @GetMapping("/searchs")

    public String renderSearch() {
        return "search";
    }

    @GetMapping("/shopping-cart")
    public String renderShopCart() {
        if(Utils.userLogin == null) return "redirect:/home";
        return "cart";
    }

    @GetMapping("checkout")
    public String renderCheckout() {
        if(Utils.userLogin == null) return "redirect:/home";
        return "checkout";
    }

    @GetMapping("/my-info")
    public String renderGuarantee() {
        if(Utils.userLogin == null) return "redirect:/home";
        return "guarantee";
    }
}
