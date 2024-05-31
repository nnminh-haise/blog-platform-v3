package com.example.javaee.controller;

import com.example.javaee.model.Blog;
import com.example.javaee.model.CategoryDetail;
import com.example.javaee.service.BlogService;
import com.example.javaee.service.CategoryDetailService;

import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

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
    public String routeToBlogIndex() {
        return "blog/index";
    }

    @GetMapping("/search.htm")
    public String searchBlog(
            ModelMap modelMap,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "5") Integer size,
            @RequestParam(name = "orderBy", defaultValue = "asc") String orderBy,
            @RequestParam(name = "category", required = false) String categorySlug) {
        List<CategoryDetail> details = this.categoryDetailService.findAllByCategorySlug(page, size, orderBy,
                categorySlug);
        for (CategoryDetail detail : details) {
            System.out.println("Blog: " + detail.getBlog().toString());
        }
        return "blog/index";
    }

    @GetMapping("/post.htm")
    public String blogViewer(ModelMap modelMap,
            @RequestParam(name = "slug", required = true) String slug) {
        Optional<Blog> blog = this.blogService.findBySlug(slug);
        System.out.println(blog.toString());
        return "blog/editor";
    }
}
