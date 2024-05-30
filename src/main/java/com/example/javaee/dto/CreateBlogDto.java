package com.example.javaee.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateBlogDto {
    @NotNull
    private String title;

    @NotNull
    private String description;

    @NotNull
    private String attachment;
}
