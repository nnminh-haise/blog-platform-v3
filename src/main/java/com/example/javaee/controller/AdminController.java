package com.example.javaee.controller;

import com.example.javaee.dto.CreateBlogDto;
import com.example.javaee.dto.CreateCategoryDetailDto;
import com.example.javaee.dto.OpenIdClaims;
import com.example.javaee.dto.UpdateBlogDto;
import com.example.javaee.helper.ErrorResponse;
import com.example.javaee.helper.ServiceResponse;
import com.example.javaee.model.Blog;
import com.example.javaee.model.Category;
import com.example.javaee.model.CategoryDetail;
import com.example.javaee.service.AdminService;
import com.example.javaee.service.BlogService;
import com.example.javaee.service.CategoryDetailService;
import com.example.javaee.service.CategoryService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;
    private final BlogService blogService;
    private final CategoryService categoryService;

    private final CategoryDetailService categoryDetailService;

    public AdminController(
            AdminService adminService,
            BlogService blogService,
            CategoryService categoryService,
            CategoryDetailService categoryDetailService) {
        this.adminService = adminService;
        this.blogService = blogService;
        this.categoryService = categoryService;
        this.categoryDetailService = categoryDetailService;
    }

    @GetMapping("/index.htm")
    public String adminIndexViewRenderer(
            @RequestParam(name = "page", defaultValue = "0", required = false) Integer page,
            @RequestParam(name = "size", defaultValue = "5", required = false) Integer size,
            @RequestParam(name = "orderBy", defaultValue = "asc", required = false) String orderBy,
            @RequestParam(name = "slug", required = false) String slug,
            HttpServletRequest request,
            ModelMap modelMap) {
        ServiceResponse<OpenIdClaims> response = this.adminService.validateRequest(request);
        if (response.isError()) {
            ErrorResponse errorResponse = response.buildError();
            modelMap.addAttribute("errorResponse", errorResponse);
            return "redirect:/error.htm";
        }
        if (!response.getData().isPresent()) {
            modelMap.addAttribute("errorResponse", ErrorResponse.buildUnknownServerError(
                    "Cannot Found User's Claim",
                    "Cannot Find User's Claim Due To Unknown Server Error"));
            return "redirect:/error.htm";
        }
        modelMap.addAttribute("adminInformation", response.getData().get());
        modelMap.addAttribute("categories", this.categoryService.findAll());
        List<Blog> blogs = this.blogService.findAllBlogByCategorySlug(page, size, orderBy, slug);
        modelMap.addAttribute("blogList", blogs);

        modelMap.addAttribute("categories", this.categoryService.findAll());

        // * Send current requesting options
        modelMap.addAttribute("currentPage", page);
        modelMap.addAttribute("currentSize", size);
        modelMap.addAttribute("totalNumberOfPage", this.blogService.countMaximumNumberOfPage(size));
        modelMap.addAttribute("orderBy", orderBy);
        modelMap.addAttribute("filterBySlug", slug);

        return "admin/index";
    }

    @GetMapping("/insert.htm")
    public String createNewBlogViewRenderer(
            HttpServletRequest request,
            ModelMap modelMap) {
        ServiceResponse<OpenIdClaims> response = this.adminService.validateRequest(request);
        if (response.isError()) {
            ErrorResponse errorResponse = response.buildError();
            modelMap.addAttribute("errorResponse", errorResponse);
            return "redirect:/error.htm";
        }
        if (!response.getData().isPresent()) {
            modelMap.addAttribute("errorResponse", ErrorResponse.buildUnknownServerError(
                    "Cannot Found User's Claim",
                    "Cannot Find User's Claim Due To Unknown Server Error"));
            return "redirect:/error.htm";
        }
        modelMap.addAttribute("adminInformation", response.getData().get());

        modelMap.addAttribute("createBlogDto", new CreateBlogDto());

        List<Category> categories = this.categoryService.findAll();
        modelMap.addAttribute("categories", categories);

        return "admin/insert";
    }

    @PostMapping("/insert.htm")
    public String creatingNewBlogHandler(
            @ModelAttribute("createBlogDto") CreateBlogDto createBlogDto,
            @RequestParam("categories") String[] categorySlugs,
            HttpServletRequest request,
            ModelMap modelMap) {
        ServiceResponse<OpenIdClaims> response = this.adminService.validateRequest(request);
        if (response.isError()) {
            ErrorResponse errorResponse = response.buildError();
            modelMap.addAttribute("errorResponse", errorResponse);
            return "redirect:/error.htm";
        }
        if (!response.getData().isPresent()) {
            modelMap.addAttribute("errorResponse", ErrorResponse.buildUnknownServerError(
                    "Cannot Found User's Claim",
                    "Cannot Find User's Claim Due To Unknown Server Error"));
            return "redirect:/error.htm";
        }
        modelMap.addAttribute("adminInformation", response.getData().get());

        ServiceResponse<Blog> blogServiceResponse = this.blogService.create(createBlogDto);
        if (blogServiceResponse.isError() || !blogServiceResponse.getData().isPresent()) {
            modelMap.addAttribute("errorResponse", blogServiceResponse.buildError());
            return "redirect:/error.htm";
        }

        for (String slug: categorySlugs) {
            Optional<Category> category = this.categoryService.findBySlug(slug);
            if (!category.isPresent()) {
                modelMap.addAttribute("errorResponse", ErrorResponse.buildUnknownServerError(
                        "Category Not Found",
                        "Cannot Find Any Category With Slug = " + slug));
                return "redirect:/error.htm";
            }

            CreateCategoryDetailDto categoryDetailDto = new CreateCategoryDetailDto();
            categoryDetailDto.setBlogId(blogServiceResponse.getData().get().getId());
            categoryDetailDto.setCategoryId(category.get().getId());
            ServiceResponse<CategoryDetail> categoryDetailServiceResponse = this.categoryDetailService
                    .create(categoryDetailDto);
            if (categoryDetailServiceResponse.isError()) {
                modelMap.addAttribute("errorResponse", categoryDetailServiceResponse.buildError());
                return "redirect:/error.htm";
            }
        }

        return "redirect:/admin/edit/" + blogServiceResponse.getData().get().getSlug() + ".htm";
    }

    @GetMapping("/edit/{slug}.htm")
    public String adminEditBlogViewRenderer(
            @PathVariable(name = "slug") String slug,
            HttpServletRequest request,
            ModelMap modelMap) {
        ServiceResponse<OpenIdClaims> response = this.adminService.validateRequest(request);
        if (response.isError()) {
            ErrorResponse errorResponse = response.buildError();
            modelMap.addAttribute("errorResponse", errorResponse);
            return "redirect:/error.htm";
        }
        if (!response.getData().isPresent()) {
            modelMap.addAttribute("errorResponse", ErrorResponse.buildUnknownServerError(
                    "Cannot Found User's Claim",
                    "Cannot Find User's Claim Due To Unknown Server Error"));
            return "redirect:/error.htm";
        }
        modelMap.addAttribute("adminInformation", response.getData().get());

        Optional<Blog> requestedBlog = this.blogService.findBySlug(slug);
        if (!requestedBlog.isPresent()) {
            modelMap.addAttribute("errorResponse", ErrorResponse.buildBadRequest(
                    "Invalid Blog Slug",
                    "Cannot Find Any Blog With The Given Slug"));
            return "redirect:/error.htm";
        }
        Blog blog = requestedBlog.get();
        modelMap.addAttribute("selectingBlog", blog);

        UpdateBlogDto updateBlogDto = new UpdateBlogDto();
        updateBlogDto.setTitle(blog.getTitle());
        updateBlogDto.setDescription(blog.getDescription());
        updateBlogDto.setIsPopular(blog.getIsPopular());
        updateBlogDto.setSubTitle(blog.getSubTitle());
        modelMap.addAttribute("updateBlogDto", updateBlogDto);

        List<Category> blogCategories = blog.getCategories();
        modelMap.addAttribute("blogCategories", blogCategories);

        List<Category> fullCategories = this.categoryService.findAll();
        List<Category> otherCategories = fullCategories
                .stream()
                .filter(category -> blogCategories
                        .stream()
                        .noneMatch(blogCategory -> blogCategory.getSlug().equals(category.getSlug())))
                .collect(Collectors.toList());
        modelMap.addAttribute("otherCategories", otherCategories);

        return "admin/edit";
    }

    @PostMapping(value = "/edit/{slug}.htm")
    public String updateBlogHandler(ModelMap modelMap,
            @RequestParam(value = "categories", required = false) String[] categorySlugs,
            @ModelAttribute("updateBlogDto") UpdateBlogDto updateBlogDto,
            @PathVariable(name = "slug") String slug,
            HttpServletRequest request) {

        ServiceResponse<OpenIdClaims> response = this.adminService.validateRequest(request);
        if (response.isError()) {
            ErrorResponse errorResponse = response.buildError();
            modelMap.addAttribute("errorResponse", errorResponse);
            return "redirect:/error.htm";
        }
        if (!response.getData().isPresent()) {
            modelMap.addAttribute("errorResponse", ErrorResponse.buildUnknownServerError(
                    "Cannot Found User's Claim",
                    "Cannot Find User's Claim Due To Unknown Server Error"));
            return "redirect:/error.htm";
        }
        modelMap.addAttribute("adminInformation", response.getData().get());

        Optional<Blog> requestedBlog = this.blogService.findBySlug(slug);
        if (!requestedBlog.isPresent()) {
            modelMap.addAttribute("errorResponse", ErrorResponse.buildBadRequest(
                    "Invalid Blog Slug",
                    "Cannot Find Any Blog With The Given Slug"));
            return "redirect:/error.htm";
        }

        ServiceResponse<Blog> blogServiceResponse = this.blogService.update(requestedBlog.get().getId(), updateBlogDto);
        if (blogServiceResponse.isError() || !blogServiceResponse.getData().isPresent()) {
            modelMap.addAttribute("errorResponse", blogServiceResponse.buildError());
            return "redirect:/error.htm";
        }

        List<String> blogCategories = requestedBlog
                .get()
                .getCategories()
                .stream()
                .map(Category::getSlug)
                .collect(Collectors.toList());

        for (String categorySlug: categorySlugs) {
            if (blogCategories.stream().anyMatch(blogCategorySlug -> blogCategorySlug.equals(categorySlug))) {
                continue;
            }

            Optional<Category> category = this.categoryService.findBySlug(categorySlug);
            if (!category.isPresent()) {
                modelMap.addAttribute("errorResponse", ErrorResponse.buildUnknownServerError(
                        "Category Not Found",
                        "Cannot Find Any Category With Slug = " + slug));
                return "redirect:/error.htm";
            }

            CreateCategoryDetailDto categoryDetailDto = new CreateCategoryDetailDto();
            categoryDetailDto.setBlogId(blogServiceResponse.getData().get().getId());
            categoryDetailDto.setCategoryId(category.get().getId());
            ServiceResponse<CategoryDetail> categoryDetailServiceResponse = this.categoryDetailService
                    .create(categoryDetailDto);
            if (categoryDetailServiceResponse.isError()) {
                modelMap.addAttribute("errorResponse", categoryDetailServiceResponse.buildError());
                return "redirect:/error.htm";
            }
        }

        return "redirect:/admin/edit/" + requestedBlog.get().getSlug() + ".htm";
    }
}
