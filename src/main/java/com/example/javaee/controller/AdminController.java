package com.example.javaee.controller;

import com.example.javaee.dto.BlogDto;
import com.example.javaee.dto.CreateBlogDto;
import com.example.javaee.dto.OpenIdClaims;
import com.example.javaee.dto.UpdateBlogDto;
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
import java.util.Date;
import java.util.List;
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
        Optional<OpenIdClaims> claims = this.validateRequest(request);
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
        Optional<OpenIdClaims> claims = this.validateRequest(request);
        if (!claims.isPresent()) {
            System.out.println(
                    "[Admin Controller] (Blog Insert Route) ? Bad request - Cannot fetch access token claims -> Redirect back to landing page");
            return "redirect:/index.htm";
        }
        modelMap.addAttribute("adminInformation", claims.get());

        modelMap.addAttribute("createBlogDto", new CreateBlogDto());

        return "admin/insert";
    }

    @PostMapping("/insert.htm")
    public String creatingNewBlogHandler(
            @ModelAttribute("createBlogDto") CreateBlogDto createBlogDto,
            HttpServletRequest request,
            ModelMap modelMap) {
        Optional<OpenIdClaims> claims = this.validateRequest(request);
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
        Optional<OpenIdClaims> claims = this.validateRequest(request);
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
            @PathVariable(name = "slug", required = true) String slug) {

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
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
