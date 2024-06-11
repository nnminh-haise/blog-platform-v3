package com.example.javaee.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCategoryDto {
    @NotNull(message = "Category's Name Cannot Be Null")
    @NotEmpty(message = "Category's Name Cannot Be Empty")
    @NotBlank(message = "Category's Name Cannot Be Blank")
    private String name;
}
