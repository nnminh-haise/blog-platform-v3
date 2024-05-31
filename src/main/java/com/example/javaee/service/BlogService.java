package com.example.javaee.service;

import com.example.javaee.dto.CreateBlogDto;
import com.example.javaee.dto.UpdateBlogDto;
import com.example.javaee.helper.RepositoryErrorType;
import com.example.javaee.helper.RepositoryResponse;
import com.example.javaee.helper.ServiceResponse;
import com.example.javaee.model.Blog;
import com.example.javaee.model.Category;
import com.example.javaee.repository.BlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class BlogService {
    @Autowired
    private BlogRepository blogRepository;

    public List<Blog> findAll() {
        return this.blogRepository.findAll();
    }

    public List<Blog> findAllBlogByCategorySlug(int page, int size, String orderBy, String categorySlug) {
        return this.blogRepository.findAllByCategorySlug(page, size, orderBy, categorySlug);
    }

    public List<Blog> findFirst(Integer amount) {
        return this.blogRepository.findFirst(amount);
    }

    public List<Blog> findFirstOfCategories(Integer amount, List<Category> categories, UUID blogId) {
        return this.blogRepository.findFirstOfCategories(amount, categories, blogId);
    }

    public Optional<Blog> findById(UUID id) {
        if (id == null) {
            return Optional.empty();
        }

        return this.blogRepository.findById(id);
    }

    public Optional<Blog> findBySlug(String slug) {
        if (slug == null) {
            return Optional.empty();
        }

        return this.blogRepository.findBySlug(slug);
    }

    public List<Blog> findPopular(Integer amount) {
        return this.blogRepository.findPopularBlogs(amount);
    }

    public ServiceResponse<Blog> create(CreateBlogDto payload) {
        Blog newBlog = new Blog();
        final LocalDateTime timestamp = LocalDateTime.now();
        newBlog.setTitle(payload.getTitle());
        newBlog.setDescription(payload.getDescription());
        newBlog.setAttachment(payload.getAttachment());
        newBlog.setCreateAt(timestamp);
        newBlog.setUpdateAt(timestamp);
        newBlog.setSlug(getSlug(payload.getTitle()));

        RepositoryResponse<Blog> response = this.blogRepository.create(newBlog);
        if (response.getError().equals(RepositoryErrorType.CONSTRAINT_VIOLATION)) {
            return ServiceResponse.ofBadRequest(
                    response.getMessage(), response.getDescription());
        }

        return ServiceResponse.ofSuccess(
                "Created new category detail!", null, newBlog);
    }

    public ServiceResponse<Blog> update(UUID id, UpdateBlogDto payload) {
        Optional<Blog> targetingBlog = this.blogRepository.findById(id);
        if (!targetingBlog.isPresent()) {
            return ServiceResponse.ofNotFound(
                    "Blog not found", "Cannot find any blog with the given ID");
        }

        Blog buffer = targetingBlog.get();
        buffer.setTitle(payload.getTitle());
        buffer.setDescription(payload.getDescription());
        buffer.setAttachment(payload.getAttachment());
        buffer.setSlug(getSlug(buffer.getTitle()));
        buffer.setPublishAt(payload.getPublishAt());
        buffer.setHiddenStatus(payload.getHiddenStatus());

        RepositoryResponse<Blog> response = this.blogRepository.update(buffer);
        if (response.getError().equals(RepositoryErrorType.CONSTRAINT_VIOLATION)) {
            return ServiceResponse.ofBadRequest(
                    response.getMessage(), response.getDescription());
        }
        return ServiceResponse.ofSuccess("Blog updated successfully", null, buffer);
    }

    public ServiceResponse<Blog> remove(UUID id) {
        Optional<Blog> targetingBlog = this.blogRepository.findById(id);
        if (!targetingBlog.isPresent()) {
            return ServiceResponse.ofNotFound(
                    "Blog not found", "Cannot find any blog with the given ID");
        }

        Blog buffer = targetingBlog.get();
        buffer.setDeleteAt(LocalDateTime.now());
        RepositoryResponse<Blog> response = this.blogRepository.update(buffer);
        if (response.getError().equals(RepositoryErrorType.CONSTRAINT_VIOLATION)) {
            return ServiceResponse.ofBadRequest(
                    response.getMessage(), response.getDescription());
        }
        return ServiceResponse.ofSuccess("Blog deleted successfully", null, buffer);
    }

    private String getSlug(String title) {
        return title
                .toLowerCase()
                .trim()
                .replace(" ", "-");
    }
}
