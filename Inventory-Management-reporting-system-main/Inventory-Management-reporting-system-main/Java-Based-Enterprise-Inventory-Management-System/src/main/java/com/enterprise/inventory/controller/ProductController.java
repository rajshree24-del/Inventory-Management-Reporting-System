package com.enterprise.inventory.controller;

import com.enterprise.inventory.entity.Product;
import com.enterprise.inventory.service.ProductService;
import com.enterprise.inventory.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ReportService reportService;

    @GetMapping("/products")
    public String products(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "products";
    }

    @GetMapping("/products/add")
    public String addProductForm(Model model) {
        model.addAttribute("product", new Product());
        return "add-product";
    }

    @PostMapping("/products/save")
    public String saveProduct(@ModelAttribute Product product) {
        productService.addProduct(product);
        return "redirect:/products";
    }

    // =========================
    // ✅ FIX: EDIT BUTTON
    // =========================
    @GetMapping("/products/edit/{id}")
    public String editProduct(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);
        return "add-product"; // reuse same form (NO new file needed)
    }

    // =========================
    // ✅ FIX: DELETE BUTTON
    // =========================
    @GetMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "redirect:/products";
    }

    // =========================
    // EXISTING UPDATE STOCK
    // =========================
    @GetMapping("/products/update/{id}")
    public String updateStock(@PathVariable Long id,
                              @RequestParam int quantity,
                              @RequestParam String type) {

        productService.updateStock(id, quantity, type);
        return "redirect:/products";
    }

    @GetMapping("/low-stock")
    public String lowStock(Model model) {
        model.addAttribute("products", productService.getLowStockProducts());
        return "low-stock";
    }

    // CSV EXPORT ENDPOINT
    @GetMapping("/reports/export")
    public void exportCSV(HttpServletResponse response) throws Exception {
        reportService.exportProductsToCSV(response);
    }

    @GetMapping("/reports/logs/export")
    public void exportLogsCSV(HttpServletResponse response) throws Exception {
        reportService.exportStockLogsToCSV(response);
    }
}