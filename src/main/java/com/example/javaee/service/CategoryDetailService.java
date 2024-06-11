package com.example.javaee.service;

import com.example.javaee.dto.CreateCategoryDetailDto;
import com.example.javaee.helper.*;
import com.example.javaee.model.Blog;
import com.example.javaee.model.Category;
import com.example.javaee.model.CategoryDetail;
import com.example.javaee.repository.BlogRepository;
import com.example.javaee.repository.CategoryDetailRepository;
import com.example.javaee.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CategoryDetailService {
    private final CategoryDetailRepository categoryDetailRepository;
    private final CategoryRepository categoryRepository;
    private final BlogRepository blogRepository;

    public CategoryDetailService(
            CategoryDetailRepository categoryDetailRepository,
            CategoryRepository categoryRepository,
            BlogRepository blogRepository) {
        this.categoryDetailRepository = categoryDetailRepository;
        this.categoryRepository = categoryRepository;
        this.blogRepository = blogRepository;
    }

    public ServiceResponse<CategoryDetail> create(CreateCategoryDetailDto createCategoryDetailDto) {
        Optional<Category> targetingCategory = categoryRepository.findById(
                createCategoryDetailDto.getCategoryId());
        if (!targetingCategory.isPresent()) {
            return ServiceResponse.ofNotFound(
                    "Category not found", "Cannot find any category with the given ID");
        }

        Optional<Blog> targetingBlog = blogRepository.findById(
                createCategoryDetailDto.getBlogId());
        if (!targetingBlog.isPresent()) {
            return ServiceResponse.ofNotFound(
                    "Blog not found", "Cannot find any blog with the given ID");
        }
        final LocalDateTime currentTimestamp = LocalDateTime.now();
        CategoryDetail newCategoryDetail = new CategoryDetail();
        newCategoryDetail.setCategory(targetingCategory.get());
        newCategoryDetail.setBlog(targetingBlog.get());
        newCategoryDetail.setCreateAt(currentTimestamp);
        newCategoryDetail.setUpdateAt(currentTimestamp);

        RepositoryResponse<CategoryDetail> response = this.categoryDetailRepository.create(newCategoryDetail);
        if (response.hasErrorOf(RepositoryErrorType.CONSTRAINT_VIOLATION)) {
            return ServiceResponse.ofBadRequest(
                    response.getMessage(), response.getDescription());
        }

        return ServiceResponse.ofSuccess(
                "Created new category detail!", null, newCategoryDetail);
    }

    public List<CategoryDetail> findAll() {
        return this.categoryDetailRepository.findAll();
    }

    public Optional<CategoryDetail> findById(UUID id) {
        if (id == null) {
            return Optional.empty();
        }

        return this.categoryDetailRepository.findById(id);
    }

    public Optional<CategoryDetail> findByBlogIdAndCategoryId(UUID blogId, UUID categoryId) {
        return this.categoryDetailRepository.findByBlogIdAndCategoryId(blogId, categoryId);
    }

    public ServiceResponse<CategoryDetail> remove(UUID id) {
        Optional<CategoryDetail> deletingCategoryDetail = this.categoryDetailRepository.findById(id);
        if (!deletingCategoryDetail.isPresent()) {
            return ServiceResponse.ofNotFound(
                    "Category detail not found", "Cannot find any category detail with the given ID");
        }

        RepositoryResponse<CategoryDetail> response = this.categoryDetailRepository.remove(deletingCategoryDetail.get());
        if (response.hasErrorOf(RepositoryErrorType.CONSTRAINT_VIOLATION)) {
            return ServiceResponse.ofUnknownServerError(response.getMessage(), response.getDescription());
        }
        return ServiceResponse.ofSuccess(
                "Category detail deleted successfully", response.getDescription(), deletingCategoryDetail.get());
    }
}
