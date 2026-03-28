package com.enterprise.inventory.service;

import com.enterprise.inventory.entity.Product;
import com.enterprise.inventory.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StockAlertService {

    private final ProductRepository productRepository;
    private final EmailService emailService;

    private LocalDateTime lastEmailSent = null;

    private final String ALERT_EMAIL = "your_email@gmail.com";

    @Scheduled(fixedRate = 60000)
    public void checkLowStock() {

        List<Product> lowStockProducts =
                productRepository.findLowStockProducts();

        if (!lowStockProducts.isEmpty()) {

            if (lastEmailSent != null &&
                    lastEmailSent.plusHours(6).isAfter(LocalDateTime.now())) {
                return; // Skip sending email
            }

            StringBuilder body = new StringBuilder();
            body.append("LOW STOCK ALERT\n\n");

            for (Product p : lowStockProducts) {

                int recommended =
                        p.getReorderLevel() * 2 - p.getQuantity();

                body.append("Product: ")
                        .append(p.getName())
                        .append("\nCurrent: ")
                        .append(p.getQuantity())
                        .append("\nRecommended Order: ")
                        .append(recommended)
                        .append("\n-----------------\n");
            }

            emailService.sendLowStockAlert(
                    ALERT_EMAIL,
                    "Inventory Low Stock Alert",
                    body.toString()
            );

            lastEmailSent = LocalDateTime.now();
        }
    }
}