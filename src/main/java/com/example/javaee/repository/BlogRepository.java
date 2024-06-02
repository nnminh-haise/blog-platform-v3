package com.example.javaee.repository;

import com.example.javaee.helper.RepositoryErrorType;
import com.example.javaee.helper.RepositoryResponse;
import com.example.javaee.helper.ResponseType;
import com.example.javaee.model.Blog;

import com.example.javaee.model.Category;
import org.hibernate.*;
import org.hibernate.query.Query;
import org.postgresql.util.PSQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.core.NestedExceptionUtils.getRootCause;

@Repository
public class BlogRepository {
    private static final Logger logger = LoggerFactory.getLogger(CategoryDetailRepository.class);

    private final SessionFactory sessionFactory;

    public BlogRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Transactional
    public List<Blog> findAll() {
        Session session = sessionFactory.getCurrentSession();
        final String Q_FIND_ALL_BLOG = "SELECT b FROM Blog AS b WHERE b.deleteAt IS NULL";
        Query<Blog> query = session.createQuery(Q_FIND_ALL_BLOG, Blog.class);
        return query.list();
    }

    @Transactional
    public Blog findOnePopular() {
        Session session = sessionFactory.getCurrentSession();
        final String Q_FIND_POPULAR_BLOG = "SELECT b FROM Blog AS b WHERE b.deleteAt IS NULL AND b.isPopular = true";
        Query<Blog> query = session.createQuery(Q_FIND_POPULAR_BLOG, Blog.class);
        query.setMaxResults(1); // Limiting the result set to 1
        return query.uniqueResult();
    }

    @Transactional
    public List<Blog> findAllByCategorySlug(int page, int size, String orderBy, String categorySlug) {
        Session session = sessionFactory.getCurrentSession();
        final String Q_FIND_ALL_BY_CATEGORY_SLUG =
                "SELECT DISTINCT cd.blog FROM CategoryDetail AS cd " +
                        "WHERE cd.blog.deleteAt IS NULL AND cd.category.slug = :categorySlug " +
                        "ORDER BY cd.blog.publishAt " + (orderBy.equals("asc") ? "ASC" : "DESC");
        final String Q_FIND_ALL =
                "SELECT b FROM Blog AS b WHERE b.deleteAt IS NULL " +
                        "ORDER BY b.publishAt " + (orderBy.equalsIgnoreCase("asc") ? "ASC" : "DESC");

        Query<Blog> query = session.createQuery(
                (categorySlug == null ? Q_FIND_ALL : Q_FIND_ALL_BY_CATEGORY_SLUG), Blog.class);
        if (categorySlug != null) {
            query.setParameter("categorySlug", categorySlug);
        }
        query.setFirstResult(page * size);
        query.setMaxResults(size);
        return query.list();
    }

    @Transactional
    public List<Blog> findFirst(Integer amount) {
        Session session = sessionFactory.getCurrentSession();
        final String Q_FIND_ALL_BLOG = "SELECT b FROM Blog AS b WHERE b.deleteAt IS NULL ORDER BY b.createAt ASC";
        Query<Blog> query = session.createQuery(Q_FIND_ALL_BLOG, Blog.class);
        query.setMaxResults(amount);
        return query.list();
    }

    @Transactional
    public List<Blog> findLast(Integer amount) {
        Session session = sessionFactory.getCurrentSession();
        final String Q_FIND_ALL_BLOG = "SELECT b FROM Blog AS b WHERE b.deleteAt IS NULL ORDER BY b.createAt DESC";
        Query<Blog> query = session.createQuery(Q_FIND_ALL_BLOG, Blog.class);
        query.setMaxResults(amount);
        return query.list();
    }


    @Transactional
    public List<Blog> findFirstOfCategories(Integer amount, List<Category> categories, UUID blogId) {
        List<UUID> categoryIds = new ArrayList<>();
        for (Category category: categories) {
            categoryIds.add(category.getId());
        }

        Session session = sessionFactory.getCurrentSession();
        final String Q_FIND_ALL_BLOG =
                "SELECT cd.blog FROM CategoryDetail AS cd " +
                "WHERE cd.blog.deleteAt IS NULL AND " +
                "NOT cd.blog.id = :blogId AND " +
                "cd.category.id in :categoryIds " +
                "ORDER BY cd.blog.createAt ASC";
        Query<Blog> query = session.createQuery(Q_FIND_ALL_BLOG, Blog.class);
        query.setParameter("categoryIds", categoryIds);
        query.setParameter("blogId", blogId);
        query.setMaxResults(amount);
        return query.list();
    }

    @Transactional
    public Optional<Blog> findById(UUID id) {
        Session session = sessionFactory.getCurrentSession();
        final String Q_FIND_BLOG_BY_ID = "SELECT b FROM Blog AS b WHERE b.deleteAt IS NULL AND b.id = :id";
        Query<Blog> query = session.createQuery(Q_FIND_BLOG_BY_ID, Blog.class);
        query.setParameter("id", id);
        Blog blog = (Blog) query.uniqueResult();
        return Optional.ofNullable(blog);
    }

    @Transactional
    public Optional<Blog> findBySlug(String slug) {
        Session session = sessionFactory.getCurrentSession();
        final String Q_FIND_BLOG_BY_SLUG = "SELECT b FROM Blog AS b WHERE b.deleteAt IS NULL AND b.slug = :slug";
        Query<Blog> query = session.createQuery(Q_FIND_BLOG_BY_SLUG, Blog.class);
        query.setParameter("slug", slug);
        Blog blog = (Blog) query.uniqueResult();
        return Optional.ofNullable(blog);
    }

    @Transactional
    public List<Blog> findPopularBlogs(Integer amount) {
        Session session = sessionFactory.getCurrentSession();
        final String Q_FIND_POPULAR_BLOG_WITH_AMOUNT = "SELECT b FROM Blog AS b WHERE b.deleteAt IS NULL AND b.isPopular = true";
        Query<Blog> query = session.createQuery(Q_FIND_POPULAR_BLOG_WITH_AMOUNT, Blog.class);
        query.setMaxResults(amount);
        return query.list();
    }

    public RepositoryResponse<Blog> create(Blog blog) {
        RepositoryResponse<Blog> response = new RepositoryResponse<>();

        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            logger.info("Creating new blog");
            session.persist(blog);
            transaction.commit();
            logger.info("Creating process success");

            response.setType(ResponseType.SUCCESS);
            response.setData(Optional.of(blog));
            return response;
        } catch (Exception exception) {
            transaction.rollback();

            Throwable rootCause = getRootCause(exception);
            final String EXCEPTION_MESSAGE = exception.getMessage();

            response.setError(RepositoryErrorType.CONSTRAINT_VIOLATION);
            response.setMessage(EXCEPTION_MESSAGE);
            logger.error("Error message: " + EXCEPTION_MESSAGE);

            if (rootCause instanceof PSQLException) {
                final String ROOT_CAUSE_MESSAGE = rootCause.getMessage();
                response.setDescription(ROOT_CAUSE_MESSAGE);
                logger.error("Root cause   : " + ROOT_CAUSE_MESSAGE);
            } else {
                response.setDescription("Unknown Server Exception");
                logger.error("Root cause   : Unknown Server Exception");
            }
            return response;
        } finally {
            session.close();
        }
    }

    public RepositoryResponse<Blog> update(Blog blog) {
        RepositoryResponse<Blog> response = new RepositoryResponse<>();

        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            logger.info("Updating blog with id = " + blog.getId());
            session.merge(blog);
            transaction.commit();
            session.close();
            logger.info("Updating process success");

            response.setType(ResponseType.SUCCESS);
            response.setData(Optional.of(blog));
            return response;
        } catch (Exception exception) {
            transaction.rollback();

            Throwable rootCause = getRootCause(exception);
            final String EXCEPTION_MESSAGE = exception.getMessage();

            response.setError(RepositoryErrorType.CONSTRAINT_VIOLATION);
            response.setMessage(EXCEPTION_MESSAGE);
            logger.error("Error message: " + EXCEPTION_MESSAGE);

            if (rootCause instanceof PSQLException) {
                final String ROOT_CAUSE_MESSAGE = rootCause.getMessage();
                response.setDescription(ROOT_CAUSE_MESSAGE);
                logger.error("Root cause   : " + ROOT_CAUSE_MESSAGE);
            } else {
                response.setDescription("Unknown Server Exception");
                logger.error("Root cause   : Unknown Server Exception");
            }
            return response;
        } finally {
            session.close();
        }
    }
}
