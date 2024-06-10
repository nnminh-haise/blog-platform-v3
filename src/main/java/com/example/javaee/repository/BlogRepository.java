package com.example.javaee.repository;

import com.example.javaee.helper.RepositoryErrorType;
import com.example.javaee.helper.RepositoryResponse;

import com.example.javaee.model.Blog;
import com.example.javaee.model.Category;
import org.hibernate.*;
import org.hibernate.query.Query;
import org.postgresql.util.PSQLException;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.core.NestedExceptionUtils.getRootCause;

@Repository
public class BlogRepository {
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
        query.setMaxResults(1);
        return query.uniqueResult();
    }

    @Transactional
    public Long countNumberOfBlog() {
        Session session = sessionFactory.getCurrentSession();
        final String Q_FIND_POPULAR_BLOG = "SELECT COUNT(b) FROM Blog AS b WHERE b.deleteAt IS NULL";
        Query<Long> query = session.createQuery(Q_FIND_POPULAR_BLOG, Long.class);
        return query.uniqueResult();
    }

    // TODO [Low prior]: refactor this method
    @Transactional
    public List<Blog> findAllByCategorySlug(int page, int size, String orderBy, String categorySlug) {
        Session session = sessionFactory.getCurrentSession();
        final String Q_FIND_ALL_BY_CATEGORY_SLUG = "SELECT DISTINCT cd.blog FROM CategoryDetail AS cd " +
                "WHERE cd.blog.deleteAt IS NULL AND cd.category.deleteAt IS NULL AND cd.category.slug = :categorySlug " +
                "ORDER BY cd.blog.createAt " + (orderBy.equals("asc") ? "ASC" : "DESC");
        final String Q_FIND_ALL = "SELECT b FROM Blog AS b WHERE b.deleteAt IS NULL " +
                "ORDER BY b.createAt " + (orderBy.equalsIgnoreCase("asc") ? "ASC" : "DESC");

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
    public List<Blog> findFirstAmountOrderByCreateAt(Integer amount, String orderBy) {
        Session session = sessionFactory.getCurrentSession();
        final String Q_FIND_ALL_BLOG = "SELECT b FROM Blog AS b WHERE b.deleteAt IS NULL ORDER BY b.createAt " +
                (orderBy.equals("asc") ? "ASC" : "DESC");
        Query<Blog> query = session.createQuery(Q_FIND_ALL_BLOG, Blog.class);
        query.setMaxResults(amount);
        return query.list();
    }

    @Transactional
    public List<Blog> findFirstAmountInCategories(Integer amount, List<Category> categoryList, UUID exceptBlogId) {
        List<UUID> categoryIds = new ArrayList<>();
        for (Category category : categoryList) {
            categoryIds.add(category.getId());
        }

        Session session = sessionFactory.getCurrentSession();
        final String Q_FIND_ALL_BLOG_IN_CATEGORIES_EXCEPT_FOR_BLOG = "SELECT cd.blog FROM CategoryDetail AS cd " +
                "WHERE " +
                "cd.blog.deleteAt IS NULL AND " +
                "(:exceptBlogId IS NULL OR NOT cd.blog.id = :exceptBlogId) AND " +
                "cd.category.id in :categoryIds AND cd.category.deleteAt IS NULL " +
                "ORDER BY cd.blog.createAt ASC";
        Query<Blog> query = session.createQuery(Q_FIND_ALL_BLOG_IN_CATEGORIES_EXCEPT_FOR_BLOG, Blog.class);
        query.setParameter("categoryIds", categoryIds);
        query.setParameter("exceptBlogId", exceptBlogId);
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
    public List<Blog> findNumberOfPopularBlogsOrderBy(Integer amount, String orderBy) {
        Session session = sessionFactory.getCurrentSession();
        final String Q_FIND_POPULAR_BLOG_WITH_AMOUNT_ORDER_BY = "SELECT b FROM Blog AS b " +
                "WHERE " +
                "b.deleteAt IS NULL AND " +
                "b.isPopular = true " +
                "ORDER BY b.createAt " + (orderBy.equalsIgnoreCase("asc") ? "ASC" : "DESC");
        Query<Blog> query = session.createQuery(Q_FIND_POPULAR_BLOG_WITH_AMOUNT_ORDER_BY, Blog.class);
        query.setMaxResults(amount);
        return query.list();
    }

    public RepositoryResponse<Blog> create(Blog blog) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            System.out.println("Creating new blog");
            session.persist(blog);
            transaction.commit();
            System.out.println("Creating process success");

            return RepositoryResponse.goodResponse("New blog created", blog);
        } catch (Exception exception) {
            transaction.rollback();

            Throwable rootCause = getRootCause(exception);
            final String EXCEPTION_MESSAGE = exception.getMessage();

            System.out.println("Error message: " + EXCEPTION_MESSAGE);

            if (rootCause instanceof PSQLException) {
                final String ROOT_CAUSE_MESSAGE = rootCause.getMessage();
                System.out.println("Root cause   : " + ROOT_CAUSE_MESSAGE);
                return RepositoryResponse.badResponse(RepositoryErrorType.CONSTRAINT_VIOLATION, EXCEPTION_MESSAGE,
                        ROOT_CAUSE_MESSAGE);
            } else {
                System.out.println("Root cause   : Unknown Server Exception");
                return RepositoryResponse.badResponse(RepositoryErrorType.CONSTRAINT_VIOLATION, EXCEPTION_MESSAGE,
                        "Unknown Server Exception");
            }
        } finally {
            session.close();
        }
    }

    public RepositoryResponse<Blog> update(Blog blog) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            System.out.println("Updating blog with id = " + blog.getId());
            session.merge(blog);
            transaction.commit();
            session.close();
            System.out.println("Updating process success");

            return RepositoryResponse.goodResponse("Blog updated", blog);
        } catch (Exception exception) {
            transaction.rollback();

            Throwable rootCause = getRootCause(exception);
            final String EXCEPTION_MESSAGE = exception.getMessage();

            System.out.println("Error message: " + EXCEPTION_MESSAGE);

            if (rootCause instanceof PSQLException) {
                final String ROOT_CAUSE_MESSAGE = rootCause.getMessage();
                System.out.println("Root cause   : " + ROOT_CAUSE_MESSAGE);
                return RepositoryResponse.badResponse(RepositoryErrorType.CONSTRAINT_VIOLATION, EXCEPTION_MESSAGE,
                        ROOT_CAUSE_MESSAGE);
            } else {
                System.out.println("Root cause   : Unknown Server Exception");
                return RepositoryResponse.badResponse(RepositoryErrorType.CONSTRAINT_VIOLATION, EXCEPTION_MESSAGE,
                        "Unknown Server Exception");
            }
        } finally {
            session.close();
        }
    }
}
