package com.parking.nl.exception;

import com.parking.nl.domain.response.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

import static com.parking.nl.common.Constants.NOT_NULL_VALIDATION_MSG;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleException(BusinessException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(),Status.FAILURE.getStatus(),LocalDateTime.now());
        log.error("Business Exception : {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ErrorResponse> handleException(ServiceException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), Status.FAILURE.getStatus(),LocalDateTime.now());
        log.error("Service exception: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<ErrorResponse> handleException(InvalidInputException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(),Status.FAILURE.getStatus(),LocalDateTime.now());
        log.error("Invalid Input exception: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(),Status.FAILURE.getStatus(),LocalDateTime.now());
        log.error("Generic exception: {}", ex.getMessage());
        if(ex.getMessage()!= null && ex.getMessage().contains(NOT_NULL_VALIDATION_MSG)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
