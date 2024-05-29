package org.example.first.groundingappapis.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TradingException extends RuntimeException {
    private final TradingErrorResult tradingErrorResult;
    public TradingException(TradingErrorResult tradingErrorResult, String message) {
        super(message);
        this.tradingErrorResult = tradingErrorResult;
    }
}