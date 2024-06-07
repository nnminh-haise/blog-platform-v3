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

    private final SessionFactory sessionFactory;

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
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            System.out.println("Creating new subcriber");
            session.persist(subscriber);
            transaction.commit();
            session.close();
            System.out.println("Creating process success");

            return RepositoryResponse.goodResponse("New subscriber created", subscriber);
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

    public RepositoryResponse<Subscriber> update(Subscriber subscriber) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            System.out.println("Updating subscriber with id = " + subscriber.getId());
            session.merge(subscriber);
            transaction.commit();
            session.close();
            System.out.println("Updating process success");

            return RepositoryResponse.goodResponse("Subscriber updated", subscriber);
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
