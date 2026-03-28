package com.enterprise.inventory.service;

import com.enterprise.inventory.entity.Product;
import com.enterprise.inventory.entity.StockLog;
import com.enterprise.inventory.repository.ProductRepository;
import com.enterprise.inventory.repository.StockLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final StockLogRepository stockLogRepository;

    // Add Product with validation
    public Product addProduct(Product product) {

        if (product.getName() == null || product.getName().isBlank()) {
            throw new RuntimeException("Product name cannot be empty");
        }

        if (product.getQuantity() < 0) {
            throw new RuntimeException("Quantity cannot be negative");
        }

        if (product.getPrice() < 0) {
            throw new RuntimeException("Price cannot be negative");
        }

        product.setLastUpdated(LocalDateTime.now());
        return productRepository.save(product);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // =========================
    // ✅ ADD THIS (EDIT SUPPORT)
    // =========================
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product Not Found"));
    }

    // =========================
    // ✅ ADD THIS (DELETE SUPPORT)
    // =========================
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    // Stock Update
    public Product updateStock(Long productId, int quantityChange, String changeType) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product Not Found"));

        if (quantityChange <= 0) {
            throw new RuntimeException("Quantity must be positive");
        }

        if (changeType.equalsIgnoreCase("ADD")) {
            product.setQuantity(product.getQuantity() + quantityChange);
        } else if (changeType.equalsIgnoreCase("REMOVE")) {

            if (product.getQuantity() < quantityChange) {
                throw new RuntimeException("Insufficient stock");
            }

            product.setQuantity(product.getQuantity() - quantityChange);
        }

        product.setLastUpdated(LocalDateTime.now());
        productRepository.save(product);

        // Save Log
        StockLog log = StockLog.builder()
                .productId(productId)
                .changeType(changeType.toUpperCase())
                .quantityChanged(quantityChange)
                .changeDate(LocalDateTime.now())
                .build();

        stockLogRepository.save(log);

        return product;
    }

    // Dynamic low stock
    public List<Product> getLowStockProducts() {
        return productRepository.findAll()
                .stream()
                .filter(p -> p.getQuantity() <= p.getReorderLevel())
                .toList();
    }

    public List<StockLog> getAllLogs() {
        return stockLogRepository.findAll();
    }

    public List<Product> searchByCategory(String category) {
        return productRepository.findByCategory(category);
    }

    public double getTotalInventoryValue() {
        return productRepository.findAll()
                .stream()
                .mapToDouble(p -> p.getPrice() * p.getQuantity())
                .sum();
    }

    public long getLowStockCount() {
        return productRepository.findLowStockProducts().size();
    }

    public long getTotalProductCount() {
        return productRepository.count();
    }

    public List<StockLog> getLogsBetween(LocalDateTime start, LocalDateTime end) {
        return stockLogRepository.findByChangeDateBetween(start, end);
    }
}