package com.example.javaee.repository;

import com.example.javaee.dto.ErrorResponse;
import com.example.javaee.exceptions.ResourceNotFoundException;
import com.example.javaee.model.Category;
import org.hibernate.query.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.core.NestedExceptionUtils.getRootCause;

@Repository
public class CategoryRepository {
    @Autowired
    private SessionFactory sessionFactory;

    @Transactional
    public List<Category> findAll() {
        Session session = sessionFactory.openSession();
        String findAllCategoryQuery = "SELECT c FROM Category AS c WHERE c.deleteAt IS NULL";
        Query<Category> query = session.createQuery(findAllCategoryQuery, Category.class);
        List<Category> categories = query.list();

        if (categories.isEmpty()) {
            throw new ResourceNotFoundException("Cannot find any category");
        }
        return categories;
    }
    
    @Transactional
    public Optional<Category> findById(UUID id) {
        Session session = sessionFactory.openSession();
        String findCategoryById = "SELECT c FROM Category AS c WHERE c.deleteAt IS NULL AND c.id = :id";
        Query<Category> query = session.createQuery(findCategoryById, Category.class);
        query.setParameter("id", id);
        Category category = (Category) query.uniqueResult();
        return Optional.ofNullable(category);
    }

    public ErrorResponse save(Category category) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.persist(category);
            transaction.commit();
            session.close();
            return ErrorResponse.noError();
        }
        catch (Exception exception) {
            transaction.rollback();
            session.close();

            Throwable rootCause = getRootCause(exception);
            if (rootCause instanceof PSQLException) {
                return new ErrorResponse(
                        HttpStatus.BAD_REQUEST.value(),
                        exception.getMessage(),
                        rootCause.getMessage());
            }

            return new ErrorResponse(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "SQL Runtime error",
                    exception.getMessage());
        }
    }

    public ErrorResponse update(Category category) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.merge(category);
            transaction.commit();
            session.close();
            return ErrorResponse.noError();
        }
        catch (Exception exception) {
            transaction.rollback();
            session.close();

            Throwable rootCause = getRootCause(exception);
            if (rootCause instanceof PSQLException) {
                return new ErrorResponse(
                        HttpStatus.BAD_REQUEST.value(),
                        exception.getMessage(),
                        rootCause.getMessage());
            }

            return new ErrorResponse(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "SQL Runtime error",
                    exception.getMessage());
        }
    }

    public ErrorResponse remove(UUID id) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            Optional<Category> targetingCategory = findById(id);
            targetingCategory.ifPresent((category -> {
                category.setDeleteAt(Timestamp.valueOf(LocalDateTime.now()));
                session.merge(category);
            }));
            transaction.commit();
            session.close();
            return ErrorResponse.noError();
        }
        catch (Exception exception) {
            transaction.rollback();
            session.close();

            Throwable rootCause = getRootCause(exception);
            if (rootCause instanceof PSQLException) {
                return new ErrorResponse(
                        HttpStatus.BAD_REQUEST.value(),
                        exception.getMessage(),
                        rootCause.getMessage());
            }

            return new ErrorResponse(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "SQL Runtime error",
                    exception.getMessage());
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
}
