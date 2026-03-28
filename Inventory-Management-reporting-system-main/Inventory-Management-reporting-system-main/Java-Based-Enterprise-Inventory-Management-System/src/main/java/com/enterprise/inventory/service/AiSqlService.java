package com.enterprise.inventory.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AiSqlService {

    private final JdbcTemplate jdbcTemplate;

    public AiSqlService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public String processQuestion(String question){

        question = question.toLowerCase();

        String sql = null;

        if(question.contains("highest quantity")){
            sql = "SELECT name, quantity FROM product ORDER BY quantity DESC LIMIT 1";
        }

        else if(question.contains("low stock")){
            sql = "SELECT name, quantity FROM product WHERE quantity < 10";
        }

        else if(question.contains("total products")){
            sql = "SELECT COUNT(*) as total FROM product";
        }

        else if(question.contains("inventory value")){
            sql = "SELECT SUM(price * quantity) as total_value FROM product";
        }

        if(sql == null){
            return "Sorry, I couldn't understand the question.";
        }

        List<Map<String,Object>> result = jdbcTemplate.queryForList(sql);

        return formatResult(result);
    }

    private String formatResult(List<Map<String,Object>> result){

        if(result.isEmpty()){
            return "No results found.";
        }

        StringBuilder sb = new StringBuilder();

        for(Map<String,Object> row : result){

            for(String key : row.keySet()){
                sb.append(key)
                        .append(": ")
                        .append(row.get(key))
                        .append(" ");
            }

            sb.append("\n");
        }

        return sb.toString();
    }
}

