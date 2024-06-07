package com.example.javaee.service;

import com.example.javaee.beans.FileUploadDirectory;
import com.example.javaee.dto.CreateBlogDto;
import com.example.javaee.dto.UpdateBlogDto;
import com.example.javaee.helper.RepositoryErrorType;
import com.example.javaee.helper.RepositoryResponse;
import com.example.javaee.helper.ServiceResponse;
import com.example.javaee.model.Blog;
import com.example.javaee.model.Category;
import com.example.javaee.repository.BlogRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
public class BlogService {
    private final FileUploadDirectory fileUploadDirectory;
    private final BlogRepository blogRepository;
    private final FileUploadService fileUploadService;

    public BlogService(
            FileUploadDirectory fileUploadDirectory,
            BlogRepository blogRepository,
            FileUploadService fileUploadService) {
        this.fileUploadDirectory = fileUploadDirectory;
        this.blogRepository = blogRepository;
        this.fileUploadService = fileUploadService;
    }

    public List<Blog> findAll() {
        return this.blogRepository.findAll();
    }

    public Blog findOnePopular() {
        return this.blogRepository.findOnePopular();
    }

    public List<Blog> findAllBlogByCategorySlug(int page, int size, String orderBy, String categorySlug) {
        return this.blogRepository.findAllByCategorySlug(page, size, orderBy, categorySlug);
    }

    public List<Blog> findFirstAmount(Integer amount) {
        return this.blogRepository.findFirstAmountOrderByCreateAt(amount, "asc");
    }

    public List<Blog> findLastAmount(Integer amount) {
        return this.blogRepository.findFirstAmountOrderByCreateAt(amount, "desc");
    }

    public List<Blog> findFirstAmountInCategories(Integer amount, List<Category> categoryList, UUID exceptBlogId) {
        return this.blogRepository.findFirstAmountInCategories(amount, categoryList, exceptBlogId);
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

    public List<Blog> findNumberOfPopularBlogsOrderBy(Integer amount, String orderBy) {
        return this.blogRepository.findNumberOfPopularBlogsOrderBy(amount, orderBy);
    }

    public ServiceResponse<Blog> create(CreateBlogDto payload) {
        final LocalDateTime currentTimestamp = LocalDateTime.now();
        final String slug = this.getSlug(payload.getTitle());
        final String savePath = this.fileUploadDirectory.getDirectory("blogBase") + "/" + slug;
        String savedAttachmentPath = this.fileUploadService.saveFile(payload.getAttachment(), savePath);

        Blog newBlog = new Blog();
        newBlog.setTitle(payload.getTitle());
        newBlog.setDescription(payload.getDescription());
        newBlog.setAttachment(savedAttachmentPath);
        newBlog.setCreateAt(currentTimestamp);
        newBlog.setUpdateAt(currentTimestamp);
        newBlog.setSlug(slug);

        RepositoryResponse<Blog> response = this.blogRepository.create(newBlog);
        if (response.hasErrorOf(RepositoryErrorType.CONSTRAINT_VIOLATION)) {
            return ServiceResponse.ofBadRequest(
                    response.getMessage(), response.getDescription());
        }
        return ServiceResponse.ofSuccess(
                "Created new category detail!", null, newBlog);
    }

    public ServiceResponse<Blog> update(UUID id, UpdateBlogDto payload) {
        Optional<Blog> updatingBlog = this.blogRepository.findById(id);
        if (!updatingBlog.isPresent()) {
            return ServiceResponse.ofNotFound(
                    "Blog not found", "Cannot find any blog with the given ID");
        }

        // * Save new file to local machine and update the file's path to database
        String slug = getSlug(payload.getTitle());
        final String savePath = this.fileUploadDirectory.getDirectory("blogBase") + "/" + slug;
        String savedAttachmentPath = this.fileUploadService.saveFile(payload.getAttachment(), savePath);

        // * Convert Date time from payload to LocalDate to save to database
        Instant instant = payload.getPublishAt().toInstant();
        LocalDate publishDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDateTime currentTimestamp = LocalDateTime.now();

        // * Set updated fields to a Blog object
        Blog updatedBlog = updatingBlog.get();
        updatedBlog.setTitle(payload.getTitle());
        updatedBlog.setDescription(payload.getDescription());
        updatedBlog.setAttachment(savedAttachmentPath);
        updatedBlog.setSlug(slug);
        updatedBlog.setPublishAt(publishDate);
        updatedBlog.setUpdateAt(currentTimestamp);
        updatedBlog.setHiddenStatus(payload.getHiddenStatus());

        RepositoryResponse<Blog> response = this.blogRepository.update(updatedBlog);
        if (response.hasErrorOf(RepositoryErrorType.CONSTRAINT_VIOLATION)) {
            return ServiceResponse.ofBadRequest(
                    response.getMessage(), response.getDescription());
        }
        return ServiceResponse.ofSuccess(
                "Blog updated successfully", null, updatedBlog);
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
        if (response.hasErrorOf(RepositoryErrorType.CONSTRAINT_VIOLATION)) {
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
