package com.example.javaee.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {
    @GetMapping("error.htm")
    public String errorHandler() {
        return "error";
    }
}
