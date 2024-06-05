package com.example.javaee.controller;

import com.example.javaee.beans.FileUploadDirectory;
import com.example.javaee.dto.CreateBlogDto;
import com.example.javaee.model.Blog;
import com.example.javaee.service.BlogService;
import com.example.javaee.service.FileUploadService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("file-handler")
public class FileUploadController {
    private final FileUploadDirectory fileUploadDirectory;
    private final FileUploadService fileUploadService;
    private final BlogService blogService;

    public FileUploadController(
            BlogService blogService,
            FileUploadService fileUploadService,
            FileUploadDirectory fileUploadDirectory) {
        this.blogService = blogService;
        this.fileUploadService = fileUploadService;
        this.fileUploadDirectory = fileUploadDirectory;
    }

    @ModelAttribute("directories")
    public Map<String, String> getFileUploadDirectories() {
        return this.fileUploadDirectory.getDirectories();
    }

    @ModelAttribute("blogs")
    public List<Blog> getAllBlogs() {
        return this.blogService.findAll();
    }

    @ModelAttribute("createBlogDto")
    public CreateBlogDto generatePlainDto() {
        return new CreateBlogDto();
    }

    @GetMapping("/file-upload-test.htm")
    public String testRoute(ModelMap modelMap) {
        modelMap.addAttribute("baseDirectory", fileUploadDirectory.getBaseDirectory());
        return "blog/editor";
//        return "test/testUploadForm";
    }

    @PostMapping("/file-saving-resolver.htm")
    public String submit(
//            @RequestParam("blog-title") String blogTitle,
            @ModelAttribute("createBlogDto") CreateBlogDto dto,
            @RequestParam(value = "file", required = false) MultipartFile[] files,
            @RequestParam(value = "file-directory", required = false) String[] directories,
            ModelMap modelMap) throws IOException {
        Map<Integer, String> results = this.fileUploadService.saveFiles(files, directories);
        System.out.println("dto:" + dto.toString());
        modelMap.addAttribute("fileCreatingResults", results);
        return "test/testFormLoading";
    }

}
