package com.example.javaee.controller;

import com.example.javaee.model.Blog;
import com.example.javaee.model.Category;
import com.example.javaee.service.BlogService;
import com.example.javaee.service.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/")
public class HomePageController {
    private final CategoryService categoryService;

    private final BlogService blogService;

    public HomePageController(
            CategoryService categoryService,
            BlogService blogService) {
        this.categoryService = categoryService;
        this.blogService = blogService;
    }

    @GetMapping("/index.htm")
    public String index(ModelMap modelMap) {
        return "index";
    }

    @ModelAttribute("featuredBlogs")
    public List<Blog> getFirst5Blogs() {
        return this.blogService.findFirst(5);
    }

    @ModelAttribute("categories")
    public List<Category> getAllCategories() {
        return this.categoryService.findAll();
    }

    @ModelAttribute("popularBlogs")
    public List<Blog> getFirst5PopularBlogs() {
        return this.blogService.findPopular(5);
    }
}
