package com.example.javaee.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateBlogDto {
    private String title;
    private String subtitle;
    private Boolean is_popular;

    private String description;

    private MultipartFile attachment;
}
