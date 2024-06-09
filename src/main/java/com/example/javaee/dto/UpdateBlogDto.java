package com.example.javaee.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateBlogDto {

    private String title;
    private String subTitle;


    private MultipartFile attachment;

    private Boolean isPopular;
    private String description;
}
