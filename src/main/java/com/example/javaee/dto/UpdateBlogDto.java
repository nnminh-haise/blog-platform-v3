package com.example.javaee.dto;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class UpdateBlogDto extends CreateBlogDto {
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate publishAt;

    private Boolean hiddenStatus;
}
