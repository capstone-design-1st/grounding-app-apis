package org.example.first.groundingappapis.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.first.groundingappapis.dto.DayTransactionLogDto;
import org.example.first.groundingappapis.dto.RealTimeTransactionLogDto;
import org.example.first.groundingappapis.entity.Property;
import org.example.first.groundingappapis.repository.DayTransactionLogRepository;
import org.example.first.groundingappapis.repository.RealTimeTransactionLogRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@RequiredArgsConstructor
public class AsyncServiceImpl implements AsyncService{
    private final RealTimeTransactionLogRepository realTimeTransactionLogRepository;
    private final DayTransactionLogRepository dayTransactionLogRepository;

    @Async
    public CompletableFuture<List<RealTimeTransactionLogDto>> findRecentTransactionLogsAsync(List<Property> properties) {
        return CompletableFuture.completedFuture(realTimeTransactionLogRepository.findRecentTransactionLogsByProperties(properties));
    }

    @Async
    public CompletableFuture<List<DayTransactionLogDto>> findRecentDayTransactionLogsAsync(List<Property> properties) {
        return CompletableFuture.completedFuture(dayTransactionLogRepository.findRecentDayTransactionLogsByProperties(properties));
    }
}
