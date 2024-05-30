package com.example.javaee.service;

import com.example.javaee.dto.CreateCategoryDto;
import com.example.javaee.helper.RepositoryErrorType;
import com.example.javaee.helper.RepositoryResponse;
import com.example.javaee.helper.ServiceResponse;
import com.example.javaee.model.Category;
import com.example.javaee.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    public ServiceResponse<Category> create(CreateCategoryDto payload) {
        Category newCategory = new Category();
        newCategory.setName(payload.getName());
        newCategory.setCreateAt(LocalDateTime.now());
        newCategory.setUpdateAt(LocalDateTime.now());
        newCategory.setSlug(getSlug(payload.getName()));

        RepositoryResponse<Category> response = this.categoryRepository.create(newCategory);
        if (response.getError().equals(RepositoryErrorType.CONSTRAINT_VIOLATION)) {
            return ServiceResponse.ofBadRequest(
                    response.getMessage(), response.getDescription());
        }

        return ServiceResponse.ofSuccess(
                "Created new category!", null, newCategory);
    }

    public List<Category> findAll() {
        return this.categoryRepository.findAll();
    }

    public Optional<Category> findById(UUID id) {
        if (id == null) {
            return Optional.empty();
        }

        return this.categoryRepository.findById(id);
    }

    public ServiceResponse<Category> remove(UUID id) {
        Optional<Category> deletingCategory = this.categoryRepository.findById(id);
        if (!deletingCategory.isPresent()) {
            return ServiceResponse.ofNotFound(
                    "Category not found", "Cannot find any category with the given ID");
        }

        Category buffer = deletingCategory.get();
        buffer.setDeleteAt(LocalDateTime.now());
        RepositoryResponse<Category> response = this.categoryRepository.update(buffer);
        if (response.getError().equals(RepositoryErrorType.CONSTRAINT_VIOLATION)) {
            return ServiceResponse.ofUnknownServerError(response.getMessage(), response.getDescription());
        }
        return ServiceResponse.ofSuccess(
                "Category detail deleted successfully", response.getDescription(), buffer);
    }
}
