package com.mid_term.springecommerce.Controllers.ModuleUser;

import com.mid_term.springecommerce.Models.Entity.Product;
import com.mid_term.springecommerce.Models.ResponseModel.ProductResponse;
import com.mid_term.springecommerce.Repositorys.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.text.NumberFormat;
import java.util.Locale;

@Controller
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private ProductRepository productRepository;
    @GetMapping("/{id}")
    public String detailProduct(@PathVariable Long id, Model model) {
        Product p = productRepository.getDetailProduct(id);
        String salePrice = formatPrice(p.getSalePrice());
        ProductResponse productResponse = new ProductResponse(p.getId(),p.getName(), p.getImageUrl(), salePrice);
        model.addAttribute("product", productResponse);
        return "detail";
    }

    private String formatPrice(int price) {
        Locale locale = new Locale("vi", "VN");
        NumberFormat formatter = NumberFormat.getCurrencyInstance(locale);
        return formatter.format(price);
    }
}
