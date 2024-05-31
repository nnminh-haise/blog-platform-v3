package com.example.javaee.repository;

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
        RepositoryResponse<Category> response = new RepositoryResponse<>();

        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            logger.info("Creating new category");
            session.persist(category);
            transaction.commit();
            logger.info("Creating process success");

            response.setType(ResponseType.SUCCESS);
            response.setData(Optional.of(category));
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

    public RepositoryResponse<Category> update(Category category) {
        RepositoryResponse<Category> response = new RepositoryResponse<>();

        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            logger.info("Updating category with id = " + category.getId());
            session.merge(category);
            transaction.commit();
            session.close();
            logger.info("Updating process success");

            response.setType(ResponseType.SUCCESS);
            response.setData(Optional.of(category));
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
