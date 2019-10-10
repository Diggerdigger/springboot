package com.august.thirteen.handler;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ControllerAdvice /* @RestControllerAdvice返回model，处理ajax请求错误*/
public class MyExceptionHandler {

    public static final String ERROR_VIEW="errorPage";

    @ExceptionHandler(value = Exception.class)
    public Object errorHandler(HttpServletResponse response, HttpServletRequest request,Exception e) throws  Exception{
        e.printStackTrace();;
        ModelAndView mav = new ModelAndView();
        mav.addObject("exception",e);
        mav.addObject("url",request.getRequestURL());
        mav.setViewName(ERROR_VIEW);
        return mav;
    }
}
