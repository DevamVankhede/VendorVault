package com.orion.vendorvault.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ModelAndView handleIllegalArgumentException(HttpServletRequest request, Exception ex, Model model) {
        log.error("IllegalArgumentException Occurred. URL: {}, Error: {}", request.getRequestURL(), ex.getMessage());
        
        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", ex);
        mav.addObject("url", request.getRequestURL());
        mav.addObject("errorMessage", ex.getMessage());
        mav.setViewName("error/400"); // Maps to src/main/resources/templates/error/400.html
        return mav;
    }
    
    @ExceptionHandler(Exception.class)
    public ModelAndView handleAllOtherExceptions(HttpServletRequest request, Exception ex) {
        log.error("Unhandled Exception Occurred. URL: {}, Error: {}", request.getRequestURL(), ex.getMessage(), ex);
        
        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", ex);
        mav.addObject("url", request.getRequestURL());
        mav.setViewName("error/500");
        return mav;
    }
}
