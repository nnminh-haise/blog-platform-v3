package com.example.javaee.repository;

import com.example.javaee.dto.ErrorResponse;
import com.example.javaee.helper.RepositoryErrorType;
import com.example.javaee.helper.RepositoryResponse;
import com.example.javaee.helper.ResponseType;
import com.example.javaee.model.Category;
import org.hibernate.query.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.postgresql.util.PSQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.core.NestedExceptionUtils.getRootCause;

@Repository
public class CategoryRepository {
    private static final Logger logger = LoggerFactory.getLogger(CategoryDetailRepository.class);
    private final SessionFactory sessionFactory;

    public CategoryRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Transactional
    public List<Category> findAll() {
        Session session = sessionFactory.getCurrentSession();
        final String Q_FIND_ALL_CATEGORY = "SELECT c FROM Category AS c WHERE c.deleteAt IS NULL";
        Query<Category> query = session.createQuery(Q_FIND_ALL_CATEGORY, Category.class);
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
        Category category = (Category) query.uniqueResult();
        return Optional.ofNullable(category);
    }

    public RepositoryResponse<Category> create(Category category) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            logger.info("Creating new category");
            session.persist(category);
            transaction.commit();
            logger.info("Creating process success");

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
            logger.info("Updating new category");
            session.merge(category);
            transaction.commit();
            logger.info("Updating process success");

            return RepositoryResponse.goodResponse("New category updated", category);
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

    //find by name
    public Optional<Category> findByName(String name) {
        Session session = sessionFactory.openSession();
        String findCategoryByName = "SELECT c FROM Category AS c WHERE c.deleteAt IS NULL AND c.name = :name";
        Query<Category> query = session.createQuery(findCategoryByName, Category.class);
        query.setParameter("name", name);
        Category category = (Category) query.uniqueResult();
        return Optional.ofNullable(category);
    }
    //pagination with category
    public List<Category> pagination(int page, int limit) {
        Session session = sessionFactory.openSession();
        String paginationQuery = "SELECT c FROM Category AS c WHERE c.deleteAt IS NULL";
        Query<Category> query = session.createQuery(paginationQuery, Category.class);
        query.setFirstResult((page - 1) * limit);
        query.setMaxResults(limit);
        List<Category> categories = query.list();
        return categories;
    }

    //remove
    public ErrorResponse remove(UUID id) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            Category category = session.get(Category.class, id);
            if (category == null) {
                transaction.rollback();
                session.close();
                return new ErrorResponse(
                        HttpStatus.NOT_FOUND.value(),
                        "Category not found",
                        "Cannot find any category with the given ID");
            }
            session.delete(category);
            transaction.commit();
            session.close();
            return ErrorResponse.noError();
        }
        catch (Exception exception) {
            transaction.rollback();
            session.close();
            return new ErrorResponse(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "SQL Runtime error",
                    exception.getMessage());
        }
    }
    //save
    public ErrorResponse save(Category category) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.save(category);
            transaction.commit();
            session.close();
            return ErrorResponse.noError();
        }
        catch (Exception exception) {
            transaction.rollback();
            session.close();
            return new ErrorResponse(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "SQL Runtime error",
                    exception.getMessage());
        }
    }
}
