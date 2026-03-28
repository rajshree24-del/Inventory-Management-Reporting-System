package com.enterprise.inventory.controller;

import com.enterprise.inventory.entity.Product;
import com.enterprise.inventory.repository.ProductRepository;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/ai")
public class AiChatController {

    private final ProductRepository productRepository;

    public AiChatController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @PostMapping("/chat")
    public Map<String,String> chat(@RequestBody Map<String,String> req){

        String message = req.get("message").toLowerCase().trim();
        String reply;

        /* LOW STOCK PRODUCTS */

        if(message.contains("low stock")){

            List<Product> lowStock = productRepository.findByQuantityLessThan(10);

            if(lowStock.isEmpty()){
                reply = "✅ There are no low stock products.";
            }
            else{

                StringBuilder sb = new StringBuilder();

                sb.append("⚠ Low stock products (")
                        .append(lowStock.size())
                        .append(" items):\n");

                for(Product p : lowStock){
                    sb.append("• ")
                            .append(p.getName())
                            .append(" (Qty: ")
                            .append(p.getQuantity())
                            .append(")\n");
                }

                reply = sb.toString();
            }
        }

        /* TOTAL PRODUCTS */

        else if(message.contains("total product")){

            long count = productRepository.count();

            reply = "📦 Total products in inventory: " + count;
        }

        /* HIGHEST QUANTITY */

        else if(message.contains("highest quantity")
                || message.contains("most stock")
                || message.contains("largest stock")){

            List<Product> products = productRepository.findAll();

            Product maxProduct = null;

            for(Product p : products){

                if(maxProduct == null || p.getQuantity() > maxProduct.getQuantity()){
                    maxProduct = p;
                }
            }

            if(maxProduct != null){
                reply = "🏆 Product with highest quantity:\n"
                        + maxProduct.getName()
                        + " (Qty: "
                        + maxProduct.getQuantity() + ")";
            }else{
                reply = "No products found.";
            }
        }

        /* INVENTORY VALUE */

        else if(message.contains("inventory value")
                || message.contains("total value")
                || message.contains("stock value")){

            List<Product> products = productRepository.findAll();

            double total = 0;

            for(Product p : products){
                total += p.getPrice() * p.getQuantity();
            }

            reply = "💰 Total inventory value: ₹ " + String.format("%.2f", total);
        }

        /* LIST ALL PRODUCTS */

        else if(message.contains("list products")
                || message.contains("show products")){

            List<Product> products = productRepository.findAll();

            if(products.isEmpty()){
                reply = "No products available.";
            }
            else{

                StringBuilder sb = new StringBuilder("📋 Product List:\n");

                for(Product p : products){
                    sb.append("• ")
                            .append(p.getName())
                            .append(" | Qty: ")
                            .append(p.getQuantity())
                            .append(" | Price: ₹")
                            .append(p.getPrice())
                            .append("\n");
                }

                reply = sb.toString();
            }
        }

        /* UNKNOWN QUESTION */

        else{
            reply = """
                    🤖 I can help with:
                    
                    • Show low stock products
                    • Total products
                    • Highest quantity product
                    • Inventory value
                    • List products
                    """;
        }

        Map<String,String> response = new HashMap<>();
        response.put("reply", reply);

        return response;
    }
}

