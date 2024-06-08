package com.example.javaee.controller;

import com.example.javaee.dto.OpenIdClaims;
import com.example.javaee.model.Category;
import com.example.javaee.service.AdminService;
import com.example.javaee.service.CategoryService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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

        modelMap.addAttribute("category", new Category());

        return "category/insert";
    }

    @PostMapping("insert.htm")
    public String categoryInsertHandler(
            HttpServletRequest request,
            ModelMap modelMap) {
        Optional<OpenIdClaims> claims = this.adminService.validateRequest(request);
        if (!claims.isPresent()) {
            System.out.println(
                    "[Admin Controller] (Admin Index Route) ? Bad request - Cannot fetch access token claims -> Redirect back to landing page");
            return "redirect:/index.htm";
        }
        modelMap.addAttribute("adminInformation", claims.get());

        modelMap.addAttribute("category", new Category());

        return "category/insert";
    }
}
