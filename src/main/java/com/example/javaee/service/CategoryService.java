package com.example.javaee.service;

import com.example.javaee.dto.CreateCategoryDto;
import com.example.javaee.dto.UpdateCategoryDto;
import com.example.javaee.helper.RepositoryErrorType;
import com.example.javaee.helper.RepositoryResponse;
import com.example.javaee.helper.ServiceResponse;
import com.example.javaee.model.Category;
import com.example.javaee.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public List<Category> findAll() {
        return this.categoryRepository.findAll();
    }

    public Optional<Category> findById(UUID id) {
        return this.categoryRepository.findById(id);
    }

    public Optional<Category> findBySlug(String slug) {
        if (slug == null) {
            return Optional.empty();
        }

        return this.categoryRepository.findBySlug(slug);
    }

    public ServiceResponse<Category> create(CreateCategoryDto dto) {
        LocalDateTime currentTimestamp = LocalDateTime.now();
        Category newCategory = new Category();
        newCategory.setName(dto.getName());
        newCategory.setCreateAt(currentTimestamp);
        newCategory.setUpdateAt(currentTimestamp);
        newCategory.setSlug(getSlug(dto.getName()));

        RepositoryResponse<Category> response = this.categoryRepository.create(newCategory);
        if (response.hasErrorOf(RepositoryErrorType.CONSTRAINT_VIOLATION)) {
            return ServiceResponse.ofBadRequest(
                    response.getMessage(), response.getDescription());
        }
        return ServiceResponse.ofSuccess(
                "Created new category!", null, newCategory);
    }

    public ServiceResponse<Category> update(UUID id, UpdateCategoryDto dto) {
        Optional<Category> updatingCategory = this.categoryRepository.findById(id);
        if (!updatingCategory.isPresent()) {
            return ServiceResponse.ofNotFound(
                    "Category not found", "Cannot find any category with the given ID");
        }

        LocalDateTime currentTimestamp = LocalDateTime.now();

        Category updatedCategory = updatingCategory.get();
        updatedCategory.setName(dto.getName());
        updatedCategory.setSlug(getSlug(dto.getName()));
        updatedCategory.setUpdateAt(currentTimestamp);

        RepositoryResponse<Category> response = this.categoryRepository.update(updatedCategory);
        if (response.hasErrorOf(RepositoryErrorType.CONSTRAINT_VIOLATION)) {
            return ServiceResponse.ofBadRequest(
                    response.getMessage(), response.getDescription());
        }
        return ServiceResponse.ofSuccess(
                "Updated category!", null, updatedCategory);
    }

    public ServiceResponse<Category> delete(UUID id) {
        Optional<Category> deletingCategory = this.categoryRepository.findById(id);
        if (!deletingCategory.isPresent()) {
            return ServiceResponse.ofNotFound(
                    "Category not found", "Cannot find any category with the given ID");
        }

        LocalDateTime currentTimestamp = LocalDateTime.now();

        Category deletedCategory = deletingCategory.get();
        deletedCategory.setDeleteAt(currentTimestamp);

        RepositoryResponse<Category> response = this.categoryRepository.update(deletedCategory);
        if (response.hasErrorOf(RepositoryErrorType.CONSTRAINT_VIOLATION)) {
            return ServiceResponse.ofBadRequest(
                    response.getMessage(), response.getDescription());
        }
        return ServiceResponse.ofSuccess(
                "Updated category!", null, deletedCategory);
    }
}