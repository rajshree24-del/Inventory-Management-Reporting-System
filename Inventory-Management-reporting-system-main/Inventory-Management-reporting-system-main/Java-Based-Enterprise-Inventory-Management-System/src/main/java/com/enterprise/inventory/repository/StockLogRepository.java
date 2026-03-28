package com.enterprise.inventory.repository;

import com.enterprise.inventory.entity.StockLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface StockLogRepository extends JpaRepository<StockLog, Long> {
    List<StockLog> findByChangeDateBetween(LocalDateTime start, LocalDateTime end);
}