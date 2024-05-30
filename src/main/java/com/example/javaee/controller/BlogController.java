package com.example.javaee.controller;

import com.example.javaee.dto.CreateBlogDto;
import com.example.javaee.dto.ErrorResponse;
import com.example.javaee.dto.ResponseDto;
import com.example.javaee.model.Blog;
import com.example.javaee.service.BlogService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/blogs")
@Valid
public class BlogController {

    private final BlogService blogService;

    public BlogController(BlogService blogService) {
        this.blogService = blogService;
    }

    @GetMapping("index.htm")
    public String routeToBlogIndex() {
        return "blog/index";
    }

    @GetMapping("/editor.htm")
    public String routeToEditor(ModelMap model) {
        model.addAttribute("createBlogDto", new CreateBlogDto());
        return "blog/editor";
    }

    @ModelAttribute("blogs")
    public List<Blog> fetchAllBlogs() {
        return blogService.findAll();
    }
}
