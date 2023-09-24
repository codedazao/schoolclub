package com.dazao.schoolclubbackend.controller.exception;

import com.dazao.schoolclubbackend.entity.RestBean;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@ControllerAdvice
@RestController
public class ValidationController {
    @ExceptionHandler(ValidationException.class)
    public RestBean<Void> exceptionHandler(ValidationException exception){
        log.warn("Resolve [{}: {}]",exception.getClass().getName(),exception.getMessage());
        return RestBean.failure(400,"请求参数错误");
    }

}
