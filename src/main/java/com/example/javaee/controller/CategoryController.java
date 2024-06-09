package com.example.javaee.controller;

import com.example.javaee.dto.CreateCategoryDto;
import com.example.javaee.dto.OpenIdClaims;
import com.example.javaee.dto.UpdateCategoryDto;
import com.example.javaee.helper.ErrorResponse;
import com.example.javaee.helper.ServiceResponse;
import com.example.javaee.model.Category;
import com.example.javaee.service.AdminService;
import com.example.javaee.service.CategoryService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("admin/categories")
public class CategoryController {
    private final CategoryService categoryService;
    private final AdminService adminService;

    public CategoryController(
            CategoryService categoryService,
            AdminService adminService) {
        this.categoryService = categoryService;
        this.adminService = adminService;
    }

    @GetMapping("index.htm")
    public String categoryIndexViewRenderer(
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

        List<Category> categories = categoryService.findAll();
        modelMap.addAttribute("categories", categories);

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

        ServiceResponse<Category> serviceResponse = this.categoryService.create(createCategoryDto);
        if (!serviceResponse.isSuccess()) {
            modelMap.addAttribute("errorResponse", serviceResponse.buildError());
            return "redirect:/error.htm";
        }

        return "redirect:/admin/categories/index.htm";
    }

    @GetMapping("/edit/{slug}.htm")
    public String adminEditCategoryViewRenderer(
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

        Optional<Category> requestedCategory = this.categoryService.findBySlug(slug);
        if (!requestedCategory.isPresent()) {
            modelMap.addAttribute("errorResponse", ErrorResponse.buildBadRequest(
                    "Invalid Category Slug",
                    "Cannot Find Any Category With The Given Slug"));
            return "redirect:/error.htm";
        }

        List<Category> categories = this.categoryService.findAll();
        modelMap.addAttribute("categories", categories);

        UpdateCategoryDto updatingCategoryDto = new UpdateCategoryDto();
        updatingCategoryDto.setName(requestedCategory.get().getName());
        modelMap.addAttribute("updateCategoryDto", updatingCategoryDto);

        return "category/edit";
    }
}
