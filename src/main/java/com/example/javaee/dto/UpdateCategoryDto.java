package com.example.javaee.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
//@NoArgsConstructor
public class UpdateCategoryDto extends CreateCategoryDto {
    private String name;
}
