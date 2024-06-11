package com.example.javaee.controller;

import com.example.javaee.dto.*;
import com.example.javaee.helper.ErrorResponse;
import com.example.javaee.helper.ServiceErrorType;
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

        if (selectingCategorySlugs == null || selectingCategorySlugs.isEmpty()) {
            List<Category> currentBlogCategories = requestedBlog.get().getCategories();
            for (Category currentBlogCategory: currentBlogCategories) {
                Optional<CategoryDetail> removingCategoryDetail = this.categoryDetailService
                        .findByBlogIdAndCategoryId(requestedBlog.get().getId(), currentBlogCategory.getId());
                if (!removingCategoryDetail.isPresent()) {
                    redirectAttributes.addFlashAttribute(
                            "errorResponse",
                            ErrorResponse.buildBadRequest(
                                    "Invalid Category Detail",
                                    "Cannot Find Any Category Detail With The Given Blog Id And Category Id"));
                    return "redirect:/error.htm";
                }

                ServiceResponse<CategoryDetail> removingCategoryDetailServiceResponse = this.categoryDetailService
                        .remove(removingCategoryDetail.get().getId());
                if (removingCategoryDetailServiceResponse.isError()) {
                    redirectAttributes.addFlashAttribute(
                            "errorResponse",
                            removingCategoryDetailServiceResponse.buildError());
                    return "redirect:/error.htm";
                }
            }

            redirectAttributes.addFlashAttribute("alertMessage", "Blog updated");
            return "redirect:/admin/edit/" + blogServiceResponse.getData().get().getSlug() + ".htm";
        }

        List<Category> currentBlogCategories = requestedBlog.get().getCategories();
        Set<String> updatedBlogCategories = new HashSet<>();

        // * Removing old categories
        for (Category currentBlogCategory: currentBlogCategories) {
            if (selectingCategorySlugs
                    .stream()
                    .noneMatch(categorySlug -> categorySlug.equals(currentBlogCategory.getSlug()))) {
                Optional<CategoryDetail> removingCategoryDetail = this.categoryDetailService
                        .findByBlogIdAndCategoryId(requestedBlog.get().getId(), currentBlogCategory.getId());
                if (!removingCategoryDetail.isPresent()) {
                    redirectAttributes.addFlashAttribute(
                            "errorResponse",
                            ErrorResponse.buildBadRequest(
                                    "Invalid Category Detail",
                                    "Cannot Find Any Category Detail With The Given Blog Id And Category Id"));
                    return "redirect:/error.htm";
                }

                ServiceResponse<CategoryDetail> categoryDetailServiceResponse =
                        this.categoryDetailService.remove(removingCategoryDetail.get().getId());
                if (categoryDetailServiceResponse.isError()) {
                    redirectAttributes.addFlashAttribute(
                            "errorResponse",
                            categoryDetailServiceResponse.buildError());
                    return "redirect:/error.htm";
                }
            }
            else {
                updatedBlogCategories.add(currentBlogCategory.getSlug());
            }
        }

        // * Adding new category detail
        for (String selectingCategorySlug: selectingCategorySlugs) {
            Optional<Category> selectingCategory = this.categoryService.findBySlug(selectingCategorySlug);
            if (!selectingCategory.isPresent()) {
                redirectAttributes.addFlashAttribute(
                        "errorResponse",
                        ErrorResponse.buildBadRequest(
                                "Invalid Category Slug",
                                "Cannot Find Any Category With The Given Slug = " + selectingCategorySlug));
                return "redirect:/error.htm";
            }

            if (updatedBlogCategories
                    .stream()
                    .noneMatch(existedCategorySlug -> existedCategorySlug.equals(selectingCategorySlug))) {
                CreateCategoryDetailDto createCategoryDetailDto = new CreateCategoryDetailDto();
                createCategoryDetailDto.setCategoryId(selectingCategory.get().getId());
                createCategoryDetailDto.setBlogId(requestedBlog.get().getId());
                ServiceResponse<CategoryDetail> categoryDetailServiceResponse =
                        this.categoryDetailService.create(createCategoryDetailDto);
                if (categoryDetailServiceResponse.isError()) {
                    redirectAttributes.addFlashAttribute(
                            "errorResponse",
                            categoryDetailServiceResponse.buildError());
                    return "redirect:/error.htm";
                }

                updatedBlogCategories.add(selectingCategorySlug);
            }
        }

        redirectAttributes.addFlashAttribute("alertMessage", "Blog updated");
        return "redirect:/admin/edit/" + requestedBlog.get().getSlug() + ".htm";
    }

    @GetMapping("/remove/{slug}.htm")
    public String adminBlogRemoveHandler(
            @PathVariable("slug") String slug,
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
            redirectAttributes.addFlashAttribute("errorResponse", ErrorResponse.buildUnknownServerError(
                    "Cannot Found User's Claim",
                    "Cannot Find User's Claim Due To Unknown Server Error"));
            return "redirect:/error.htm";
        }
        modelMap.addAttribute("adminInformation", response.getData().get());

        Optional<Blog> requestedBlog = this.blogService.findBySlug(slug);
        if (!requestedBlog.isPresent()) {
            redirectAttributes.addFlashAttribute("errorResponse", ErrorResponse.buildBadRequest(
                    "Invalid Blog Slug",
                    "Cannot Find Any Blog With The Given Slug"));
            return "redirect:/error.htm";
        }

        List<Category> blogCategories = requestedBlog.get().getCategories();
        System.out.println("blog categories:" + blogCategories.size());
        for (Category category: blogCategories) {
            Optional<CategoryDetail> categoryDetail = categoryDetailService.findByBlogIdAndCategoryId(
                    requestedBlog.get().getId(), category.getId());
            if (!categoryDetail.isPresent()) {
                redirectAttributes.addFlashAttribute("errorResponse", ErrorResponse.buildBadRequest(
                        "Category Detail Not Found",
                        "Cannot Find Any Category Detail"));
                return "redirect:/error.htm";
            }

            ServiceResponse<CategoryDetail> removingCategoryDetailServiceResponse = categoryDetailService
                    .remove(categoryDetail.get().getId());
            if (removingCategoryDetailServiceResponse.isError()) {
                redirectAttributes.addFlashAttribute("errorResponse",
                        removingCategoryDetailServiceResponse.buildError());
                return "redirect:/error.htm";
            }
        }

        ServiceResponse<Blog> blogServiceResponse = this.blogService.remove(requestedBlog.get().getId());
        if (blogServiceResponse.isError()) {
            redirectAttributes.addFlashAttribute("errorResponse", blogServiceResponse.buildError());
            return "redirect:/error.htm";
        }

        return "redirect:/admin/index.htm";
    }
}
