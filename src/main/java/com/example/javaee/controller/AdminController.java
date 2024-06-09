package com.example.javaee.controller;

import com.example.javaee.dto.*;
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
    @ModelAttribute("slugs")
    public Map<String, String> fetchAllSlugs() {
        List<Category> categories = this.categoryService.findAll();
        Map<String, String> slugs = new HashMap<>();
        for (Category category: categories) {
            slugs.put(category.getSlug(), category.getName());
        }
        return slugs;
    }
    @ModelAttribute("categories")
    public List<Category> fetchAllCategories() {
        return this.categoryService.findAll();
    }

    @GetMapping("/index.htm")
    public String adminIndexViewRenderer(
            @RequestParam(name = "page", defaultValue = "0", required = false) Integer page,
            @RequestParam(name = "size", defaultValue = "5", required = false) Integer size,
            @RequestParam(name = "orderBy", defaultValue = "asc") String orderBy,
            @RequestParam(name = "slug", required = false) String slug,
            HttpServletRequest request,
            ModelMap modelMap) {
        Optional<OpenIdClaims> claims = this.adminService.validateRequest(request);
        if (!claims.isPresent()) {
            System.out.println(
                    "[Admin Controller] (Admin Index Route) ? Bad request - Cannot fetch access token claims -> Redirect back to landing page");
            return "redirect:/index.htm";
        }
        modelMap.addAttribute("adminInformation", claims.get());

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
        Optional<OpenIdClaims> claims = this.adminService.validateRequest(request);
        if (!claims.isPresent()) {
            System.out.println(
                    "[Admin Controller] (Blog Insert Route) ? Bad request - Cannot fetch access token claims -> Redirect back to landing page");
            return "redirect:/index.htm";
        }
//        System.out.println("1");
//        modelMap.addAttribute("adminInformation", claims.get());
//        System.out.println("2");
//
        modelMap.addAttribute("createBlogDto", new CreateBlogDto());

        return "admin/insert";
    }

    @PostMapping("/insert.htm")
    public String creatingNewBlogHandler(
            HttpServletRequest request,
            @RequestParam(value = "cates", required = false) String[] cates,
            ModelMap modelMap,

            @ModelAttribute("createBlogDto") CreateBlogDto createBlogDto ) {
        Optional<OpenIdClaims> claims = this.adminService.validateRequest(request);
        if (!claims.isPresent()) {
            System.out.println(
                    "[Admin Controller] (Blog Insert Route) ? Bad request - Cannot fetch access token claims -> Redirect back to landing page");
            return "redirect:/index.htm";
        }


        System.out.println("createBlogDto"+ createBlogDto);
        modelMap.addAttribute("adminInformation", claims.get());
        System.out.println("attachment: "+ createBlogDto.getAttachment().getOriginalFilename());
        ServiceResponse<Blog> response = this.blogService.create(createBlogDto);
        List<String> selectedCategories = (cates != null) ? Arrays.asList(cates) : null;
        for (String cate: selectedCategories) {
            CreateCategoryDetailDto categoryDetail = new CreateCategoryDetailDto();
            categoryDetail.setBlogId(response.getData().get().getId());
            categoryDetail.setCategoryId(categoryService.findBySlug(cate).get().getId());

            ServiceResponse<CategoryDetail> response1 = this.categoryDetailService.create(categoryDetail);
            if  (response1.isSuccess()) {
                System.out.println("category detail created");
            }
            else {
                System.out.println("category detail not created");
            }
        }

        if (response.isError() || !response.getData().isPresent()) {
            // TODO: handle error here!
            return "redirect:/index.htm";
        }
        return "redirect:/admin/index.htm";

//        return "redirect:/admin/edit/" + response.getData().get().getSlug() + ".htm";
    }

    @GetMapping("/edit/{slug}.htm")
    public String adminEditBlogViewRenderer(
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

        Optional<Blog> requestedBlog = this.blogService.findBySlug(slug);
        if (!requestedBlog.isPresent()) {
            // TODO: handle error here
            return "redirect:/admin/index.htm";
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
        //find all categories have category detail with blog id
        List<CategoryDetail> categoryDetails = this.categoryDetailService.findByBlogId(blog.getId());

        List<String> categorySlugs = new ArrayList<>();
        for (CategoryDetail categoryDetail: categoryDetails) {
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
        System.out.println("from update"+updateBlogDto);

        Optional<OpenIdClaims> claims = this.adminService.validateRequest(request);
        if (!claims.isPresent()) {
            System.out.println(
                    "[Admin Controller] (Blog Insert Route) ? Bad request - Cannot fetch access token claims -> Redirect back to landing page");
            return "redirect:/index.htm";
        }
        modelMap.addAttribute("adminInformation", claims.get());
        Optional<Blog> updatingBlog = this.blogService.findBySlug(slug);
        if (!updatingBlog.isPresent()) {
            // TODO: handle error here
            return "redirect:/admin/index.htm";
        }

        UpdateBlogDto payload = new UpdateBlogDto();
        payload.setTitle(updateBlogDto.getTitle());
        payload.setDescription(updateBlogDto.getDescription());
        payload.setAttachment(updateBlogDto.getAttachment());
        // TODO: add update fields for these two fields
        payload.setIsPopular(updateBlogDto.getIsPopular());
        payload.setSubTitle(updateBlogDto.getSubTitle());

        List<CategoryDetail> categoryDetails = this.categoryDetailService.findByBlogId(updatingBlog.get().getId());
        for (CategoryDetail categoryDetail: categoryDetails) {
            ServiceResponse<CategoryDetail> response = this.categoryDetailService.remove(categoryDetail.getId());
        }


        // TODO: add update blog service
        this.blogService.update(updatingBlog.get().getId(), payload);
        List<String> selectedCategories = (cates != null) ? Arrays.asList(cates) : null;
        for (String cate: selectedCategories) {
            CreateCategoryDetailDto categoryDetail = new CreateCategoryDetailDto();
            categoryDetail.setBlogId(updatingBlog.get().getId());
            categoryDetail.setCategoryId(categoryService.findBySlug(cate).get().getId());

            ServiceResponse<CategoryDetail> response1 = this.categoryDetailService.create(categoryDetail);
            if  (response1.isSuccess()) {
                System.out.println("category detail created");
            }
            else {
                System.out.println("category detail not created");
            }
        }

        return "redirect:/admin/index.htm";
    }
}
