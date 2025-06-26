package com.boutique.uniformes.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {
    
    @ModelAttribute
    public void addRequestToModel(Model model, HttpServletRequest request) {
        model.addAttribute("request", request);
    }
    

    @ModelAttribute("currentUrl")
    public String getCurrentUrl(HttpServletRequest request) {
        return request.getRequestURI();
    }
    
    @ModelAttribute("contextPath")
    public String getContextPath(HttpServletRequest request) {
        return request.getContextPath();
    }
}
    

