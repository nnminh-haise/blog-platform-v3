package com.example.javaee.dto;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateBlogDto {
    private String title;

    private String description;

    private MultipartFile attachment;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date publishAt;

    private Boolean hiddenStatus;
}
