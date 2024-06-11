package com.example.javaee.service;

import com.example.javaee.beans.FilePathBuilder;
import com.example.javaee.beans.FileUploadDirectory;
import com.example.javaee.dto.CreateBlogDto;
import com.example.javaee.dto.UpdateBlogDto;
import com.example.javaee.helper.RepositoryErrorType;
import com.example.javaee.helper.RepositoryResponse;
import com.example.javaee.helper.ServiceResponse;
import com.example.javaee.model.Blog;
import com.example.javaee.model.Category;
import com.example.javaee.repository.BlogRepository;
import jakarta.servlet.ServletContext;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class BlogService {
    private final ServletContext servletContext;
    private final FileUploadDirectory fileUploadDirectory;
    private final BlogRepository blogRepository;
    private final FileUploadService fileUploadService;

    public BlogService(
            ServletContext servletContext,
            FileUploadDirectory fileUploadDirectory,
            BlogRepository blogRepository,
            FileUploadService fileUploadService) {
        this.servletContext = servletContext;
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

    public List<Blog> findAllBlogByCategorySlug(Integer page, Integer size, String orderBy, String categorySlug) {
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

    public Long countNumberOfPage(Integer size) {
        Long totalNumberOfBlog = this.blogRepository.countNumberOfBlog();
        return totalNumberOfBlog / size + (totalNumberOfBlog % size == 0 ? 0 : 1);
    }

    public Long countNumberOfPage(Integer size, String slug) {
        Long totalNumberOfBlog = this.blogRepository.countNumberOfBlog(slug);
        return totalNumberOfBlog / size + (totalNumberOfBlog % size == 0 ? 0 : 1);
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

    public ServiceResponse<Blog> create(@Valid CreateBlogDto payload) {
        if (payload == null) {
            return ServiceResponse.ofBadRequest(
                    "Invalid Create Blog Dto",
                    "Create Blog Dto Cannot Be Null");
        }
        if (payload.getTitle() == null || payload.getTitle().isEmpty()) {
            return ServiceResponse.ofBadRequest(
                    "Invalid Blog Title",
                    "Blog Title Cannot Be Null Or Empty");
        }
        if (payload.getSubtitle() == null || payload.getSubtitle().isEmpty()) {
            return ServiceResponse.ofBadRequest(
                    "Invalid Blog Subtitle",
                    "Blog Subtitle Cannot Be Null Or Empty");
        }
        if (payload.getAttachment() == null || payload.getAttachment().isEmpty()) {
            return ServiceResponse.ofBadRequest(
                    "Invalid Blog Thumbnail",
                    "Blog Thumbnail Cannot Be Null Or Empty");
        }

        final LocalDateTime currentTimestamp = LocalDateTime.now();
        final String slug = this.getSlug(payload.getTitle());

        Blog newBlog = new Blog();
        newBlog.setSlug(slug);
        newBlog.setTitle(payload.getTitle());
        newBlog.setSubTitle(payload.getSubtitle());
        newBlog.setIsPopular(payload.getIsPopular());
        newBlog.setDescription(payload.getDescription());
        newBlog.setCreateAt(currentTimestamp);
        newBlog.setUpdateAt(currentTimestamp);

        FilePathBuilder filePathBuilder = new FilePathBuilder();
        filePathBuilder
                .beginWithBaseDirectory(this.fileUploadDirectory.getBaseDirectory())
                .addSubDirectory(slug);
        Boolean savingAttachment = this.fileUploadService.saveFile(
                payload.getAttachment(),
                this.servletContext.getRealPath(filePathBuilder.build()));

        if (!savingAttachment) {
            return ServiceResponse.ofUnknownServerError(
                    "Cannot save file!",
                    "File path:" + filePathBuilder.build());
        }

        newBlog.setAttachment(filePathBuilder.ofFile(payload.getAttachment()).build());
        newBlog.setThumbnail(payload.getAttachment().getOriginalFilename());

        RepositoryResponse<Blog> createBlogRepositoryResponse = this.blogRepository.create(newBlog);
        if (createBlogRepositoryResponse.hasErrorOf(RepositoryErrorType.CONSTRAINT_VIOLATION)) {
            return ServiceResponse.ofBadRequest(
                    createBlogRepositoryResponse.getMessage(),
                    createBlogRepositoryResponse.getDescription());
        }
        return ServiceResponse.ofSuccess(
                "Success", "New Blog Created", newBlog);
    }

    public ServiceResponse<Blog> update(UUID id, @Valid UpdateBlogDto payload) {
        Optional<Blog> updatingBlog = this.blogRepository.findById(id);
        if (!updatingBlog.isPresent()) {
            return ServiceResponse.ofNotFound(
                    "Blog not found", "Cannot find any blog with the given ID");
        }
        Blog updatedBlog = updatingBlog.get();
        String slug = getSlug(payload.getTitle());
        LocalDateTime currentTimestamp = LocalDateTime.now();

        if (payload.getTitle() == null || payload.getTitle().isEmpty()) {
            return ServiceResponse.ofBadRequest(
                    "Invalid UpdateBlogDto",
                    "Blog's Title Cannot Be Null Or Empty");
        }

        if (payload.getSubTitle() == null || payload.getSubTitle().isEmpty()) {
            return ServiceResponse.ofBadRequest(
                    "Invalid UpdateBlogDto",
                    "Blog's Subtitle Cannot Be Null Or Empty");
        }

        updatedBlog.setTitle(payload.getTitle());
        updatedBlog.setSubTitle(payload.getSubTitle());
        updatedBlog.setDescription(payload.getDescription());
        updatedBlog.setSlug(slug);
        updatedBlog.setUpdateAt(currentTimestamp);
        updatedBlog.setIsPopular(payload.getIsPopular());

        if (!payload.getAttachment().isEmpty()) {
            FilePathBuilder filePathBuilder = new FilePathBuilder();
            filePathBuilder
                  .beginWithBaseDirectory(this.fileUploadDirectory.getBaseDirectory())
                  .addSubDirectory(slug);
            Boolean savingAttachment = this.fileUploadService.saveFile(
                  payload.getAttachment(),
                  this.servletContext.getRealPath(filePathBuilder.build()));
            if (!savingAttachment) {
                return ServiceResponse.ofUnknownServerError(
                        "Cannot save file!",
                        "File path:" + filePathBuilder.build());

            }
            updatedBlog.setAttachment(filePathBuilder.ofFile(payload.getAttachment()).build());
            updatedBlog.setThumbnail(payload.getAttachment().getOriginalFilename());
        }

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
