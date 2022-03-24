package com.example.thepiratescodingtest.exception;

import com.example.thepiratescodingtest.controller.MainController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(assignableTypes = MainController.class) // 지정하지 않으면 글로벌
public class ControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResult illegalExResolver(IllegalArgumentException e) {
        log.info("illegalExResolver Start!");
        return new ErrorResult(HttpStatus.BAD_REQUEST,
                e.getMessage());
    }
}