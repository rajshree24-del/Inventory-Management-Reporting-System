package com.enterprise.inventory.controller;

import com.enterprise.inventory.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class StockLogController {

    private final ProductService productService;

    @GetMapping("/logs")
    public String logs(Model model) {
        model.addAttribute("logs",
                productService.getAllLogs());
        return "logs";
    }

    @GetMapping("/logs/filter")
    public String filterLogs(
            @RequestParam String start,
            @RequestParam String end,
            Model model) {

        LocalDateTime startDate = LocalDateTime.parse(start);
        LocalDateTime endDate = LocalDateTime.parse(end);

        model.addAttribute("logs",
                productService.getLogsBetween(startDate, endDate));

        return "logs";
    }
}