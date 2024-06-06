package com.example.javaee.controller;

import com.example.javaee.dto.CreateBlogDto;
import com.example.javaee.dto.OpenIdClaims;
import com.example.javaee.model.Blog;
import com.example.javaee.model.Category;
import com.example.javaee.service.BlogService;
import com.example.javaee.service.CategoryService;
import com.example.javaee.service.FileUploadService;
import com.example.javaee.service.GoogleApiService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.http.client.ClientProtocolException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final FileUploadService fileUploadService;
    private final GoogleApiService googleApiService;
    private final BlogService blogService;
    private final CategoryService categoryService;

    public AdminController(
            FileUploadService fileUploadService,
            GoogleApiService googleApiService,
            BlogService blogService,
            CategoryService categoryService) {
        this.fileUploadService = fileUploadService;
        this.googleApiService = googleApiService;
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
            HttpServletRequest request,
            ModelMap modelMap,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "orderBy", defaultValue = "asc") String orderBy,
            @RequestParam(name = "slug", required = false) String slug) {
        Optional<OpenIdClaims> claims = this.validateRequest(request);
        if (!claims.isPresent()) {
            System.out.println("[Admin Controller] (Admin Index Route) ? Bad request - Cannot fetch access token claims -> Redirect back to landing page");
            return "redirect:/index.htm";
        }

        modelMap.addAttribute("adminInformation", claims.get());

        // TODO: refactor this code
        modelMap.addAttribute("categories", this.categoryService.findAll().getData());
        List<Category> categories = this.categoryService.findAll().getData();
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

    @GetMapping("/insert.htm")
    public String routeToBlogInsert(
            HttpServletRequest request,
            ModelMap model) {
        Optional<OpenIdClaims> claims = this.validateRequest(request);
        if (!claims.isPresent()) {
            System.out.println("[Admin Controller] (Blog Insert Route) ? Bad request - Cannot fetch access token claims -> Redirect back to landing page");
            return "redirect:/index.htm";
        }

        // ! Remove this might cause error!
        model.addAttribute("createBlogDto", new CreateBlogDto());

        return "admin/insert";
    }

    @PostMapping("/insert.htm")
    public String saveBlog(
            @RequestParam(name = "title", required = true) String title,
            @RequestParam(name = "description", required = true) String description,
            @RequestParam(name = "attachment", required = false) MultipartFile file,
            HttpServletRequest request,
            ModelMap model) {
        Optional<OpenIdClaims> claims = this.validateRequest(request);
        if (!claims.isPresent()) {
            System.out.println("[Admin Controller] (Blog Insert Route) ? Bad request - Cannot fetch access token claims -> Redirect back to landing page");
            return "redirect:/index.htm";
        }

        // * These log should be removed
        System.out.println("title:" + title);
        System.out.println("description:" + description);
        System.out.println("file:" + file.getOriginalFilename());

//        * This is for saving data to db
//        ServiceResponse<Blog> response = this.blogService.create(createBlogDto);

        return "admin/insert";
    }

    @ModelAttribute("createBlogDto")
    public CreateBlogDto modelAttributeForBlogEdit() {
        return new CreateBlogDto();
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

    private Optional<OpenIdClaims> validateRequest(HttpServletRequest request) {
        if (request == null) {
            return Optional.empty();
        }

        HttpSession session = request.getSession();
        String accessToken = String.valueOf(session.getAttribute("accessToken"));
        String expireIn = String.valueOf(session.getAttribute("expireIn"));
        if (accessToken == null || expireIn == null) {
            return Optional.empty();
        }

        try {
            OpenIdClaims claims = this.googleApiService.getUserInfo(accessToken);
            return Optional.of(claims);
        } catch (ClientProtocolException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
