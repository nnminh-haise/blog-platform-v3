package com.example.javaee.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateBlogDto {
    @NotNull(message = "Blog's Title Cannot Be Null")
    @NotEmpty(message = "Blog's Title Cannot Be Empty")
    @NotBlank(message = "Blog's Title Cannot Be Blank")
    private String title;

    @NotNull(message = "Blog's Subtitle Cannot Be Null")
    @NotEmpty(message = "Blog's Subtitle Cannot Be Empty")
    @NotBlank(message = "Blog's Subtitle Cannot Be Blank")
    private String subtitle;

    private String description;

    private Boolean isPopular;

    private MultipartFile attachment;
}
