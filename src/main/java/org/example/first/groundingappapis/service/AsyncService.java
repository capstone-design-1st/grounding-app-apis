package org.example.first.groundingappapis.service;

import org.example.first.groundingappapis.dto.DayTransactionLogDto;
import org.example.first.groundingappapis.dto.RealTimeTransactionLogDto;
import org.example.first.groundingappapis.entity.Property;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface AsyncService {
    CompletableFuture<List<RealTimeTransactionLogDto>> findRecentTransactionLogsAsync(List<Property> properties);

    CompletableFuture<List<DayTransactionLogDto>> findRecentDayTransactionLogsAsync(List<Property> properties);
}
