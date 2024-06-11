package com.example.javaee.controller;

import com.example.javaee.dto.CreateCategoryDto;
import com.example.javaee.dto.OpenIdClaims;
import com.example.javaee.dto.UpdateCategoryDto;
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

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("admin/categories")
public class CategoryController {
    private final BlogService blogService;
    private final CategoryService categoryService;
    private final AdminService adminService;
    private final CategoryDetailService categoryDetailService;

    public CategoryController(
            BlogService blogService,
            CategoryService categoryService,
            AdminService adminService,
            CategoryDetailService categoryDetailService) {
        this.blogService = blogService;
        this.categoryService = categoryService;
        this.adminService = adminService;
        this.categoryDetailService = categoryDetailService;
    }

    @GetMapping("index.htm")
    public String categoryIndexViewRenderer(
            @RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
            @RequestParam(value = "size", defaultValue = "5", required = false) Integer size,
            @RequestParam(value = "orderBy", defaultValue = "asc", required = false) String orderBy,
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

        // * Finding categories base on the pagination values
        List<Category> categories = this.categoryService.findAll(page, size, orderBy);
        modelMap.addAttribute("categories", categories);

        // * Parsing the values to the view for handling pagination when fetching new page
        modelMap.addAttribute("currentPage", page);
        modelMap.addAttribute("currentSize", size);
        modelMap.addAttribute("totalNumberOfPage", this.categoryService.countMaximumNumberOfPage(size));
        modelMap.addAttribute("orderBy", orderBy);

        return "category/index";
    }

    @GetMapping("insert.htm")
    public String categoryInsertViewRenderer(
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

        modelMap.addAttribute("createCategoryDto", new CreateCategoryDto());

        return "category/insert";
    }

    @PostMapping("insert.htm")
    public String categoryInsertHandler(
            @ModelAttribute("createCategoryDto") CreateCategoryDto createCategoryDto,
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

        ServiceResponse<Category> creatingCategoryServiceResponse = this.categoryService.create(createCategoryDto);
        if (creatingCategoryServiceResponse.isError()) {
            redirectAttributes.addFlashAttribute("errorResponse", creatingCategoryServiceResponse.buildError());
            return "redirect:/error.htm";
        }

        redirectAttributes.addFlashAttribute("alertMessage", "Category Created");
        return "redirect:/admin/categories/index.htm";
    }

    @GetMapping("/edit/{slug}.htm")
    public String adminEditCategoryViewRenderer(
            @PathVariable("slug") String slug,
            @RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
            @RequestParam(value = "size", defaultValue = "5", required = false) Integer size,
            @RequestParam(value = "orderBy", defaultValue = "asc", required = false) String orderBy,
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

        Optional<Category> requestedCategory = this.categoryService.findBySlug(slug);
        if (!requestedCategory.isPresent()) {
            redirectAttributes.addFlashAttribute("errorResponse", ErrorResponse.buildBadRequest(
                    "Invalid Category Slug",
                    "Cannot Find Any Category With The Given Slug"));
            return "redirect:/error.htm";
        }

        modelMap.addAttribute("selectingCategory", requestedCategory.get());
        modelMap.addAttribute("updateCategoryDto", new UpdateCategoryDto(requestedCategory.get().getName()));

        List<Blog> relatedBlogs = requestedCategory.get().getRelatedBlogs(page, size);
        modelMap.addAttribute("relatedBlogs", relatedBlogs);

        // * Parsing the values to the view for handling pagination when fetching new page
        modelMap.addAttribute("currentPage", page);
        modelMap.addAttribute("currentSize", size);
        Long numberOfItems = requestedCategory.get().getNumberOfBlog();
        Long numberOfPage = numberOfItems / size + ((numberOfItems % size == 0) ? 0 : 1);
        modelMap.addAttribute("totalNumberOfPage", numberOfPage);
        modelMap.addAttribute("orderBy", orderBy);

        return "category/edit";
    }

    @PostMapping("/edit/{slug}.htm")
    public String adminEditCategoryHandler(
            @PathVariable("slug") String slug,
            @RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
            @RequestParam(value = "size", defaultValue = "5", required = false) Integer size,
            @RequestParam(value = "orderBy", defaultValue = "asc", required = false) String orderBy,
            @ModelAttribute("updateCategoryDto") UpdateCategoryDto updateCategoryDto,
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

        Optional<Category> updatingCategory = this.categoryService.findBySlug(slug);
        if (!updatingCategory.isPresent()) {
            redirectAttributes.addFlashAttribute("errorResponse", ErrorResponse.buildBadRequest(
                    "Cannot Found Category",
                    "Cannot Find Any Category With The Given Slug"));
            return "redirect:/error.htm";
        }

        ServiceResponse<Category> updatedCategoryResponse = this.categoryService.update(
                updatingCategory.get().getId(), updateCategoryDto);
        if (updatedCategoryResponse.isError()) {
            redirectAttributes.addFlashAttribute("errorResponse", updatedCategoryResponse.buildError());
            return "redirect:/error.htm";
        }

        redirectAttributes.addFlashAttribute("alertMessage", "Category Updated");
        return "redirect:/admin/categories/edit/" + updatedCategoryResponse.getData().get().getSlug() + ".htm";
    }

    @GetMapping("/delete.htm")
    public String adminRemoveCategoryDetailHandler(
            @RequestParam(value = "blogSlug") String blogSlug,
            @RequestParam(value = "categorySlug") String categorySlug,
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

        Optional<Blog> requestingBlog = this.blogService.findBySlug(blogSlug);
        if (!requestingBlog.isPresent()) {
            redirectAttributes.addFlashAttribute("errorResponse", ErrorResponse.buildBadRequest(
                    "Blog Not Found",
                    "Cannot Find Any Blog With The Given Slug"));
            return "redirect:/error.htm";
        }

        Optional<Category> requestingCategory = this.categoryService.findBySlug(categorySlug);
        if (!requestingCategory.isPresent()) {
            redirectAttributes.addFlashAttribute("errorResponse", ErrorResponse.buildBadRequest(
                    "Category Not Found",
                    "Cannot Find Any Category With The Given Slug"));
            return "redirect:/error.htm";
        }

        Optional<CategoryDetail> removingDetail = this.categoryDetailService.findByBlogIdAndCategoryId(
                requestingBlog.get().getId(), requestingCategory.get().getId());
        if (!removingDetail.isPresent()) {
            redirectAttributes.addFlashAttribute("errorResponse", ErrorResponse.buildBadRequest(
                    "Category Detail Not Found",
                    "Cannot Find Any Category Detail With The Given Slugs"));
            return "redirect:/error.htm";
        }

        ServiceResponse<CategoryDetail> removedDetailResponse = this.categoryDetailService.remove(removingDetail.get().getId());
        if (removedDetailResponse.isError()) {
            redirectAttributes.addFlashAttribute("errorResponse", removedDetailResponse.buildError());
            return "redirect:/error.htm";
        }

        return "redirect:/admin/categories/edit/" + requestingCategory.get().getSlug() + ".htm";
    }

    @GetMapping("/remove/{slug}.htm")
    public String adminCategoryRemoveCategoryHandler(
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

        Optional<Category> removingCategory = this.categoryService.findBySlug(slug);
        if (!removingCategory.isPresent()) {
            redirectAttributes.addFlashAttribute("errorResponse", ErrorResponse.buildBadRequest(
                    "Category Not Found",
                    "Cannot Find Any Category With The Given Slug"));
            return "redirect:/error.htm";
        }

        ServiceResponse<Category> serviceResponse = this.categoryService.delete(removingCategory.get().getId());
        if (serviceResponse.isError()) {
            redirectAttributes.addFlashAttribute("errorResponse", serviceResponse.buildError());
            return "redirect:/error.htm";
        }

        return "redirect:/admin/categories/index.htm";
    }
}
