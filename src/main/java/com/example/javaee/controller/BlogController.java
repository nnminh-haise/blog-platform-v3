package com.example.javaee.controller;

import com.example.javaee.dto.CreateBlogDto;
import com.example.javaee.model.Blog;
import com.example.javaee.model.Category;
import com.example.javaee.model.CategoryDetail;
import com.example.javaee.model.Category;
import com.example.javaee.service.BlogService;
import com.example.javaee.service.CategoryService;

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

    private final CategoryService categoryService;

    private final BlogService blogService;

    public BlogController(
            BlogService blogService,
            CategoryService categoryService) {
        this.blogService = blogService;
        this.categoryService = categoryService;
    }

    @GetMapping("/index.htm")
    public String searchBlog(
            ModelMap modelMap,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "5") Integer size,
            @RequestParam(name = "orderBy", defaultValue = "asc") String orderBy,
            @RequestParam(name = "category", required = false) String categorySlug) {
        List<Blog> blogs = this.blogService.findAllBlogByCategorySlug(
                page, size, orderBy, categorySlug);
        if (categorySlug != null) {
            Optional<Category> category = categoryService.findBySlug(categorySlug);
            modelMap.addAttribute("category", category.get());
        } else {
            modelMap.addAttribute("categoryName", "All Stories");
        }

        modelMap.addAttribute("blogs", blogs);

        return "blog/index";
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

        System.out.println("from saver");

        System.out.println("title of the blog: " + createBlogDto.getTitle());
        System.out.println("description of the blog: " + createBlogDto.getDescription() + " - " + createBlogDto.getDescription().length());
        System.out.println("attachment of the blog: " + createBlogDto.getAttachment());
        return "blog/editor";
    }

    @RequestMapping(value = "/{slug}.htm", method = RequestMethod.GET)
    public String viewBlog(
            ModelMap modelMap,
            @PathVariable(name = "slug", required = true) String slug) {
        Optional<Blog> blog = this.blogService.findBySlug(slug);
        if (!blog.isPresent()) {
            modelMap.addAttribute("message", "There is some error!");
            return "blog/index";
        }
        modelMap.addAttribute("blog", blog.get());

        List<Category> categories = new ArrayList<>();
        for (CategoryDetail detail : blog.get().getCategoryDetails()) {
            categories.add(detail.getCategory());
        }
        modelMap.addAttribute("blogCategoryList", categories);

        List<Blog> firstOfCategories = this.blogService.findFirstOfCategories(
                5, categories, blog.get().getId());
        modelMap.addAttribute("nextBlogs", firstOfCategories);

        return "blog/detail";
    }

    @ModelAttribute("categories")
    public List<Category> fetchAllCategories() {
        return this.categoryService.findAll().getData();
    }

    @ModelAttribute("popularBlogs")
    public List<Blog> getFirst5PopularBlogs() {
        return this.blogService.findPopular(5);
    }

    @ModelAttribute("favouriteBlogs")
    public List<Blog> getFavouriteBlogs() {
        return this.blogService.findLast(4);
    }
}
