package com.example.thepiratescodingtest.exception;

import com.example.thepiratescodingtest.controller.MainController;
import lombok.extern.slf4j.Slf4j;
import org.omg.CORBA.UserException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestControllerAdvice(assignableTypes = MainController.class) // 지정하지 않으면 글로벌
public class ControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResult illegalExResolver(HttpServletRequest request,
                                         HttpServletResponse response,
                                         IllegalArgumentException e) {
        log.info("illegalExResolver Start!");
        return new ErrorResult(HttpStatus.BAD_REQUEST,
                e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResult> userExResolver(HttpServletRequest request,
                                                      HttpServletResponse response,
                                                      UserException exception) {
        ErrorResult errorResult = new ErrorResult(
                HttpStatus.BAD_REQUEST,
                exception.getMessage());
        return new ResponseEntity<>(errorResult, HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "error.bad")
    public class BadRequestException extends RuntimeException{
    }
}