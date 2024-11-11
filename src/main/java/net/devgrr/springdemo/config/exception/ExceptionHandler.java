package net.devgrr.springdemo.config.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionHandler {
    @org.springframework.web.bind.annotation.ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ErrorResponse> handle(HttpRequestMethodNotSupportedException e) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(ErrorCode.METHOD_NOT_ALLOWED));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handle(Exception e) {
        return ResponseEntity.internalServerError()
                .body(new ErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(BaseException.class)
    protected ResponseEntity<ErrorResponse> handle(BaseException e) {
        final ErrorCode errorCode = e.getErrorCode();
        final String errorMessage = e.getMessage();
        return ResponseEntity.status(errorCode.getStatus())
                .body(new ErrorResponse(errorCode, errorMessage));
    }
}
