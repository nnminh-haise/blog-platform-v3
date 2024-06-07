package com.example.javaee.controller;

import com.example.javaee.dto.BlogDto;
import com.example.javaee.dto.CreateBlogDto;
import com.example.javaee.dto.OpenIdClaims;
import com.example.javaee.helper.ServiceResponse;
import com.example.javaee.model.Blog;
import com.example.javaee.model.Category;
import com.example.javaee.model.CategoryDetail;
import com.example.javaee.service.BlogService;
import com.example.javaee.service.CategoryService;
import com.example.javaee.service.FileUploadService;
import com.example.javaee.service.GoogleApiService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.http.client.ClientProtocolException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final GoogleApiService googleApiService;
    private final BlogService blogService;
    private final CategoryService categoryService;

    public AdminController(
            GoogleApiService googleApiService,
            BlogService blogService,
            CategoryService categoryService) {
        this.googleApiService = googleApiService;
        this.blogService = blogService;
        this.categoryService = categoryService;
    }

    @ModelAttribute("categories")
    public List<Category> fetchAllCategories() {
        return this.categoryService.findAll().getData();
    }

    @ModelAttribute("totalPages")
    public Integer getLimitBlogPage() {
        final int PAGE_SIZE = 5;

        List<Blog> blogs = this.blogService.findAll();
        int totalBlogs = blogs.size();
        int totalPages = totalBlogs / PAGE_SIZE;
        if (totalBlogs % PAGE_SIZE != 0) {
            totalPages++;
        }
        return totalPages;
    }

    @GetMapping("/index.htm")
    public String adminIndexRouter(
            @RequestParam(name = "page", defaultValue = "0", required = false) Integer page,
            @RequestParam(name = "size", defaultValue = "5", required = false) Integer size,
            @RequestParam(name = "orderBy", defaultValue = "asc") String orderBy,
            @RequestParam(name = "slug", required = false) String slug,
            HttpServletRequest request,
            ModelMap modelMap) {
        Optional<OpenIdClaims> claims = this.validateRequest(request);
        if (!claims.isPresent()) {
            System.out.println(
                    "[Admin Controller] (Admin Index Route) ? Bad request - Cannot fetch access token claims -> Redirect back to landing page");
            return "redirect:/index.htm";
        }
        modelMap.addAttribute("adminInformation", claims.get());

        List<Blog> blogs = this.blogService.findAllBlogByCategorySlug(page, size, orderBy, null);
        modelMap.addAttribute("blogList", blogs);

        return "admin/index";
    }

    @GetMapping("/insert.htm")
    public String createNewBlogViewResolver(
            HttpServletRequest request,
            ModelMap modelMap) {
        Optional<OpenIdClaims> claims = this.validateRequest(request);
        if (!claims.isPresent()) {
            System.out.println(
                    "[Admin Controller] (Blog Insert Route) ? Bad request - Cannot fetch access token claims -> Redirect back to landing page");
            return "redirect:/index.htm";
        }
        modelMap.addAttribute("adminInformation", claims.get());

        modelMap.addAttribute("createBlogDto", new CreateBlogDto());

        return "admin/insert";
    }

    @PostMapping("/insert.htm")
    public String creatingNewBlogHandler(
//            @RequestParam(name = "title", required = true) String title,
//            @RequestParam(name = "description", required = true) String description,
//            @RequestParam(name = "attachment", required = false) MultipartFile file,
            @ModelAttribute("createBlogDto") CreateBlogDto createBlogDto,
            HttpServletRequest request,
            ModelMap modelMap) {
        Optional<OpenIdClaims> claims = this.validateRequest(request);
        if (!claims.isPresent()) {
            System.out.println(
                    "[Admin Controller] (Blog Insert Route) ? Bad request - Cannot fetch access token claims -> Redirect back to landing page");
            return "redirect:/index.htm";
        }
        modelMap.addAttribute("adminInformation", claims.get());

//        System.out.println("dto:" + createBlogDto.toString());
        ServiceResponse<Blog> response = this.blogService.create(createBlogDto);
        if (response.isError() || !response.getData().isPresent()) {
            // TODO: handle error here!
            return "redirect:/index.htm";
        }

        return "redirect:/admin/edit/" + response.getData().get().getSlug() + ".htm";
    }

    @GetMapping("/edit/{slug}.htm")
    public String viewBlog(
            @PathVariable(name = "slug", required = true) String slug,
            ModelMap modelMap) {
        Optional<Blog> blog = this.blogService.findBySlug(slug);
        if (!blog.isPresent()) {
            modelMap.addAttribute("message", "There is some error!");
            return "admin/index";
        }
        modelMap.addAttribute("updateBlogDto", blog.get());

        modelMap.addAttribute("slug", slug);
        List<Category> categories = new ArrayList<>();
        for (CategoryDetail detail : blog.get().getCategoryDetails()) {
            categories.add(detail.getCategory());
        }
        modelMap.addAttribute("blogCategoryList", categories);

        List<Blog> first5BlogsOfSameCategories = this.blogService.findFirstAmountInCategories(
                5, categories, blog.get().getId());
        modelMap.addAttribute("nextBlogs", first5BlogsOfSameCategories);

        modelMap.addAttribute("blogDto", new BlogDto(blog.get().getTitle(), blog.get().getDescription(), null));

        return "admin/edit";
    }

    @PostMapping(value = "/edit/{slug}.htm")
    public String updateBlogHandler(ModelMap modelMap,
            // @RequestParam(name = "title", required = true) String title,
            // @RequestParam(name = "description", required = true) String description,
            // @RequestParam(name = "attachment", required = false) MultipartFile
            // attachment,
            @ModelAttribute("BlogDto") BlogDto blogDto,
            @PathVariable(name = "slug", required = true) String slug) {
        System.out.println("update");
        System.out.println("slug:" + slug);
        // System.out.println("title:" + title);
        // System.out.println("attachment:" + attachment.getOriginalFilename());
        // System.out.println("description:" + description);
        // modelMap.addAttribute("title", title);
        // modelMap.addAttribute("attachment", attachment);
        // modelMap.addAttribute("description", description);
        System.out.println("dto:" + blogDto.toString());

        modelMap.addAttribute("blogDto", blogDto);
        return "admin/edit";
    }

    // @ModelAttribute("blogDto")
    // public BlogDto modelForBlogDto() {
    // return new BlogDto();
    // }

    @ModelAttribute("createBlogDto")
    public CreateBlogDto modelAttributeForBlogEdit() {
        return new CreateBlogDto();
    }

    @ModelAttribute("slugs")
    public Map<String, String> fetchAllSlugs() {
        List<Category> categories = this.categoryService.findAll().getData();
        Map<String, String> slugs = new java.util.HashMap<>();
        for (Category category : categories) {
            slugs.put(category.getSlug(), category.getName());
        }
        return slugs;
    }

    @ModelAttribute("createBlogDto")
    public CreateBlogDto generatePlainDto() {
        return new CreateBlogDto();
    }

    private Optional<OpenIdClaims> validateRequest(HttpServletRequest request) {
        if (request == null) {
            return Optional.empty();
        }

        HttpSession session = request.getSession();
        String accessToken = String.valueOf(session.getAttribute("accessToken"));
        String expireIn = String.valueOf(session.getAttribute("expireIn"));
        if (accessToken == null || expireIn == null) {
            return Optional.empty();
        }

        try {
            OpenIdClaims claims = this.googleApiService.getUserInfo(accessToken);
            return Optional.of(claims);
        } catch (ClientProtocolException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
