package com.example.javaee.controller;

import com.example.javaee.dto.CreateBlogDto;
import com.example.javaee.dto.ErrorResponse;
import com.example.javaee.dto.ResponseDto;
import com.example.javaee.model.Blog;
import com.example.javaee.service.BlogService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/blogs")
public class BlogController {

    private final BlogService blogService;

    public BlogController(BlogService blogService) {
        this.blogService = blogService;
    }

    @GetMapping("index.htm")
    public String routeToBlogIndex() {
        return "blog/index";
    }

    @ModelAttribute("blogs")
    public List<Blog> fetchAllBlogs() {
        ResponseDto<List<Blog>> response = blogService.findAll();

        if (!response.getStatus().equals(HttpStatus.OK.value())) {
            return new ArrayList<>();
        }
        return response.getData();
    }
    @GetMapping("detail.htm")
    public String routeToBlogDetail() {return "blog/detail";}

    @GetMapping("/editor.htm")
    public String routeToEditor(ModelMap model) {
        model.addAttribute("createBlogDto", new CreateBlogDto());
        return "blog/editor";
    }

    @PostMapping("/saver.htm")
    public String saverBlog(
            @ModelAttribute("createBlogDto") CreateBlogDto createBlogDto,
            ModelMap model) {

        System.out.println("title of the blog: " + createBlogDto.getTitle());
        System.out.println("description of the blog: " + createBlogDto.getDescription() + " - " + createBlogDto.getDescription().length());
        System.out.println("attachment of the blog: " + createBlogDto.getAttachment());
        return "blog/editor";
    }

    @GetMapping("/edit.htm")
    public String routeToBlogEditor(
            @RequestParam(name = "id", required = false) UUID id,
            ModelMap model) {
        if (id != null) {
            ResponseDto<Blog> response = this.blogService.findById(id);
            if (response.hasStatus(HttpStatus.OK)) {
                model.addAttribute("createBlogDto", new CreateBlogDto(
                        response.getData().getTitle(),
                        response.getData().getDescription(),
                        response.getData().getAttachment()
                ));
            }
            else {
                model.addAttribute("createBlogDto", new CreateBlogDto());
                model.addAttribute("errorMessage", new ErrorResponse(
                        response.getStatus(),
                        response.getStatus().toString(),
                        response.getMessage()
                ));
            }
        }
        else {
            model.addAttribute("createBlogDto", new CreateBlogDto());
        }
        return "blog/edit";
    }

    @PostMapping("/save.htm")
    public String saveBlog(
            @ModelAttribute("createBlogDto") CreateBlogDto createBlogDto,
            ModelMap model) {
        String resultView = "blog/edit";

        if (createBlogDto == null) {
            model.addAttribute("errorMessage", new ErrorResponse(
               HttpStatus.BAD_REQUEST.value(),
               HttpStatus.BAD_REQUEST.toString(),
               "Invalid blog"
            ));
            return resultView;
        }

        ResponseDto<Blog> response = this.blogService.create(createBlogDto);
        if (!response.hasStatus(HttpStatus.OK)) {
            model.addAttribute("errorMessage", new ErrorResponse(
                    response.getStatus(),
                    response.getStatus().toString(),
                    response.getMessage()
            ));
            return resultView;
        }

        model.addAttribute("blogs", response.getData());
        return "redirect:/blogs/index.htm";
    }
}
