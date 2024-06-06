package com.example.javaee.dto;

import java.time.LocalDate;

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

    private String attachment;

//    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate publishAt;

    private Boolean hiddenStatus;
}
