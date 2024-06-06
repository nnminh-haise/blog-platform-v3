package com.example.javaee.controller;

import com.example.javaee.dto.CreateBlogDto;
import com.example.javaee.dto.OpenIdClaims;
import com.example.javaee.dto.UpdateBlogDto;
import com.example.javaee.helper.ServiceResponse;
import com.example.javaee.model.Blog;
import com.example.javaee.model.Category;
import com.example.javaee.model.CategoryDetail;
import com.example.javaee.service.AdminService;
import com.example.javaee.service.BlogService;
import com.example.javaee.service.CategoryService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final String LANDING_PAGE_URL = "/index.htm";
    private final String REDIRECT_TO = "redirect:";
    private final BlogService blogService;
    private final CategoryService categoryService;
    private final AdminService adminService;

    public AdminController(
            AdminService adminService,
            BlogService blogService,
            CategoryService categoryService) {
        this.adminService = adminService;
        this.blogService = blogService;
        this.categoryService = categoryService;
    }

    /**
     * <h1>Notes</h1>
     * <p>
     * The `code` parameter is put before the Google Login Controller redirect to
     * the Admin Controller. This code is required for the Login Interceptor
     * varifying the login account is authorized or not.
     * </p>
     * <p>
     * The `accessToken` value is put into the session storage when the Login
     * Interceptor is executing the preHandle method. This is access token is
     * required for the controller to fetch the user's data from Google.
     * </p>
     */
    @GetMapping("/index.htm")
    public String cmsIndexRoute(
            HttpServletRequest request,
            @RequestParam(name = "code") String code,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "orderBy", defaultValue = "asc") String orderBy,
            @RequestParam(name = "slug", required = false) String slug,
            ModelMap modelMap) {
        // * this is route validation
        Optional<OpenIdClaims> claims = this.adminService.validateCode(request, code);
        if (!claims.isPresent()) {
            return REDIRECT_TO + LANDING_PAGE_URL;
        }

        modelMap.addAttribute("userInformation", claims.get());
        // Expose `code` value to view for request
        modelMap.addAttribute("validationCode", code);

//        // TODO refactor this code
//        modelMap.addAttribute("categories", this.categoryService.findAll().getData());
//        List<Category> categories = this.categoryService.findAll().getData();
//        for (Category category : categories) {
//            System.out.println("category: " + category.getName() + " - " + category.getSlug());
//        }
//        System.out.println("slug: " + slug);
//        System.out.println("orderBy: " + orderBy);
//        System.out.println("page: " + page);
//        if (slug != null) {
//            List<Blog> blogs = this.blogService.findAllBlogByCategorySlug(
//                    page, 5, orderBy, slug);
//            modelMap.addAttribute("blogs", blogs);
//
//        } else {
//            List<Blog> blogs = this.blogService.findAllBlogOrderBy(
//                    page, 5, orderBy);
//            modelMap.addAttribute("blogs", blogs);
//
//        }

        return "admin/index";
    }

    @RequestMapping(value = "/insert.htm", method = RequestMethod.GET)
    public String cmsInsertRoute(
            HttpServletRequest request,
            @RequestParam(name = "code") String code,
            ModelMap model) {
        // * this is route validation
        Optional<OpenIdClaims> claims = this.adminService.validateCode(request, code);
        if (!claims.isPresent()) {
            return REDIRECT_TO + LANDING_PAGE_URL;
        }

        model.addAttribute("createBlogDto", new CreateBlogDto());

        return "admin/insert";
    }

    @PostMapping(value = "/insert.htm")
    public String cmsSavingRoute(
            HttpServletRequest request,
            @RequestParam(name = "code") String code,
            @ModelAttribute("createBlogDto") CreateBlogDto createBlogDto,
            ModelMap model) {
        // * this is route validation
        Optional<OpenIdClaims> claims = this.adminService.validateCode(request, code);
        if (!claims.isPresent()) {
            return REDIRECT_TO + LANDING_PAGE_URL;
        }

        ServiceResponse<Blog> response = this.blogService.create(createBlogDto);

        return "admin/insert";
    }

    // ? Why need fetch all slug?
    @ModelAttribute("slugs")
    public Map<String, String> fetchAllSlugs() {
        List<Category> categories = this.categoryService.findAll().getData();
        Map<String, String> slugs = new java.util.HashMap<>();
        for (Category category : categories) {
            slugs.put(category.getSlug(), category.getName());
        }
        return slugs;
    }

    @ModelAttribute("createBlogDto")
    public CreateBlogDto generatePlainDto() {
        return new CreateBlogDto();
    }

    @ModelAttribute("categories")
    public List<Category> fetchAllCategoriesForFilterOptions() {
        return this.categoryService.findAll().getData();
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

    @RequestMapping(value = "/edit/{slug}.htm", method = RequestMethod.GET)
    public String viewBlog(
            ModelMap modelMap,
            @PathVariable(name = "slug", required = true) String slug) {
        System.out.println(slug);
        Optional<Blog> blog = this.blogService.findBySlug(slug);
        System.out.println("blog");
        System.out.println(blog);
        if (!blog.isPresent()) {
            modelMap.addAttribute("message", "There is some error!");
            return "admin/index";
        }
        modelMap.addAttribute("updateBlogDto", blog.get());
        modelMap.addAttribute("slug", slug); // Add slug to the model

        List<Category> categories = new ArrayList<>();
        for (CategoryDetail detail : blog.get().getCategoryDetails()) {
            categories.add(detail.getCategory());
        }
        modelMap.addAttribute("blogCategoryList", categories);

        List<Blog> firstOfCategories = this.blogService.findFirstOfCategories(
                5, categories, blog.get().getId());
        modelMap.addAttribute("nextBlogs", firstOfCategories);

        return "admin/edit";
    }

    @PostMapping(value = "/edit/{slug}.htm")
    public String updateBlog( ModelMap modelMap, @ModelAttribute("updateBlogDto") UpdateBlogDto updateBlogDto,
                            @PathVariable(name = "slug", required = true) String slug) {
            System.out.println("update");
        System.out.println(slug);
        return "admin/edit";
    }

}
