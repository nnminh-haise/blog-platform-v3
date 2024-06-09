package com.example.javaee.controller;

import com.example.javaee.dto.CreateCategoryDto;
import com.example.javaee.dto.OpenIdClaims;
import com.example.javaee.dto.UpdateCategoryDto;
import com.example.javaee.helper.ServiceResponse;
import com.example.javaee.model.Category;
import com.example.javaee.model.CategoryDetail;
import com.example.javaee.service.AdminService;
import com.example.javaee.service.CategoryDetailService;
import com.example.javaee.service.CategoryService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("admin/categories")
public class CategoryController {
    private final CategoryService categoryService;
    private final AdminService adminService;
    public int pageSizes = 5;
    private final CategoryDetailService categoryDetailService;
    @ModelAttribute("totalPages")
    public Integer getLimitPageCategory( ) {
        List<Category> response = categoryService.findAll();
        if (response == null) {
            return 1;
        }
        int total = response.size();
        int limit = total / pageSizes;
        if (total % pageSizes != 0) {
            limit++;
        }
        return limit;
    }
    public CategoryController(
            CategoryService categoryService,
            AdminService adminService, CategoryDetailService categoryDetailService) {
        this.categoryService = categoryService;
        this.adminService = adminService;
        this.categoryDetailService = categoryDetailService;
    }

    @GetMapping("index.htm")
    public String categoryIndexViewRenderer(
            HttpServletRequest request,
            ModelMap modelMap,
            @RequestParam(name = "pageCategory", defaultValue ="1") Integer pageCategory)
    {
        Optional<OpenIdClaims> claims = this.adminService.validateRequest(request);
        if (!claims.isPresent()) {
            System.out.println(
                    "[Admin Controller] (Admin Index Route) ? Bad request - Cannot fetch access token claims -> Redirect back to landing page");
            return "redirect:/index.htm";
        }

        modelMap.addAttribute("adminInformation", claims.get());

        System.out.println("pageCategory: " + pageCategory);

        modelMap.addAttribute("pageCategory", pageCategory);
        List<Category> response = categoryService.paginate(pageCategory, pageSizes);
        modelMap.addAttribute("categories", response);

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
            ModelMap modelMap,
            RedirectAttributes redirectAttributes) {
        Optional<OpenIdClaims> claims = this.adminService.validateRequest(request);
        if (!claims.isPresent()) {
            System.out.println(
                    "[Admin Controller] (Admin Index Route) ? Bad request - Cannot fetch access token claims -> Redirect back to landing page");
            return "redirect:/index.htm";
        }
        modelMap.addAttribute("adminInformation", claims.get());

        if (createCategoryDto.getName().isEmpty()) {
            modelMap.addAttribute("message", "Name is required");
            return "category/insert";
        }
        if(categoryService.findBySlug(categoryService.getSlug(createCategoryDto.getName())).isPresent()){
            modelMap.addAttribute("message", "Name is exists");
            return "category/insert";
        }
        ServiceResponse<Category> response = this.categoryService.create(createCategoryDto);
        if (!response.isSuccess()) {
            // TODO: add error handling here
            return "redirect://index.hm";
        }
        redirectAttributes.addFlashAttribute("message", "Create category successfully!");
        return "redirect:/admin/categories/index.htm";
    }

    @GetMapping("/editor/{id}.htm")
    public String adminEditCategoryViewRenderer(
            HttpServletRequest request,
            ModelMap model,
             @PathVariable("id") UUID id, @RequestParam( name="pageCategory",defaultValue = "1") Integer pageCategory) {
        Optional<OpenIdClaims> claims = this.adminService.validateRequest(request);
        if (!claims.isPresent()) {
            System.out.println(
                    "[Admin Controller] (Blog Insert Route) ? Bad request - Cannot fetch access token claims -> Redirect back to landing page");
            return "redirect:/index.htm";
        }
        model.addAttribute("adminInformation", claims.get());


        model.addAttribute("id_editor", id);
        Optional<Category> optionalCategory = categoryService.findById(id);
        Category category = optionalCategory.get();
        model.addAttribute("createCategoryDto",category);
        model.addAttribute("pageCategory", pageCategory);

        List<Category> response = categoryService.paginate(pageCategory, pageSizes);

        System.out.println("id of the category: " + category.getName());
        model.addAttribute("categories", response);

        return "category/edit";
    }
    @PostMapping("/editor/{id}.htm")
    public String edit(ModelMap model, @ModelAttribute("createCategoryDto") Category category, @PathVariable("id") UUID id, @RequestParam(name= "pageCategory",defaultValue = "1") Integer pageCategory
    , RedirectAttributes redirectAttributes){
        model.addAttribute("createCategoryDto", category);
        model.addAttribute("pageCategory", pageCategory);
        model.addAttribute("categories", categoryService.paginate(pageCategory, pageSizes));
        if (category.getName().isEmpty()) {
            model.addAttribute("message", "Name is required");
            return "category/edit";
        }

        String slug = categoryService.getSlug(category.getName());
        Optional<Category> optionalCategory = categoryService.findBySlug(slug);

        if (optionalCategory.isPresent() && !optionalCategory.get().getId().equals(id)) {
            model.addAttribute("message", "Name is exists");
            return "category/edit";
        }

        UpdateCategoryDto updateCategoryDto = new UpdateCategoryDto(category.getName());
        ServiceResponse<Category> response = categoryService.update(id, updateCategoryDto);

        if (!response.isSuccess()) {
            model.addAttribute("message", "Update failed");

            return "category/edit";
        }
        model.addAttribute("pageCategory", pageCategory);
        List<Category> res= categoryService.paginate(pageCategory, pageSizes);
        System.out.println("id of the category: " + category.getName());
        model.addAttribute("categories", res);
        redirectAttributes.addFlashAttribute("message", "Form submitted successfully!");
        return "redirect:/admin/categories/index.htm?pageCategory=" + pageCategory;
    }
    @RequestMapping(value = "/delete/{id}.htm", method = RequestMethod.GET)
    public String delete(ModelMap model, @PathVariable("id") UUID id,@RequestParam(name = "pageCategory", defaultValue = "1") Integer pageCategory,
                         RedirectAttributes redirectAttributes) {
        model.addAttribute("pageCategory", pageCategory);
        model.addAttribute("categories", categoryService.paginate(pageCategory, pageSizes));
        List<CategoryDetail> res = categoryDetailService.findByCategoryId(id);
        if (res.size() > 0) {
            model.addAttribute("message", "Category is exists in blog. Please remove blog before delete category");
            return "category/index";
        }
        ServiceResponse<Category> response = categoryService.delete(id);
        if (!response.isSuccess()) {
            model.addAttribute("message", "Delete failed");
            return "category/index";
        }
        redirectAttributes.addFlashAttribute("message", "Delete successfully!");
        return "redirect:/admin/categories/index.htm?pageCategory=" + pageCategory;
    }
}
