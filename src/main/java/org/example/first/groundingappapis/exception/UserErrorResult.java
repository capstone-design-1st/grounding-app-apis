package org.example.first.groundingappapis.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserErrorResult {
    DUPLICATED_EMAIL(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다."), //409
    REQUIRED_VALUE(HttpStatus.BAD_REQUEST, "필수 값이 누락되었습니다."), //400
    FAILED_VALIDATING_CODE(HttpStatus.BAD_REQUEST, "인증코드가 일치하지 않습니다. 인증 코드를 재요청해 주세요."), //400
    UNVERIFIED_PHONE_NUMBER(HttpStatus.FORBIDDEN, "인증되지 않은 전화번호입니다. 재시도해주세요."), //403
    UNVERIFIED_EMAIL(HttpStatus.FORBIDDEN, "인증되지 않은 이메일입니다. 재시도해주세요."), //403
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다."), //404
    INVALID_JWT(HttpStatus.UNAUTHORIZED, "유효하지 않은 JWT입니다."), //401
    EXPIRED_JWT(HttpStatus.UNAUTHORIZED, "만료된 JWT입니다."), //401
    NOT_FOUND_JWT(HttpStatus.UNAUTHORIZED, "존재하지 않는 JWT입니다."), //401
    USER_PARTIALLY_CREATED(HttpStatus.ACCEPTED, "사용자 정보가 부분적으로 생성되었습니다. 사용자 정보를 다시 생성해주세요."), //202
    USER_HAVE_TO_SIGN_IN(HttpStatus.UNAUTHORIZED, "사용자 정보가 부족합니다. 사용자 정보를 다시 생성해주세요."), //401
    DUPLICATED_NICKNAME(HttpStatus.CONFLICT, "이미 존재하는 닉네임입니다."), //409
    EMPTY_PROFILEIMG(HttpStatus.BAD_REQUEST, "프로필 이미지가 없습니다."), //400
    DUPLICATED_PHONE_NUMBER(HttpStatus.CONFLICT, "이미 존재하는 전화번호입니다."), //409
    ;
    private final HttpStatus httpStatus;
    private final String message;
}
