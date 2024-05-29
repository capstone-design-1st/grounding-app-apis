package org.example.first.groundingappapis.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum TradingErrorResult {

    TRADING_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 거래입니다."), //404
    ACCOUNT_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 계정입니다."), //404
    NOT_ENOUGH_DEPOSIT(HttpStatus.FORBIDDEN, "예수금이 충분하지 않습니다.");//409;
    private final HttpStatus httpStatus;
    private final String message;
}
