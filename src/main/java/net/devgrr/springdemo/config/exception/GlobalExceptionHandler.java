package net.devgrr.springdemo.config.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  protected ResponseEntity<ErrorResponse> handle(HttpRequestMethodNotSupportedException e) {
    //        System.out.println("error 1 : " + e.getMessage());
    return ResponseEntity.badRequest().body(new ErrorResponse(ErrorCode.METHOD_NOT_ALLOWED));
  }

  @ExceptionHandler(
      MethodArgumentNotValidException
          .class) // ConstraintViolationException // InvocationTargetException
  protected ResponseEntity<ErrorResponse> handle(MethodArgumentNotValidException e) {
    //        System.out.println("error 2 : " + e.getMessage());
    return ResponseEntity.badRequest()
        .body(new ErrorResponse(ErrorCode.INVALID_INPUT_VALUE, e.getMessage()));
  }

  @ExceptionHandler(Exception.class)
  protected ResponseEntity<ErrorResponse> handle(Exception e) {
    //        System.out.println("error 3 : " + e.getMessage());
    return ResponseEntity.internalServerError()
        .body(new ErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR));
  }

  @ExceptionHandler(BaseException.class)
  protected ResponseEntity<ErrorResponse> handle(BaseException e) {
    //        System.out.println("error 4 : " + e.getMessage());
    final ErrorCode errorCode = e.getErrorCode();
    final String errorMessage = e.getMessage();
    return ResponseEntity.status(errorCode.getStatus())
        .body(new ErrorResponse(errorCode, errorMessage));
  }
}
