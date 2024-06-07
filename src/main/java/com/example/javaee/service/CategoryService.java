package com.example.javaee.service;

import com.example.javaee.dto.CreateCategoryDto;
import com.example.javaee.dto.ErrorResponse;
import com.example.javaee.dto.ResponseDto;
import com.example.javaee.dto.UpdateCategoryDto;
import com.example.javaee.exceptions.ResourceNotFoundException;
import com.example.javaee.helper.RepositoryErrorType;
import com.example.javaee.helper.RepositoryResponse;
import com.example.javaee.helper.ServiceResponse;
import com.example.javaee.model.Blog;
import com.example.javaee.model.Category;
import com.example.javaee.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    private String getSlug(String title) {
        return title
                .toLowerCase()
                .trim()
                .replace(" ", "-");
    }

    public ResponseDto<Category> create(CreateCategoryDto dto) {
        LocalDateTime currentTimestamp = LocalDateTime.now();
        Category newCategory = new Category();
        newCategory.setName(dto.getName());
        newCategory.setCreateAt(currentTimestamp);
        newCategory.setUpdateAt(currentTimestamp);
        newCategory.setSlug(getSlug(dto.getName()));

        ErrorResponse errorResponse = this.categoryRepository.save(newCategory);

        return errorResponse.ifHasErrorOrElse(
                () -> new ResponseDto<>(
                        errorResponse.getStatus(),
                        errorResponse.getMessage()),
                // * otherwise
                () -> new ResponseDto<>(
                        HttpStatus.OK.value(),
                        "Success", newCategory));
    }


    public ResponseDto<List<Category>> findAll() {
        try {
            List<Category> categories = this.categoryRepository.findAll();
            return new ResponseDto<>(
                    HttpStatus.OK.value(),
                    "Success",
                    categories);
        }
        catch (ResourceNotFoundException exception) {
            return new ResponseDto<>(
                    HttpStatus.NOT_FOUND.value(),
                    exception.getMessage());
        }
    }

    public ResponseDto<Category> findById(UUID id) {
        if (id == null) {
            return new ResponseDto<>(
                    HttpStatus.BAD_REQUEST.value(),
                    "Invalid ID");
        }

        Optional<Category> category = this.categoryRepository.findById(id);
        return category.map(value -> new ResponseDto<>(
                HttpStatus.OK.value(),
                "Success",
                value)).orElseGet(() -> new ResponseDto<>(
                HttpStatus.NOT_FOUND.value(),
                "Cannot find any blog with the given ID"));
    }

    public ServiceResponse<Category> update(UUID id, UpdateCategoryDto dto) {
        Optional<Category> targetingCategory = this.categoryRepository.findById(id);
        if (!targetingCategory.isPresent()) {
            return new ResponseDto<>(
                    HttpStatus.NOT_FOUND.value(),
                    "Cannot find any category with the given ID");
        }

        Category newCategory = targetingCategory.get();

        RepositoryResponse<Blog> response = this.blogRepository.update(updatedBlog);
        if (response.hasErrorOf(RepositoryErrorType.CONSTRAINT_VIOLATION)) {
            return ServiceResponse.ofBadRequest(
                    response.getMessage(), response.getDescription());
        }
        return ServiceResponse.ofSuccess(
                "Blog updated successfully", null, updatedBlog);

        ErrorResponse errorResponse = this.categoryRepository.update(newCategory);
        return errorResponse.ifHasErrorOrElse(
                () -> new ResponseDto<>(
                        errorResponse.getStatus(),
                        errorResponse.getMessage()),
                // * otherwise
                () -> new ResponseDto<>(
                        HttpStatus.OK.value(),
                        "Success", newCategory));
    }
    public Optional<Category> findBySlug(String slug) {
        if (slug == null) {
            return Optional.empty();
        }

        return this.categoryRepository.findBySlug(slug);
    }
    public ResponseDto<Category> remove(UUID id) {
        ErrorResponse errorResponse = this.categoryRepository.remove(id);
        return errorResponse.ifHasErrorOrElse(
                () -> new ResponseDto<>(
                        errorResponse.getStatus(),
                        errorResponse.getMessage()),
                // * otherwise
                () -> new ResponseDto<>(
                        HttpStatus.OK.value(),
                        "Success"));
    }
    //find by name
    public ResponseDto<Category> findByName(String name) {
        if (name == null) {
            return new ResponseDto<>(
                    HttpStatus.BAD_REQUEST.value(),
                    "Invalid Name");
        }

        Optional<Category> category = this.categoryRepository.findByName(name);
        return category.map(value -> new ResponseDto<>(
                HttpStatus.OK.value(),
                "Success",
                value)).orElseGet(() -> new ResponseDto<>(
                HttpStatus.NOT_FOUND.value(),
                "Cannot find any blog with the given Name"));
    }
    // use pagination to get pageCategory use function pagination
    public ResponseDto<List<Category>> pagination(int page, int limit) {
        try {
            List<Category> categories = this.categoryRepository.pagination(page, limit);
            return new ResponseDto<>(
                    HttpStatus.OK.value(),
                    "Success",
                    categories);
        }
        catch (ResourceNotFoundException exception) {
            return new ResponseDto<>(
                    HttpStatus.NOT_FOUND.value(),
                    exception.getMessage());
        }
    }

}