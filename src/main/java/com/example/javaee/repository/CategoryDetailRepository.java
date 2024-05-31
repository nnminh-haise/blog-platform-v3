package com.example.javaee.repository;

import com.example.javaee.helper.RepositoryErrorType;
import com.example.javaee.helper.RepositoryResponse;
import com.example.javaee.helper.ResponseType;
import com.example.javaee.model.CategoryDetail;
import jakarta.transaction.Transactional;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.postgresql.util.PSQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.core.NestedExceptionUtils.getRootCause;

@Repository
public class CategoryDetailRepository {
    private static final Logger logger = LoggerFactory.getLogger(CategoryDetailRepository.class);
    private final SessionFactory sessionFactory;

    public CategoryDetailRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Transactional
    public List<CategoryDetail> findAll() {
        Session session = sessionFactory.getCurrentSession();
        final String Q_FIND_ALL_CATEGORY_DETAIL = "SELECT cd FROM CategoryDetail AS cd WHERE cd.deleteAt IS NULL";
        logger.info("Fetching all category detail");
        Query<CategoryDetail> query = session.createQuery(Q_FIND_ALL_CATEGORY_DETAIL, CategoryDetail.class);
        List<CategoryDetail> categoryDetailList = query.list();
        logger.info("Fetching process completed");
        return categoryDetailList;
    }

    @Transactional
    public Optional<CategoryDetail> findById(UUID id) {
        Session session = sessionFactory.getCurrentSession();
        final String Q_FIND_BY_ID = "SELECT cd FROM CategoryDetail AS cd WHERE cd.deleteAt IS NULL AND cd.id = :id";
        logger.info("Fetching category detail by id = " + id);
        Query<CategoryDetail> query = session.createQuery(Q_FIND_BY_ID, CategoryDetail.class);
        query.setParameter("id", id);
        CategoryDetail categoryDetail = (CategoryDetail) query.uniqueResult();
        logger.info("Fetching process completed");
        return Optional.ofNullable(categoryDetail);
    }

    @Transactional
    public List<CategoryDetail> findAllByCategoryId(UUID id) {
        Session session = sessionFactory.getCurrentSession();
        final String Q_FIND_ALL_CATEGORY_DETAIL_BY_CATEGORY_ID = "SELECT cd FROM CategoryDetail AS cd WHERE cd.deleteAt IS NULL and cd.category.id = :id";
        logger.info("Fetching category detail by category id = " + id);
        Query<CategoryDetail> query = session.createQuery(
                Q_FIND_ALL_CATEGORY_DETAIL_BY_CATEGORY_ID, CategoryDetail.class);
        query.setParameter("id", id);
        List<CategoryDetail> categoryDetailList = query.list();
        logger.info("Fetching process completed");
        return categoryDetailList;
    }

    @Transactional
    public List<CategoryDetail> findAllByBlogId(UUID id) {
        Session session = sessionFactory.getCurrentSession();
        final String Q_FIND_ALL_CATEGORY_DETAIL_BY_BLOG_ID = "SELECT cd FROM CategoryDetail AS cd WHERE cd.deleteAt IS NULL and cd.blog.id = :id";
        logger.info("Fetching category detail by blog id = " + id);
        Query<CategoryDetail> query = session.createQuery(Q_FIND_ALL_CATEGORY_DETAIL_BY_BLOG_ID, CategoryDetail.class);
        query.setParameter("id", id);
        List<CategoryDetail> categoryDetailList = query.list();
        logger.info("Fetching process completed");
        return categoryDetailList;
    }

    @Transactional
    public List<CategoryDetail> findAllBlogHasCategoryWithOrder(
            int page, int size, String orderBy, String categorySlug) {
        Session session = sessionFactory.getCurrentSession();
        final String Q_FIND_ALL_BLOG_BY_CATEGORY_SLUG = "SELECT CD FROM CategoryDetail AS CD " +
                "WHERE CD.blog.deleteAt IS NULL" +
                " AND CD.category.slug = :categorySlug ORDER BY " +
                "CASE WHEN :orderBy = 'asc' THEN CD.blog.createAt END ASC, " +
                "CASE WHEN :orderBy = 'desc' THEN CD.blog.createAt END DESC";
        Query<CategoryDetail> query = session.createQuery(Q_FIND_ALL_BLOG_BY_CATEGORY_SLUG, CategoryDetail.class);
        query.setParameter("orderBy", orderBy);
        query.setParameter("categorySlug", categorySlug);
        query.setFirstResult(page * size);
        query.setMaxResults(size);
        return query.list();
    }

    public RepositoryResponse<CategoryDetail> create(CategoryDetail categoryDetail) {
        RepositoryResponse<CategoryDetail> response = new RepositoryResponse<>();

        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            logger.info("Creating new category detail");
            session.persist(categoryDetail);
            transaction.commit();
            logger.info("Creating process success");

            response.setType(ResponseType.SUCCESS);
            response.setData(Optional.of(categoryDetail));
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

    public RepositoryResponse<CategoryDetail> update(CategoryDetail categoryDetail) {
        RepositoryResponse<CategoryDetail> response = new RepositoryResponse<>();

        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            logger.info("Updating category detail with id = " + categoryDetail.getId());
            session.merge(categoryDetail);
            transaction.commit();
            session.close();
            logger.info("Updating process success");

            response.setType(ResponseType.SUCCESS);
            response.setData(Optional.of(categoryDetail));
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
