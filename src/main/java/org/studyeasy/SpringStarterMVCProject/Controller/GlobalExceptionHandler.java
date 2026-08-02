package org.studyeasy.SpringStarterMVCProject.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public String handleGeneralException(Exception ex, Model model) {
        logger.error("Unhandled Exception caught by GlobalExceptionHandler: {}", ex.getMessage(), ex);
        model.addAttribute("message", "An unexpected error occurred. Please try again later.");
        model.addAttribute("status", 500);
        return "error";
    }
}
