package com.anshu.imageservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.anshu.imageservice.exception.ErrorResponse;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DeviceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleDevice(DeviceNotFoundException ex){
        return build(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(InvalidDeviceSecretException.class)
    public ResponseEntity<ErrorResponse> handleSecret(InvalidDeviceSecretException ex){
        return build(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(DeviceInactiveException.class)
    public ResponseEntity<ErrorResponse> handleInActive(DeviceInactiveException ex){
        return build(ex.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex){
        return build("Internal Server Error" , HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ErrorResponse> build(String msg, HttpStatus status) {
        return ResponseEntity.status(status).body(
                ErrorResponse.builder()
                        .message(msg)
                        .status(status.value())
                        .timestamp(LocalDateTime.now(ZoneOffset.UTC))
                        .build()
        );
    }
}
