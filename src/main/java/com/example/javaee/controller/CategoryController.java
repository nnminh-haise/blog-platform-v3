package com.example.javaee.controller;

import com.example.javaee.dto.CreateCategoryDto;
import com.example.javaee.dto.OpenIdClaims;
import com.example.javaee.dto.UpdateCategoryDto;
import com.example.javaee.helper.ServiceResponse;
import com.example.javaee.model.Blog;
import com.example.javaee.model.Category;
import com.example.javaee.service.AdminService;
import com.example.javaee.service.CategoryService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
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
        Optional<OpenIdClaims> claims = this.adminService.validateRequest(request);
        if (!claims.isPresent()) {
            System.out.println(
                    "[Admin Controller] (Admin Index Route) ? Bad request - Cannot fetch access token claims -> Redirect back to landing page");
            return "redirect:/index.htm";
        }
        modelMap.addAttribute("adminInformation", claims.get());

        List<Category> categories = categoryService.findAll();
        modelMap.addAttribute("categories", categories);

        return "category/index";
    }

    @GetMapping("insert.htm")
    public String categoryInsertViewRenderer(
            HttpServletRequest request,
            ModelMap modelMap) {
        Optional<OpenIdClaims> claims = this.adminService.validateRequest(request);
        if (!claims.isPresent()) {
            System.out.println(
                    "[Admin Controller] (Admin Index Route) ? Bad request - Cannot fetch access token claims -> Redirect back to landing page");
            return "redirect:/index.htm";
        }
        modelMap.addAttribute("adminInformation", claims.get());

        modelMap.addAttribute("createCategoryDto", new CreateCategoryDto());

        return "category/insert";
    }

    @PostMapping("insert.htm")
    public String categoryInsertHandler(
            @ModelAttribute("createCategoryDto") CreateCategoryDto createCategoryDto,
            HttpServletRequest request,
            ModelMap modelMap) {
        Optional<OpenIdClaims> claims = this.adminService.validateRequest(request);
        if (!claims.isPresent()) {
            System.out.println(
                    "[Admin Controller] (Admin Index Route) ? Bad request - Cannot fetch access token claims -> Redirect back to landing page");
            return "redirect:/index.htm";
        }
        modelMap.addAttribute("adminInformation", claims.get());

        ServiceResponse<Category> response = this.categoryService.create(createCategoryDto);
        if (!response.isSuccess()) {
            // TODO: add error handling here
            return "redirect://index.hm";
        }

        return "redirect:/admin/categories/index.htm";
    }

    @GetMapping("/edit/{slug}.htm")
    public String adminEditCategoryViewRenderer(
            @PathVariable(name = "slug", required = true) String slug,
            HttpServletRequest request,
            ModelMap modelMap) {
        Optional<OpenIdClaims> claims = this.adminService.validateRequest(request);
        if (!claims.isPresent()) {
            System.out.println(
                    "[Admin Controller] (Blog Insert Route) ? Bad request - Cannot fetch access token claims -> Redirect back to landing page");
            return "redirect:/index.htm";
        }
        modelMap.addAttribute("adminInformation", claims.get());

        Optional<Category> requestedCategory = this.categoryService.findBySlug(slug);
        System.out.println("category:" + requestedCategory.get().toString());
        if (!requestedCategory.isPresent()) {
            // TODO: handle error here
            System.out.println("Error: Cannot find any category with the given slug");
            return "redirect:/admin/index.htm";
        }

        List<Category> categories = this.categoryService.findAll();
        modelMap.addAttribute("categories", categories);

        UpdateCategoryDto updatingCategoryDto = new UpdateCategoryDto();
        updatingCategoryDto.setName(requestedCategory.get().getName());
        modelMap.addAttribute("updateCategoryDto", updatingCategoryDto);

        return "category/edit";
    }
}
