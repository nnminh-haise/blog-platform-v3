package com.example.javaee.service;

import com.example.javaee.dto.CreateCategoryDto;
import com.example.javaee.dto.UpdateCategoryDto;
import com.example.javaee.helper.RepositoryErrorType;
import com.example.javaee.helper.RepositoryResponse;
import com.example.javaee.helper.ServiceResponse;
import com.example.javaee.model.Category;
import com.example.javaee.repository.CategoryRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> findAll() {
        return this.categoryRepository.findAll();
    }

    public List<Category> findAll(Integer page, Integer size, String orderBy) {
        return this.categoryRepository.findAll(page, size, orderBy);
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

    public Long countMaximumNumberOfPage(Integer size) {
        Long totalNumberOfCategory = this.categoryRepository.countNumberOfCategory();
        return totalNumberOfCategory / size + (totalNumberOfCategory % size == 0 ? 0 : 1);
    }

    public ServiceResponse<Category> create(@Valid CreateCategoryDto payload) {
        if (payload.getName() == null || payload.getName().isEmpty()) {
            return ServiceResponse.ofBadRequest(
                    "Invalid CreateCategoryDto",
                    "Category's Name Cannot Be Null Or Empty");
        }

        LocalDateTime currentTimestamp = LocalDateTime.now();
        Category newCategory = new Category();
        newCategory.setName(payload.getName());
        newCategory.setSlug(getSlug(payload.getName()));
        newCategory.setCreateAt(currentTimestamp);
        newCategory.setUpdateAt(currentTimestamp);

        RepositoryResponse<Category> response = this.categoryRepository.create(newCategory);
        if (response.hasErrorOf(RepositoryErrorType.CONSTRAINT_VIOLATION)) {
            return ServiceResponse.ofBadRequest(
                    response.getMessage(), response.getDescription());
        }
        return ServiceResponse.ofSuccess(
                "Created new category!", null, newCategory);
    }

    public ServiceResponse<Category> update(UUID id, @Valid UpdateCategoryDto dto) {
        Optional<Category> updatingCategory = this.categoryRepository.findById(id);
        if (!updatingCategory.isPresent()) {
            return ServiceResponse.ofNotFound(
                    "Category not found", "Cannot find any category with the given ID");
        }

        if (dto.getName() == null || dto.getName().isEmpty()) {
            return ServiceResponse.ofBadRequest(
                    "Invalid UpdateCategoryDto",
                    "Category Name Cannot Be Null Or Empty");
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

    private String getSlug(String title) {
        return title
                .toLowerCase()
                .trim()
                .replace(" ", "-");
    }
}