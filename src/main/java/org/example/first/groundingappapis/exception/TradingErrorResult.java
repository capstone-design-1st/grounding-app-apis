package org.example.first.groundingappapis.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum TradingErrorResult {

    TRADING_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 거래입니다."), //404
    ACCOUNT_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 계정입니다."), //404
    NOT_ENOUGH_DEPOSIT(HttpStatus.FORBIDDEN, "예수금이 충분하지 않습니다."),
    INVENTORY_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 종목을 보유하지 않았습니다." ),
    NOT_ENOUGH_INVENTORY(HttpStatus.FORBIDDEN, "보유한 수량이 부족합니다." ),
    FUNDRAISE_ENDED(HttpStatus.FORBIDDEN, "모집 마감된 매물입니다. 거래를 이용해주세요."),
    EXCEED_FUNDRAISE_TOTAL_AMOUNT(HttpStatus.FORBIDDEN, "총 모집 금액을 초과한 주문입니다."),
    ALREADY_SUBSCRIBED(HttpStatus.FORBIDDEN, "이미 모집한 매물입니다." ),
    NOT_ENOUGH_FUNDRAISE(HttpStatus.FORBIDDEN, "모집이 끝나지 않은 매물입니다."),
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 주문입니다."),
    DAY_TRANSACTION_LOG_NOT_FOUND(HttpStatus.NOT_FOUND, "일별 로그가 존재하지 않습니다."),
    INVALID_TYPE(HttpStatus.FORBIDDEN, "유효하지 않은 거래유형입니다." ),
    NOT_ENOUGH_SELLABLE_QUANTITY(HttpStatus.FORBIDDEN,  "판매 가능한 수량이 부족합니다." ),
    ;
    private final HttpStatus httpStatus;
    private final String message;
}
