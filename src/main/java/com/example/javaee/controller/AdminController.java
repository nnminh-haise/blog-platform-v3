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
import org.apache.http.client.ClientProtocolException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final GoogleApiService googleApiService;
    private final BlogService blogService;
    private final CategoryService categoryService;

    public AdminController(
            GoogleApiService googleApiService,
            BlogService blogService,
            CategoryService categoryService) {
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

    @RequestMapping(value = "/insert.htm",method = RequestMethod.GET)
    public String routeToBlogInsert(
            HttpServletRequest request,
            ModelMap model) {
        Optional<OpenIdClaims> claims = this.validateRequest(request);
        if (!claims.isPresent()) {
            System.out.println("[Admin Controller] (Blog Insert Route) ? Bad request - Cannot fetch access token claims -> Redirect back to landing page");
            return "redirect:/index.htm";
        }

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
