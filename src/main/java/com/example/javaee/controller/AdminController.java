package com.example.javaee.controller;

import com.example.javaee.dto.BlogDto;
import com.example.javaee.dto.CreateBlogDto;
import com.example.javaee.dto.OpenIdClaims;
import com.example.javaee.dto.UpdateBlogDto;
import com.example.javaee.helper.ServiceResponse;
import com.example.javaee.model.Blog;
import com.example.javaee.model.Category;
import com.example.javaee.service.AdminService;
import com.example.javaee.service.BlogService;
import com.example.javaee.service.CategoryService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;
    private final BlogService blogService;
    private final CategoryService categoryService;

    public AdminController(
            AdminService adminService,
            BlogService blogService,
            CategoryService categoryService) {
        this.adminService = adminService;
        this.blogService = blogService;
        this.categoryService = categoryService;
    }

    @ModelAttribute("categories")
    public List<Category> fetchAllCategories() {
        return this.categoryService.findAll();
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
    public String adminIndexViewRenderer(
            @RequestParam(name = "page", defaultValue = "0", required = false) Integer page,
            @RequestParam(name = "size", defaultValue = "5", required = false) Integer size,
            @RequestParam(name = "orderBy", defaultValue = "asc") String orderBy,
            @RequestParam(name = "slug", required = false) String slug,
            HttpServletRequest request,
            ModelMap modelMap) {
        Optional<OpenIdClaims> claims = this.adminService.validateRequest(request);
        if (!claims.isPresent()) {
            System.out.println(
                    "[Admin Controller] (Admin Index Route) ? Bad request - Cannot fetch access token claims -> Redirect back to landing page");
            return "redirect:/index.htm";
        }
        modelMap.addAttribute("adminInformation", claims.get());

        List<Blog> blogs = this.blogService.findAllBlogByCategorySlug(page, size, orderBy, null);
        modelMap.addAttribute("blogList", blogs);

        return "admin/index";
    }

    @GetMapping("/insert.htm")
    public String createNewBlogViewRenderer(
            HttpServletRequest request,

            ModelMap modelMap) {
        Optional<OpenIdClaims> claims = this.adminService.validateRequest(request);
        if (!claims.isPresent()) {
            System.out.println(
                    "[Admin Controller] (Blog Insert Route) ? Bad request - Cannot fetch access token claims -> Redirect back to landing page");
            return "redirect:/index.htm";
        }
        modelMap.addAttribute("adminInformation", claims.get());

        modelMap.addAttribute("createBlogDto", new CreateBlogDto());

        return "admin/insert";
    }
    @GetMapping("/test/insert.htm")
    public String test_insert(
            HttpServletRequest request,
            ModelMap modelMap) {
        Optional<OpenIdClaims> claims = this.adminService.validateRequest(request);
        if (!claims.isPresent()) {
            System.out.println(
                    "[Admin Controller] (Blog Insert Route) ? Bad request - Cannot fetch access token claims -> Redirect back to landing page");
            return "redirect:/index.htm";
        }
//        System.out.println("1");
//        modelMap.addAttribute("adminInformation", claims.get());
//        System.out.println("2");
//
        modelMap.addAttribute("createBlogDto", new CreateBlogDto());

        return "admin/my_insert";
    }
    @PostMapping("/test/insert.htm")
    public String test_post_mapping(

            HttpServletRequest request,

            ModelMap modelMap,
            @ModelAttribute("title") String title,
            @ModelAttribute("description") String descriptoion,
            @ModelAttribute("cates") List<String> cates ,
            @ModelAttribute("is_popular") boolean is_popular,
            @ModelAttribute("subtitle") String subtitle,
            @ModelAttribute("attachment") MultipartFile file) {
        Optional<OpenIdClaims> claims = this.adminService.validateRequest(request);
        if (!claims.isPresent()) {
            System.out.println(
                    "[Admin Controller] (Blog Insert Route) ? Bad request - Cannot fetch access token claims -> Redirect back to landing page");
            return "redirect:/index.htm";
        }
        System.out.println("title"+title);
        System.out.println("attachment"+ file.getOriginalFilename());
        modelMap.addAttribute("adminInformation", claims.get());
        CreateBlogDto createBlogDto = new CreateBlogDto(title,subtitle,is_popular,descriptoion,file);
        ServiceResponse<Blog> response = this.blogService.create(createBlogDto);
        if (response.isError() || !response.getData().isPresent()) {
            // TODO: handle error here!
            return "redirect:/index.htm";
        }

        return "redirect:/admin/edit/" + response.getData().get().getSlug() + ".htm";
    }
    @ModelAttribute("slugs")
    public Map<String, String> fetchAllSlugs() {
        List<Category> categories = this.categoryService.findAll();
        Map<String, String> slugs = new HashMap<>();
        for (Category category: categories) {
            slugs.put(category.getSlug(), category.getName());
        }
        return slugs;
    }
    @PostMapping("/insert.htm")
    public String creatingNewBlogHandler(
            @ModelAttribute("createBlogDto") CreateBlogDto createBlogDto,
            HttpServletRequest request,
            ModelMap modelMap) {
        Optional<OpenIdClaims> claims = this.adminService.validateRequest(request);
        if (!claims.isPresent()) {
            System.out.println(
                    "[Admin Controller] (Blog Insert Route) ? Bad request - Cannot fetch access token claims -> Redirect back to landing page");
            return "redirect:/index.htm";
        }
        modelMap.addAttribute("adminInformation", claims.get());

        ServiceResponse<Blog> response = this.blogService.create(createBlogDto);
        if (response.isError() || !response.getData().isPresent()) {
            // TODO: handle error here!
            return "redirect:/index.htm";
        }

        return "redirect:/admin/edit/" + response.getData().get().getSlug() + ".htm";
    }

    @GetMapping("/edit/{slug}.htm")
    public String adminEditBlogViewRenderer(
            @PathVariable(name = "slug", required = true) String slug,
            HttpServletRequest request,
            ModelMap modelMap) {
        Optional<OpenIdClaims> claims = this.adminService.validateRequest(request);
        if (!claims.isPresent()) {
            System.out.println(
                    "[Admin Controller] (Blog Insert Route) ? Bad request - Cannot fetch access token claims -> Redirect back to landing page");
            return "redirect:/index.htm";
        }
        modelMap.addAttribute("adminInformation", claims.get());

        Optional<Blog> requestedBlog = this.blogService.findBySlug(slug);
        if (!requestedBlog.isPresent()) {
            // TODO: handle error here
            return "redirect:/admin/index.htm";
        }

        modelMap.addAttribute("blogDto", requestedBlog.get());

        return "admin/edit";
    }

    @PostMapping(value = "/edit/{slug}.htm")
    public String updateBlogHandler(ModelMap modelMap,
            @ModelAttribute("BlogDto") BlogDto blogDto,
            @PathVariable(name = "slug", required = true) String slug,
            HttpServletRequest request) {
        Optional<OpenIdClaims> claims = this.adminService.validateRequest(request);
        if (!claims.isPresent()) {
            System.out.println(
                    "[Admin Controller] (Blog Insert Route) ? Bad request - Cannot fetch access token claims -> Redirect back to landing page");
            return "redirect:/index.htm";
        }
        modelMap.addAttribute("adminInformation", claims.get());

        Optional<Blog> updatingBlog = this.blogService.findBySlug(slug);
        if (!updatingBlog.isPresent()) {
            // TODO: handle error here
            return "redirect:/admin/index.htm";
        }

        UpdateBlogDto payload = new UpdateBlogDto();
        payload.setTitle(blogDto.getTitle());
        payload.setDescription(blogDto.getDescription());
        payload.setAttachment(blogDto.getAttachment());
        // TODO: add update fields for these two fields
        payload.setHiddenStatus(false);
        payload.setPublishAt(new Date());

        // TODO: add update blog service
        this.blogService.update(updatingBlog.get().getId(), payload);

        modelMap.addAttribute("blogDto", blogDto);
        return "admin/edit";
    }
}
