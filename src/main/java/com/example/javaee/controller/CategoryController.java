//package com.example.javaee.controller;
//
//
//import com.example.javaee.dto.CreateCategoryDto;
//import com.example.javaee.dto.ResponseDto;
//import com.example.javaee.dto.UpdateCategoryDto;
//import com.example.javaee.helper.ServiceResponse;
//import com.example.javaee.model.Category;
//import com.example.javaee.service.CategoryService;
//import org.springframework.http.HttpStatus;
//
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.*;
//
//import org.springframework.ui.ModelMap;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.UUID;
//
//@Controller
//@RequestMapping("/category")
//public class CategoryController {
//
//    int checkFist = 0;
//    public int pageSizes = 5;
//
//    @ModelAttribute("totalPages")
//    public Integer getLimitPageCategory( ) {
//        List<Category> categories = categoryService.findAll();
//        if (categories.size() != 0) {
//            return 1;
//        }
//        int total = categories.size();
//        int limit = total / pageSizes;
//        if (total % pageSizes != 0) {
//            limit++;
//        }
//        return limit;
//    }
//
//    private final CategoryService categoryService;
//
//    public CategoryController(CategoryService categoryService) {
//        this.categoryService = categoryService;
//    }
//
//    @GetMapping("/index.htm")
//    public String list(ModelMap model, @RequestParam(value = "pageCategory", required = false) Integer pageCategory){
//        System.out.println("pageCategory: " + pageCategory);
//        if (pageCategory == null) {
//            pageCategory = 1;
//
//        }
//        model.addAttribute("pageCategory", pageCategory);
//        ServiceResponse<List<Category>> response = categoryService.pagination(pageCategory, pageSizes);
//        if (!response.getStatus().equals(HttpStatus.OK.value())) {
//            return "category/index";
//        }
//        model.addAttribute("categories", response.getData());
//        return "category/index";
//    }
//
//
//
//
//    @RequestMapping(value = "/insert.htm", method = RequestMethod.GET)
//    public String add(ModelMap model) {
//        model.addAttribute("category", new Category());
//        return "category/insert";
//    }
//
//
//    @RequestMapping(value = "/insert.htm", method = RequestMethod.POST)
//    public String add(ModelMap model, @ModelAttribute("category") Category category) {
//        ServiceResponse<Category> check = categoryService.findByName(category.getName());
//        if (check.getStatus().equals(HttpStatus.OK.value())) {
//            model.addAttribute("message", "Name is exists");
//            return "category/insert";
//        }
//        ServiceResponse<Category> response = categoryService.create(new CreateCategoryDto(category.getName()));
//
//        if (!response.getStatus().equals(HttpStatus.OK.value())) {
//            return "category/insert";
//        }
//        return "redirect:/category/index.htm";
//    }
//    @ModelAttribute("categories")
//    public List<Category> fetchAllCategories() {
//          List<Category> categories = new ArrayList<>();
//        ServiceResponse<List<Category>> response = categoryService.findAll();
//            if (!response.getStatus().equals(HttpStatus.OK.value())) {
//                return categories;
//            }
//            return response.getData();
//    }
//    @RequestMapping(value = "/editor/{id}.htm",method = RequestMethod.GET)
//    public String routeToEditor(ModelMap model, @PathVariable("id") UUID id,@RequestParam("pageCategory") Integer pageCategory) {
//        model.addAttribute("id_editor", id);
//        if(pageCategory == null){
//            pageCategory = 1;
//        }
//        Category category = categoryService.findById(id).getData();
//        model.addAttribute("createCategoryDto",category);
//        model.addAttribute("pageCategory", pageCategory);
//        ServiceResponse<List<Category>> response = categoryService.pagination(pageCategory, pageSizes);
//        System.out.println("id of the category: " + category.getName());
//        model.addAttribute("categories", response.getData());
//        return "category/edit";
//    }
//    @RequestMapping(value = "/editor/{id}.htm", method = RequestMethod.POST)
//    public String edit(ModelMap model, @ModelAttribute("createCategoryDto") Category category, @PathVariable("id") UUID id,@RequestParam("pageCategory") Integer pageCategory){
//        if (category.getName().isEmpty()) {
//            model.addAttribute("message", "Name is required");
//            return "category/edit";
//        }
//        ServiceResponse<Category> check = categoryService.findByName(category.getName());
//        if (check.getStatus().equals(HttpStatus.OK.value())) {
//            model.addAttribute("message", "Name is exists");
//            return "category/edit";
//        }
//        UpdateCategoryDto updateCategoryDto = new UpdateCategoryDto(category.getName());
//        ServiceResponse<Category> response = categoryService.update(category.getId(), updateCategoryDto);
//
//        if (!response.getStatus().equals(HttpStatus.OK.value())) {
//            return "category/edit";
//        }
//        model.addAttribute("pageCategory", pageCategory);
//        ServiceResponse<List<Category>> res= categoryService.pagination(pageCategory, pageSizes);
//        System.out.println("id of the category: " + category.getName());
//        model.addAttribute("categories", res.getData());
//
//        return "redirect:/category/index.htm?pageCategory=" + pageCategory;
//    }
//    @PostMapping("/saver.htm")
//    public String saverCategory(
//            @ModelAttribute("createCategoryDto") CreateCategoryDto createCategoryDto,
//            ModelMap model) {
//
//        if (createCategoryDto.getName().isEmpty()) {
//            model.addAttribute("message", "Name is required");
//            return "category/edit";
//        }
//        ServiceResponse<Category> check = categoryService.findByName(createCategoryDto.getName());
//        if (check.getStatus().equals(HttpStatus.OK.value())) {
//            model.addAttribute("message", "Name is exists");
//            return "category/edit";
//        }
//
//
//        ServiceResponse<Category> response = categoryService.create(createCategoryDto);
//        if (!response.getStatus().equals(HttpStatus.OK.value())) {
//            return "category/edit";
//        }
//
//        System.out.println("name of the category: " + createCategoryDto.getName());
//        return "category/edit";
//    }
//    @RequestMapping(value = "/delete/{id}.htm", method = RequestMethod.GET)
//    public String delete(ModelMap model, @PathVariable("id") UUID id,@RequestParam("pageCategory") Integer pageCategory) {
//        ServiceResponse<Category> response = categoryService.remove(id);
//        if (!response.getStatus().equals(HttpStatus.OK.value())) {
//            return "category/index";
//        }
//        model.addAttribute("pageCategory", pageCategory);
//        return "redirect:/category/index.htm?pageCategory=" + pageCategory;
//    }
//
//
//}
