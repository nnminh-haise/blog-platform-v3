package com.example.javaee.service;

import com.example.javaee.dto.CreateCategoryDto;
import com.example.javaee.dto.UpdateCategoryDto;
import com.example.javaee.exceptions.ResourceNotFoundException;
import com.example.javaee.helper.RepositoryErrorType;
import com.example.javaee.helper.RepositoryResponse;
import com.example.javaee.helper.ServiceResponse;
import com.example.javaee.model.Category;
import com.example.javaee.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public static String getSlug(String input) {
        if (input == null) {
            return null;
        }

        // Convert to lower case
        String slug = input.toLowerCase();

        // Remove accents and diacritics
        slug = Normalizer.normalize(slug, Normalizer.Form.NFD);
        slug = slug.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

        // Replace spaces and non-word characters with a dash
        slug = slug.replaceAll("[^\\w\\s]", ""); // Loại bỏ các ký tự không phải là chữ, số hoặc khoảng trắng
        slug = slug.replaceAll("\\s+", "-"); // Thay thế tất cả khoảng trắng bằng dấu gạch nối

        // Trim dashes from the beginning and end
        slug = slug.replaceAll("^-|-$", "");

        return slug;
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
    public List<Category> paginate(int page, int size) {
        return this.categoryRepository.paginate(page, size);
    }

}