package com.example.javaee.repository;

import com.example.javaee.helper.RepositoryErrorType;
import com.example.javaee.helper.RepositoryResponse;
import com.example.javaee.helper.ResponseType;
import com.example.javaee.model.Subscriber;
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
public class SubscriberRepository {
    private static final Logger logger = LoggerFactory.getLogger(CategoryDetailRepository.class);

    private SessionFactory sessionFactory;

    public SubscriberRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Transactional
    public List<Subscriber> findAll() {
        Session session = sessionFactory.getCurrentSession();
        final String Q_FIND_ALL_SUBSCRIBER = "SELECT s FROM Subscriber AS s WHERE s.deleteAt IS NULL";
        Query<Subscriber> query = session.createQuery(Q_FIND_ALL_SUBSCRIBER, Subscriber.class);
        return query.list();
    }

    @Transactional
    public Optional<Subscriber> findById(UUID id) {
        Session session = sessionFactory.getCurrentSession();
        final String Q_FIND_BY_ID = "SELECT s FROM Subscriber AS s WHERE s.deleteAt IS NULL AND s.id = :id";
        Query<Subscriber> query = session.createQuery(Q_FIND_BY_ID, Subscriber.class);
        query.setParameter("id", id);
        Subscriber subscriber = (Subscriber) query.uniqueResult();
        return Optional.ofNullable(subscriber);
    }

    public RepositoryResponse<Subscriber> create(Subscriber subscriber) {
        RepositoryResponse<Subscriber> response = new RepositoryResponse<>();

        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            logger.info("Creating new subcriber");
            session.persist(subscriber);
            transaction.commit();
            session.close();
            logger.info("Creating process success");

            response.setType(ResponseType.SUCCESS);
            response.setData(Optional.of(subscriber));
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

    public RepositoryResponse<Subscriber> update(Subscriber subscriber) {
        RepositoryResponse<Subscriber> response = new RepositoryResponse<>();

        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            logger.info("Updating subscriber with id = " + subscriber.getId());
            session.merge(subscriber);
            transaction.commit();
            session.close();
            logger.info("Updating process success");

            response.setType(ResponseType.SUCCESS);
            response.setData(Optional.of(subscriber));
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
