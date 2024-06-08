package com.example.javaee.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("admin/categories")
public class CategoryController {
    public CategoryController() {

    }

    @GetMapping("index.htm")
    public String categoryIndexViewRenderer(
            HttpServletRequest request,
            ModelMap modelMap) {
        return "category/index";
    }
}
