package com.example.javaee.repository;

import com.example.javaee.helper.RepositoryErrorType;
import com.example.javaee.helper.RepositoryResponse;
import com.example.javaee.model.Category;
import org.hibernate.query.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.postgresql.util.PSQLException;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.core.NestedExceptionUtils.getRootCause;

@Repository
public class CategoryRepository {
    private final SessionFactory sessionFactory;

    public CategoryRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Transactional
    public List<Category> findAll() {
        Session session = sessionFactory.getCurrentSession();
        final String Q_FIND_ALL = "SELECT c FROM Category AS c WHERE c.deleteAt IS NULL";
        Query<Category> query = session.createQuery(Q_FIND_ALL, Category.class);
        return query.list();
    }

    @Transactional
    public List<Category> findAll(int page, int size, String orderBy) {
        Session session = sessionFactory.getCurrentSession();
        final String Q_FIND_ALL = "SELECT c FROM Category AS c WHERE c.deleteAt IS NULL " +
                "ORDER BY c.createAt " + (orderBy.equalsIgnoreCase("asc") ? "ASC" : "DESC");

        Query<Category> query = session.createQuery(Q_FIND_ALL, Category.class);
        query.setFirstResult(page * size);
        query.setMaxResults(size);
        return query.list();
    }

    @Transactional
    public Optional<Category> findById(UUID id) {
        Session session = sessionFactory.getCurrentSession();
        final String Q_FIND_CATEGORY_BY_ID = "SELECT c FROM Category AS c WHERE c.deleteAt IS NULL AND c.id = :id";
        Query<Category> query = session.createQuery(Q_FIND_CATEGORY_BY_ID, Category.class);
        query.setParameter("id", id);
        Category category = (Category) query.uniqueResult();
        return Optional.ofNullable(category);
    }

    @Transactional
    public Optional<Category> findBySlug(String slug) {
        Session session = sessionFactory.getCurrentSession();
        final String Q_FIND_CATEGORY_BY_ID = "SELECT c FROM Category AS c WHERE c.deleteAt IS NULL AND c.slug = :slug";
        Query<Category> query = session.createQuery(Q_FIND_CATEGORY_BY_ID, Category.class);
        query.setParameter("slug", slug);
        Category category = query.uniqueResult();
        return Optional.ofNullable(category);
    }

    @Transactional
    public Long countNumberOfCategory() {
        Session session = sessionFactory.getCurrentSession();
        final String Q_FIND_POPULAR_BLOG = "SELECT COUNT(c) FROM Category AS c WHERE c.deleteAt IS NULL";
        Query<Long> query = session.createQuery(Q_FIND_POPULAR_BLOG, Long.class);
        return query.uniqueResult();
    }

    public RepositoryResponse<Category> create(Category category) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            System.out.println("Creating new category");
            session.persist(category);
            transaction.commit();
            System.out.println("Creating process success");

            return RepositoryResponse.goodResponse("New category created", category);
        } catch (Exception exception) {
            transaction.rollback();

            Throwable rootCause = getRootCause(exception);
            final String EXCEPTION_MESSAGE = exception.getMessage();

            System.out.println("Error message: " + EXCEPTION_MESSAGE);

            if (rootCause instanceof PSQLException) {
                final String ROOT_CAUSE_MESSAGE = rootCause.getMessage();
                System.out.println("Root cause   : " + ROOT_CAUSE_MESSAGE);
                return RepositoryResponse.badResponse(RepositoryErrorType.CONSTRAINT_VIOLATION, EXCEPTION_MESSAGE, ROOT_CAUSE_MESSAGE);
            } else {
                System.out.println("Root cause   : Unknown Server Exception");
                return RepositoryResponse.badResponse(RepositoryErrorType.CONSTRAINT_VIOLATION, EXCEPTION_MESSAGE, "Unknown Server Exception");
            }
        } finally {
            session.close();
        }
    }

    public RepositoryResponse<Category> update(Category category) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            System.out.println("Updating new category");
            session.merge(category);
            transaction.commit();
            System.out.println("Updating process success");

            return RepositoryResponse.goodResponse("Category updated", category);
        } catch (Exception exception) {
            transaction.rollback();

            Throwable rootCause = getRootCause(exception);
            final String EXCEPTION_MESSAGE = exception.getMessage();

            System.out.println("Error message: " + EXCEPTION_MESSAGE);

            if (rootCause instanceof PSQLException) {
                final String ROOT_CAUSE_MESSAGE = rootCause.getMessage();
                System.out.println("Root cause   : " + ROOT_CAUSE_MESSAGE);
                return RepositoryResponse.badResponse(RepositoryErrorType.CONSTRAINT_VIOLATION, EXCEPTION_MESSAGE, ROOT_CAUSE_MESSAGE);
            } else {
                System.out.println("Root cause   : Unknown Server Exception");
                return RepositoryResponse.badResponse(RepositoryErrorType.CONSTRAINT_VIOLATION, EXCEPTION_MESSAGE, "Unknown Server Exception");
            }
        } finally {
            session.close();
        }
    }

    public RepositoryResponse<Category> remove(Category category) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            System.out.println("Removing new category");
            session.remove(category);
            transaction.commit();
            System.out.println("Removing process success");

            return RepositoryResponse.goodResponse("Category updated", category);
        } catch (Exception exception) {
            transaction.rollback();

            Throwable rootCause = getRootCause(exception);
            final String EXCEPTION_MESSAGE = exception.getMessage();

            System.out.println("Error message: " + EXCEPTION_MESSAGE);

            if (rootCause instanceof PSQLException) {
                final String ROOT_CAUSE_MESSAGE = rootCause.getMessage();
                System.out.println("Root cause   : " + ROOT_CAUSE_MESSAGE);
                return RepositoryResponse.badResponse(RepositoryErrorType.CONSTRAINT_VIOLATION, EXCEPTION_MESSAGE, ROOT_CAUSE_MESSAGE);
            } else {
                System.out.println("Root cause   : Unknown Server Exception");
                return RepositoryResponse.badResponse(RepositoryErrorType.CONSTRAINT_VIOLATION, EXCEPTION_MESSAGE, "Unknown Server Exception");
            }
        } finally {
            session.close();
        }
    }
}
