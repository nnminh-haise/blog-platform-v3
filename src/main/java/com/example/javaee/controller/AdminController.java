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
            CategoryService categoryService, CategoryDetailService categoryDetailService) {
        this.adminService = adminService;
        this.blogService = blogService;
        this.categoryService = categoryService;
        this.categoryDetailService = categoryDetailService;
    }

    @ModelAttribute("categories")
    public List<Category> fetchAllCategories() {
        return this.categoryService.findAll();
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

        List<Blog> blogs = this.blogService.findAllBlogByCategorySlug(page, size, orderBy, slug);
        modelMap.addAttribute("blogList", blogs);

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

        return "admin/insert";
    }

    @PostMapping("/insert.htm")
    public String creatingNewBlogHandler(
            @RequestParam(value = "cates", required = false) String[] cates, // TODO: refactor the name of this param
            @ModelAttribute("createBlogDto") CreateBlogDto createBlogDto,
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

        ServiceResponse<Blog> serviceResponse = this.blogService.create(createBlogDto);
        if (serviceResponse.isError() || !serviceResponse.getData().isPresent()) {
            modelMap.addAttribute("errorResponse", serviceResponse.buildError());
            return "redirect:/error.htm";
        }

        // TODO: Refactor this code logic
        List<String> selectedCategories = (cates != null) ? Arrays.asList(cates) : null;
        for (String cate : selectedCategories) {
            CreateCategoryDetailDto categoryDetail = new CreateCategoryDetailDto();
            categoryDetail.setBlogId(serviceResponse.getData().get().getId());
            categoryDetail.setCategoryId(categoryService.findBySlug(cate).get().getId());

            ServiceResponse<CategoryDetail> response1 = this.categoryDetailService.create(categoryDetail);
            if (response1.isSuccess()) {
                System.out.println("category detail created");
            } else {
                System.out.println("category detail not created");
            }
        }

        return "redirect:/admin/edit/" + serviceResponse.getData().get().getSlug() + ".htm";
    }

    @GetMapping("/edit/{slug}.htm")
    public String adminEditBlogViewRenderer(
            @PathVariable(name = "slug", required = true) String slug,
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
        UpdateBlogDto updateBlogDto = new UpdateBlogDto();
        updateBlogDto.setTitle(blog.getTitle());
        updateBlogDto.setDescription(blog.getDescription());
        updateBlogDto.setIsPopular(blog.getIsPopular());
        updateBlogDto.setSubTitle(blog.getSubTitle());

        modelMap.addAttribute("updateBlogDto", updateBlogDto);
        modelMap.addAttribute("slug", slug);
        modelMap.addAttribute("blogAttachment", blog.getAttachment());

        // * Get all categories of the blog.
        // TODO: fix this logic
        List<CategoryDetail> categoryDetails = this.categoryDetailService.findByBlogId(blog.getId());

        List<String> categorySlugs = new ArrayList<>();
        for (CategoryDetail categoryDetail : categoryDetails) {
            Optional<Category> category = this.categoryService.findById(categoryDetail.getCategory().getId());
            if (category.isPresent()) {
                categorySlugs.add(category.get().getSlug());
            }
        }
        modelMap.addAttribute("categorySlugs", categorySlugs);
        return "admin/edit";
    }

    @PostMapping(value = "/edit/{slug}.htm")
    public String updateBlogHandler(ModelMap modelMap,
            @ModelAttribute("updateBlogDto") UpdateBlogDto updateBlogDto,
            @RequestParam(value = "cates", required = false) String[] cates,
            @PathVariable(name = "slug", required = true) String slug,

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

        Optional<Blog> updatingBlog = this.blogService.findBySlug(slug);
        if (!updatingBlog.isPresent()) {
            modelMap.addAttribute("errorResponse", ErrorResponse.buildBadRequest(
                    "Invalid Blog Slug",
                    "Cannot Find Any Blog With The Given Slug"));
            return "redirect:/error.htm";
        }

        UpdateBlogDto payload = new UpdateBlogDto();
        payload.setTitle(updateBlogDto.getTitle());
        payload.setDescription(updateBlogDto.getDescription());
        payload.setAttachment(updateBlogDto.getAttachment());
        // TODO: add update fields for these two fields
        payload.setIsPopular(updateBlogDto.getIsPopular());
        payload.setSubTitle(updateBlogDto.getSubTitle());

        List<CategoryDetail> categoryDetails = this.categoryDetailService.findByBlogId(updatingBlog.get().getId());
        for (CategoryDetail categoryDetail : categoryDetails) {
            ServiceResponse<CategoryDetail> categoryDetailServiceResponse = this.categoryDetailService.remove(categoryDetail.getId());
        }

        // TODO: add update blog service
        this.blogService.update(updatingBlog.get().getId(), payload);
        List<String> selectedCategories = (cates != null) ? Arrays.asList(cates) : null;
        for (String cate : selectedCategories) {
            CreateCategoryDetailDto categoryDetail = new CreateCategoryDetailDto();
            categoryDetail.setBlogId(updatingBlog.get().getId());
            categoryDetail.setCategoryId(categoryService.findBySlug(cate).get().getId());

            ServiceResponse<CategoryDetail> response1 = this.categoryDetailService.create(categoryDetail);
            if (response1.isSuccess()) {
                System.out.println("category detail created");
            } else {
                System.out.println("category detail not created");
            }
        }

        return "redirect:/admin/index.htm";
    }
}
