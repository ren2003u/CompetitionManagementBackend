package com.example.demo.commom;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@ResponseBody
public class ExceptionAdvice {
    @ExceptionHandler(Exception.class)
    public Object exceptionAdvice(Exception e){
        return AjaxResult.fail(-1,e.getMessage());
    }
}
