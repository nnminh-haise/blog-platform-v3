package com.example.javaee.controller;

import com.example.javaee.dto.CreateBlogDto;
import com.example.javaee.dto.OpenIdClaims;
import com.example.javaee.helper.ServiceResponse;
import com.example.javaee.model.Blog;
import com.example.javaee.model.Category;
import com.example.javaee.service.BlogService;
import com.example.javaee.service.CategoryService;
import com.example.javaee.service.GoogleApiService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final BlogService blogService;

    private final CategoryService categoryService;

    private final GoogleApiService googleApiService;

    public AdminController(
            GoogleApiService googleApiService,
            BlogService blogService,
            CategoryService categoryService) {
        this.googleApiService = googleApiService;
        this.blogService = blogService;
        this.categoryService = categoryService;
    }

    @ModelAttribute("categories")
    public List<Category> fetchAllCategories() {
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

    @GetMapping("/index.htm")
    public String searchBlogAdmin(
            HttpServletRequest request,
            ModelMap modelMap,
            @RequestParam(name = "code", required = false) String code,
            @RequestParam(name = "dev", required = false) String dev,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "orderBy", defaultValue = "asc") String orderBy,
            @RequestParam(name = "slug", required = false) String slug) throws IOException {
        // TODO: remove this devMode filter in production
//        Boolean devMode = (dev != null && dev.equals("1"));
//        if (!devMode) {
//            // ! do not remove this code param filter
//            if (code == null) {
//                return "redirect:/index.htm";
//            }
//        }
//
//        HttpSession session = request.getSession();
//        String accessToken = (String) session.getAttribute("accessToken");
//        // TODO: remove dev mode filter
//        if (!devMode && accessToken == null) {
//            return "redirect:/index.htm";
//        }
//
//        // TODO: Remove this dev mode if statement and return these two statement ouf of the if statement.
//        if (devMode) {
//            modelMap.addAttribute("userInformation", new OpenIdClaims(
//                    "sub",
//                    "email",
//                    "verified_email",
//                    "name",
//                    "given_name",
//                    "family_name",
//                    "picture"
//            ));
//        }
//        else {
//            // ! When removing the if statement this will be kept
//            OpenIdClaims claims = this.googleApiService.getUserInfo(accessToken);
//            modelMap.addAttribute("userInformation", claims);
//        }

        modelMap.addAttribute("categories", this.categoryService.findAll().getData());
        List<Category> categories = this.categoryService.findAll().getData();
        for (Category category : categories) {
            System.out.println("category: " + category.getName() + " - " + category.getSlug());
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
        for (Category category : categories) {
            slugs.put(category.getSlug(), category.getName());
        }
        return slugs;
    }

    @RequestMapping(value = "/insert.htm", method = RequestMethod.GET)
    public String routeToBlogInsert(ModelMap model) {
        model.addAttribute("createBlogDto", new CreateBlogDto());

        return "admin/insert";
    }
    @ModelAttribute("createBlogDto")
    public CreateBlogDto generatePlainDto() {
        return new CreateBlogDto();
    }
    @PostMapping(value = "/insert.htm")
    public String saveBlog(ModelMap model, @ModelAttribute("createBlogDto") CreateBlogDto createBlogDto) {
        System.out.println("from saver" + createBlogDto);
        System.out.println("Title" + createBlogDto.getTitle());
        System.out.println("Description" + createBlogDto.getDescription());

        ServiceResponse<Blog> response = this.blogService.create(createBlogDto);

        return "admin/insert";
    }
}
