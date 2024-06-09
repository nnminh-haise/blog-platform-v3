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
    private final SessionFactory sessionFactory;

    public CategoryDetailRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Transactional
    public List<CategoryDetail> findAll() {
        Session session = sessionFactory.getCurrentSession();
        final String Q_FIND_ALL_CATEGORY_DETAIL = "SELECT cd FROM CategoryDetail AS cd WHERE cd.deleteAt IS NULL";
        System.out.println("Fetching all category detail");
        Query<CategoryDetail> query = session.createQuery(Q_FIND_ALL_CATEGORY_DETAIL, CategoryDetail.class);
        List<CategoryDetail> categoryDetailList = query.list();
        System.out.println("Fetching process completed");
        return categoryDetailList;
    }

    @Transactional
    public Optional<CategoryDetail> findById(UUID id) {
        Session session = sessionFactory.getCurrentSession();
        final String Q_FIND_BY_ID = "SELECT cd FROM CategoryDetail AS cd WHERE cd.deleteAt IS NULL AND cd.id = :id";
        System.out.println("Fetching category detail by id = " + id);
        Query<CategoryDetail> query = session.createQuery(Q_FIND_BY_ID, CategoryDetail.class);
        query.setParameter("id", id);
        CategoryDetail categoryDetail = (CategoryDetail) query.uniqueResult();
        System.out.println("Fetching process completed");
        return Optional.ofNullable(categoryDetail);
    }

    @Transactional
    public List<CategoryDetail> findAllByCategoryId(UUID id) {
        Session session = sessionFactory.getCurrentSession();
        final String Q_FIND_ALL_CATEGORY_DETAIL_BY_CATEGORY_ID = "SELECT cd FROM CategoryDetail AS cd WHERE cd.deleteAt IS NULL and cd.category.id = :id";
        System.out.println("Fetching category detail by category id = " + id);
        Query<CategoryDetail> query = session.createQuery(
                Q_FIND_ALL_CATEGORY_DETAIL_BY_CATEGORY_ID, CategoryDetail.class);
        query.setParameter("id", id);
        List<CategoryDetail> categoryDetailList = query.list();
        System.out.println("Fetching process completed");
        return categoryDetailList;
    }

    @Transactional
    public List<CategoryDetail> findAllByBlogId(UUID id) {
        Session session = sessionFactory.getCurrentSession();
        final String Q_FIND_ALL_CATEGORY_DETAIL_BY_BLOG_ID = "SELECT cd FROM CategoryDetail AS cd WHERE cd.deleteAt IS NULL and cd.blog.id = :id";
        System.out.println("Fetching category detail by blog id = " + id);
        Query<CategoryDetail> query = session.createQuery(Q_FIND_ALL_CATEGORY_DETAIL_BY_BLOG_ID, CategoryDetail.class);
        query.setParameter("id", id);
        List<CategoryDetail> categoryDetailList = query.list();
        System.out.println("Fetching process completed");
        return categoryDetailList;
    }

    public RepositoryResponse<CategoryDetail> create(CategoryDetail categoryDetail) {
        RepositoryResponse<CategoryDetail> response = new RepositoryResponse<>();

        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            System.out.println("Creating new category detail");
            session.persist(categoryDetail);
            transaction.commit();
            System.out.println("Creating process success");

            return RepositoryResponse.goodResponse("New category detail created", categoryDetail);
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

    public RepositoryResponse<CategoryDetail> update(CategoryDetail categoryDetail) {
        RepositoryResponse<CategoryDetail> response = new RepositoryResponse<>();

        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            System.out.println("Updating category detail with id = " + categoryDetail.getId());
            session.merge(categoryDetail);
            transaction.commit();
            session.close();
            System.out.println("Updating process success");

            return RepositoryResponse.goodResponse("Category detail updated", categoryDetail);
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
    @Transactional
    public List<CategoryDetail> findByBlogId(UUID blogId) {
        Session session = sessionFactory.getCurrentSession();
        final String Q_FIND_BY_BLOG_ID = "SELECT cd FROM CategoryDetail AS cd WHERE cd.deleteAt IS NULL AND cd.blog.id = :blogId";
        System.out.println("Fetching category detail by blog id = " + blogId);
        Query<CategoryDetail> query = session.createQuery(Q_FIND_BY_BLOG_ID, CategoryDetail.class);
        query.setParameter("blogId", blogId);
        List<CategoryDetail> categoryDetailList = query.list();
        System.out.println("Fetching process completed");
        return categoryDetailList;
    }
}
