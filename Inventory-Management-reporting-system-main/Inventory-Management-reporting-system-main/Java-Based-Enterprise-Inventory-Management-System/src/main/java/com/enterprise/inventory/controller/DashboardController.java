package com.enterprise.inventory.controller;

import com.enterprise.inventory.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final ProductService productService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {

        model.addAttribute("totalProducts",
                productService.getTotalProductCount());

        model.addAttribute("totalInventoryValue",
                productService.getTotalInventoryValue());

        model.addAttribute("lowStockCount",
                productService.getLowStockCount());

        return "dashboard";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}