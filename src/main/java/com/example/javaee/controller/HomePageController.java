package com.example.javaee.controller;

import com.example.javaee.model.Category;
import com.example.javaee.service.CategoryDetailService;
import com.example.javaee.service.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/")
public class HomePageController {

    private CategoryDetailService categoryDetailService;

    private CategoryService categoryService;

    public HomePageController(
            CategoryService categoryService,
            CategoryDetailService categoryDetailService) {
        this.categoryDetailService = categoryDetailService;
        this.categoryService = categoryService;
    }

    @ModelAttribute("categories")
    public List<Category> getAllCategories() {
        return null;
    }
}
