package com.example.javaee.controller;

import com.example.javaee.dto.CreateBlogDto;
import com.example.javaee.helper.ServiceResponse;
import com.example.javaee.model.Blog;
import com.example.javaee.model.Category;
import com.example.javaee.service.BlogService;
import com.example.javaee.service.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final BlogService blogService;

    private final CategoryService categoryService;

    public AdminController(BlogService blogService, CategoryService categoryService) {
        this.blogService = blogService;
        this.categoryService = categoryService;
    }

    @ModelAttribute("totalPages")
    public Integer getLimitBlogPage() {
        final int PAGE_SIZE = 5;

        List<Blog> blogs = this.blogService.findAll();
        int totalBlogs = blogs.size();
        int totalPages = totalBlogs / PAGE_SIZE;
        if (totalBlogs % PAGE_SIZE != 0) {
            totalPages++;
        }
        return totalPages;
    }

    @GetMapping("/index.htm")
    public String searchBlogAdmin(
            ModelMap modelMap,
            @RequestParam(name = "code", required = false) String code,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "orderBy", defaultValue = "asc") String orderBy,
            @RequestParam(name = "slug", required = false) String slug) {
        if (code == null) {
            return "redirect:/index.htm";
        }

        modelMap.addAttribute("categories", this.categoryService.findAll().getData());
        List<Category> categories = this.categoryService.findAll().getData();
        for (Category category: categories) {
            System.out.println("category: " + category.getName() + " - " + category.getSlug()   );
        }
        System.out.println("slug: " + slug);
        System.out.println("orderBy: " + orderBy);
        System.out.println("page: " + page);
        if (slug != null) {
            List<Blog> blogs = this.blogService.findAllBlogByCategorySlug(
                    page, 5, orderBy, slug);
            modelMap.addAttribute("blogs", blogs);


        } else {
            List<Blog> blogs = this.blogService.findAllBlogOrderBy(
                    page, 5, orderBy);
            modelMap.addAttribute("blogs", blogs);

        }


        return "admin/index";
    }
    @ModelAttribute("slugs")
    public Map<String, String> fetchAllSlugs() {
        List<Category> categories = this.categoryService.findAll().getData();
        Map<String, String> slugs = new java.util.HashMap<>();
        for (Category category: categories) {
            slugs.put(category.getSlug(), category.getName());
        }
        return slugs;
    }
    @RequestMapping(value = "/insert.htm",method = RequestMethod.GET)
    public String routeToBlogInsert(ModelMap model) {
        model.addAttribute("createBlogDto", new CreateBlogDto());

        return "blog/insert";
    }
    @RequestMapping(value = "/insert.htm", method = RequestMethod.POST)
    public String saveBlog(ModelMap model,
                           @ModelAttribute("createBlogDto") CreateBlogDto createBlogDto)
    {
        System.out.println("from saver"+ createBlogDto);
        System.out.println("Title"+ createBlogDto.getTitle());
        System.out.println("Description"+ createBlogDto.getDescription());
        System.out.println("Attachment"+ createBlogDto.getAttachment());
        System.out.println("Slug"+ createBlogDto.getSlug());
        ServiceResponse<Blog> response = this.blogService.create(createBlogDto);

        return "admin/insert";
    }
}
