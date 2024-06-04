package com.example.javaee.controller;

import com.example.javaee.beans.FileUploadDirectory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Controller
public class TestFileUpload {
    private final FileUploadDirectory fileUploadDirectory;

    public TestFileUpload(FileUploadDirectory fileUploadDirectory) {
        this.fileUploadDirectory = fileUploadDirectory;
    }

    @GetMapping("/test-upload-file.htm")
    public String testRoute(ModelMap modelMap) {
        modelMap.addAttribute("baseDirectory", fileUploadDirectory.getBaseDirectory());
        return "test/testUploadForm";
    }

    @ModelAttribute("directories")
    public Map<String, String> getFileUploadDirectories() {
        return this.fileUploadDirectory.getDirectories();
    }

    @PostMapping("/uploadFile.htm")
    public String submit(
            @RequestParam("file") MultipartFile file, ModelMap modelMap) throws IOException {
//        File savedFile = this.fileUploadDirectory
//                .beginFromBase(file.getOriginalFilename())
//                .addDirectory("doc")
//                .toFile();
//        System.out.println(this.fileUploadDirectory.getFullPath());
//        System.out.println("file:" + savedFile.getAbsolutePath());
//        file.transferTo(savedFile);
//        modelMap.addAttribute("file", file);
//        modelMap.addAttribute("fileDirectory", this.fileUploadDirectory.getFullPath());
        return "test/testFormLoading";
    }

}
