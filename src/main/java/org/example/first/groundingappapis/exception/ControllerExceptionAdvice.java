package org.example.first.groundingappapis.exception;

import org.example.first.groundingappapis.dto.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class ControllerExceptionAdvice {

    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<ResponseDto> handleMissingServletRequestPartException(MissingServletRequestPartException e) {

        log.error("MissingServletRequestPartException", e);

        final ResponseDto responseDto = ResponseDto.builder()
                .error(e.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ResponseDto> handleBindException(BindException e) {

        final String errors = e.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));

        log.error("BindException", errors);

        final ResponseDto responseDto = ResponseDto.builder()
                .error(errors)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ResponseDto> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {

        log.error("MethodArgumentTypeMismatchException", e);

        final ResponseDto responseDto = ResponseDto.builder()
                .error(e.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
    }

    @ExceptionHandler(UserException.class)
    public ResponseEntity<ResponseDto> handleUserException(UserException e) {

        log.error("UserException", e);

        final UserErrorResult errorResult = e.getUserErrorResult();

        final ResponseDto responseDto = ResponseDto.builder()
                .error(errorResult.getMessage())
                .build();

        return ResponseEntity.status(errorResult.getHttpStatus()).body(responseDto);
    }
    @ExceptionHandler(PropertyException.class)
    public ResponseEntity<ResponseDto> handlePropertyException(PropertyException e) {

        log.error("PropertyException", e);

        final PropertyErrorResult errorResult = e.getPropertyErrorResult();

        final ResponseDto responseDto = ResponseDto.error()
                .error(errorResult.getMessage())
                .build();

        //return body, nothing. refator this
        return ResponseEntity.status(errorResult.getHttpStatus()).body(responseDto);
    }
    @ExceptionHandler(TradingException.class)
    public ResponseEntity<ResponseDto> handleTradingException(TradingException e) {

        log.error("TradingException", e);

        final TradingErrorResult errorResult = e.getTradingErrorResult();

        final ResponseDto responseDto = ResponseDto.error()
                .error(errorResult.getMessage())
                .build();

        //return body, nothing. refator this
        return ResponseEntity.status(errorResult.getHttpStatus()).body(responseDto);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDto> handleException(Exception e) {

        log.error("Exception" + String.valueOf(e));

        if(e.getClass().getName().equals("org.springframework.security.access.AccessDeniedException")){
            final ResponseDto responseDto = ResponseDto.builder()
                    .error(e.getMessage())
                    .build();

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseDto);
        }

        final ResponseDto responseDto = ResponseDto.builder()
                .error(e.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
    }


}
