package com.example.javaee.controller;

import com.example.javaee.model.Blog;
import com.example.javaee.model.CategoryDetail;
import com.example.javaee.service.BlogService;
import com.example.javaee.service.CategoryDetailService;

import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/blogs")
@Valid
public class BlogController {

    private final BlogService blogService;

    private final CategoryDetailService categoryDetailService;

    public BlogController(BlogService blogService, CategoryDetailService categoryDetailService) {
        this.blogService = blogService;
        this.categoryDetailService = categoryDetailService;
    }

    @GetMapping("/index.htm")
    public String searchBlog(
            ModelMap modelMap,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "5") Integer size,
            @RequestParam(name = "orderBy", defaultValue = "asc") String orderBy,
            @RequestParam(name = "category", required = false) String categorySlug) {
        List<CategoryDetail> details = this.categoryDetailService.findAllByCategorySlug(page, size, orderBy,
                categorySlug);

        List<Blog> blogs = new ArrayList<>();
        for (CategoryDetail detail : details) {
            blogs.add(detail.getBlog());
        }
        modelMap.addAttribute("blogs", blogs);
        return "blog/index";
    }

    @GetMapping("/post.htm")
    public String viewBlog(
            ModelMap modelMap,
            @RequestParam(name = "slug", required = true) String slug) {
        Optional<Blog> blog = this.blogService.findBySlug(slug);
        if (!blog.isPresent()) {
            modelMap.addAttribute("message", "There is some error!");
            return "blog/post";
        }
        modelMap.addAttribute("blog", blog.get());
        return "blog/post";
    }
}
