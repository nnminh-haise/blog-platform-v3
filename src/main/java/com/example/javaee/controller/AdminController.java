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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
            RedirectAttributes redirectAttributes,
            HttpServletRequest request,
            ModelMap modelMap) {
        ServiceResponse<OpenIdClaims> response = this.adminService.validateRequest(request);
        if (response.isError()) {
            redirectAttributes.addFlashAttribute(
                    "errorResponse",
                    response.buildError());
            return "redirect:/error.htm";
        }
        if (!response.getData().isPresent()) {
            redirectAttributes.addFlashAttribute(
                    "errorResponse",
                    ErrorResponse.buildUnknownServerError(
                            "User's Claim Not Found",
                            "Cannot Find User's Claim Due To Unknown Server Error"));
            return "redirect:/error.htm";
        }
        modelMap.addAttribute("adminInformation", response.getData().get());

        modelMap.addAttribute("categoryListForFilterOptions", this.categoryService.findAll());

        List<Blog> blogs = this.blogService.findAllBlogByCategorySlug(page, size, orderBy, slug);
        modelMap.addAttribute("blogList", blogs);

        // * Send current requesting options
        modelMap.addAttribute("currentPage", page);
        modelMap.addAttribute("currentSize", size);
        modelMap.addAttribute("numberOfPage", this.blogService.countNumberOfPage(size, slug));
        modelMap.addAttribute("orderBy", orderBy);
        modelMap.addAttribute("slug", slug);

        return "admin/index";
    }

    @GetMapping("/insert.htm")
    public String createNewBlogViewRenderer(
            RedirectAttributes redirectAttributes,
            HttpServletRequest request,
            ModelMap modelMap) {
        ServiceResponse<OpenIdClaims> response = this.adminService.validateRequest(request);
        if (response.isError()) {
            redirectAttributes.addFlashAttribute("errorResponse", response.buildError());
            return "redirect:/error.htm";
        }
        if (!response.getData().isPresent()) {
            redirectAttributes.addFlashAttribute(
                    "errorResponse",
                    ErrorResponse.buildUnknownServerError(
                            "User's Claim Not Found",
                            "Cannot Find User's Claim Due To Unknown Server Error"));
            return "redirect:/error.htm";
        }
        modelMap.addAttribute("adminInformation", response.getData().get());

        // * Creating a new dto for a new blog
        modelMap.addAttribute("createBlogDto", new CreateBlogDto());

        // * All available categories for the new blog
        List<Category> categories = this.categoryService.findAll();
        modelMap.addAttribute("categories", categories);

        return "admin/insert";
    }

    @PostMapping("/insert.htm")
    public String creatingNewBlogHandler(
            @ModelAttribute("createBlogDto") CreateBlogDto createBlogDto,
            @RequestParam(value = "categories", required = false) List<String> categorySlugs,
            RedirectAttributes redirectAttributes,
            HttpServletRequest request,
            ModelMap modelMap) {
        ServiceResponse<OpenIdClaims> response = this.adminService.validateRequest(request);
        if (response.isError()) {
            redirectAttributes.addFlashAttribute("errorResponse", response.buildError());
            return "redirect:/error.htm";
        }
        if (!response.getData().isPresent()) {
            redirectAttributes.addFlashAttribute(
                    "errorResponse",
                    ErrorResponse.buildUnknownServerError(
                            "User's Claim Not Found",
                            "Cannot Find User's Claim Due To Unknown Server Error"));
            return "redirect:/error.htm";
        }
        modelMap.addAttribute("adminInformation", response.getData().get());

        ServiceResponse<Blog> blogServiceResponse = this.blogService.create(createBlogDto);
        if (blogServiceResponse.isError() || !blogServiceResponse.getData().isPresent()) {
            redirectAttributes.addFlashAttribute("errorResponse", blogServiceResponse.buildError());
            return "redirect:/error.htm";
        }
        redirectAttributes.addFlashAttribute("alertMessage", blogServiceResponse.getDescription());

        if (categorySlugs == null || categorySlugs.isEmpty()) {
            return "redirect:/admin/edit/" + blogServiceResponse.getData().get().getSlug() + ".htm";
        }

        for (String slug: categorySlugs) {
            Optional<Category> category = this.categoryService.findBySlug(slug);
            if (!category.isPresent()) {
                redirectAttributes.addFlashAttribute(
                        "errorResponse",
                        ErrorResponse.buildUnknownServerError(
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
                redirectAttributes.addFlashAttribute(
                        "errorResponse",
                        categoryDetailServiceResponse.buildError());
                return "redirect:/error.htm";
            }
        }

        return "redirect:/admin/edit/" + blogServiceResponse.getData().get().getSlug() + ".htm";
    }

    @GetMapping("/edit/{slug}.htm")
    public String adminEditBlogViewRenderer(
            @PathVariable(name = "slug") String slug,
            RedirectAttributes redirectAttributes,
            HttpServletRequest request,
            ModelMap modelMap) {
        ServiceResponse<OpenIdClaims> response = this.adminService.validateRequest(request);
        if (response.isError()) {
            ErrorResponse errorResponse = response.buildError();
            redirectAttributes.addFlashAttribute("errorResponse", errorResponse);
            return "redirect:/error.htm";
        }
        if (!response.getData().isPresent()) {
            redirectAttributes.addFlashAttribute(
                    "errorResponse",
                    ErrorResponse.buildUnknownServerError(
                            "Cannot Found User's Claim",
                            "Cannot Find User's Claim Due To Unknown Server Error"));
            return "redirect:/error.htm";
        }
        modelMap.addAttribute("adminInformation", response.getData().get());

        Optional<Blog> requestedBlog = this.blogService.findBySlug(slug);
        if (!requestedBlog.isPresent()) {
            redirectAttributes.addFlashAttribute(
                    "errorResponse",
                    ErrorResponse.buildBadRequest(
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
    public String updateBlogHandler(
            @RequestParam(value = "categories", required = false) List<String> selectingCategorySlugs,
            @ModelAttribute("updateBlogDto") UpdateBlogDto updateBlogDto,
            @PathVariable(name = "slug") String slug,
            RedirectAttributes redirectAttributes,
            HttpServletRequest request,
            ModelMap modelMap) {
        ServiceResponse<OpenIdClaims> response = this.adminService.validateRequest(request);
        if (response.isError()) {
            ErrorResponse errorResponse = response.buildError();
            redirectAttributes.addFlashAttribute("errorResponse", errorResponse);
            return "redirect:/error.htm";
        }
        if (!response.getData().isPresent()) {
            redirectAttributes.addFlashAttribute(
                    "errorResponse",
                    ErrorResponse.buildUnknownServerError(
                            "Cannot Found User's Claim",
                            "Cannot Find User's Claim Due To Unknown Server Error"));
            return "redirect:/error.htm";
        }
        modelMap.addAttribute("adminInformation", response.getData().get());

        Optional<Blog> requestedBlog = this.blogService.findBySlug(slug);
        if (!requestedBlog.isPresent()) {
            redirectAttributes.addFlashAttribute(
                    "errorResponse",
                    ErrorResponse.buildBadRequest(
                            "Invalid Blog Slug",
                            "Cannot Find Any Blog With The Given Slug"));
            return "redirect:/error.htm";
        }

        ServiceResponse<Blog> blogServiceResponse = this.blogService.update(requestedBlog.get().getId(), updateBlogDto);
        if (blogServiceResponse.isError() || !blogServiceResponse.getData().isPresent()) {
            redirectAttributes.addFlashAttribute("errorResponse", blogServiceResponse.buildError());
            return "redirect:/error.htm";
        }

//        List<Category> currentBlogCategories = requestedBlog.get().getCategories();
//        for (Category currentBlogCategory: currentBlogCategories) {
//            for (String selectingCategorySlug: selectingCategorySlugs) {
//
//            }
//        }

//        List<String> blogCategories = requestedBlog
//                .get()
//                .getCategories()
//                .stream()
//                .map(Category::getSlug)
//                .collect(Collectors.toList());
//
//        for (String categorySlug: categorySlugs) {
//            if (blogCategories.stream().anyMatch(blogCategorySlug -> blogCategorySlug.equals(categorySlug))) {
//                continue;
//            }
//
//            Optional<Category> category = this.categoryService.findBySlug(categorySlug);
//            if (!category.isPresent()) {
//                modelMap.addAttribute("errorResponse", ErrorResponse.buildUnknownServerError(
//                        "Category Not Found",
//                        "Cannot Find Any Category With Slug = " + slug));
//                return "redirect:/error.htm";
//            }
//
//            CreateCategoryDetailDto categoryDetailDto = new CreateCategoryDetailDto();
//            categoryDetailDto.setBlogId(blogServiceResponse.getData().get().getId());
//            categoryDetailDto.setCategoryId(category.get().getId());
//            ServiceResponse<CategoryDetail> categoryDetailServiceResponse = this.categoryDetailService
//                    .create(categoryDetailDto);
//            if (categoryDetailServiceResponse.isError()) {
//                modelMap.addAttribute("errorResponse", categoryDetailServiceResponse.buildError());
//                return "redirect:/error.htm";
//            }
//        }

        return "redirect:/admin/edit/" + requestedBlog.get().getSlug() + ".htm";
    }

    @GetMapping("/remove/{slug}.htm")
    public String adminBlogRemoveHandler(
            @PathVariable("slug") String slug,
            ModelMap modelMap,
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

        List<Category> blogCategories = requestedBlog.get().getCategories();
        for (Category category: blogCategories) {
            Optional<CategoryDetail> categoryDetail = categoryDetailService.findByBlogIdAndCategoryId(
                    requestedBlog.get().getId(), category.getId());
            if (!categoryDetail.isPresent()) {
                modelMap.addAttribute("errorResponse", ErrorResponse.buildBadRequest(
                        "Category Detail Not Found",
                        "Cannot Find Any Category Detail"));
                return "redirect:/error.htm";
            }

            categoryDetailService.remove(categoryDetail.get().getId());
        }

        ServiceResponse<Blog> blogServiceResponse = this.blogService.remove(requestedBlog.get().getId());
        if (blogServiceResponse.isError()) {
            modelMap.addAttribute("errorResponse", blogServiceResponse.buildError());
            return "redirect:/error.htm";
        }

        return "redirect:/admin/index.htm";
    }
}
