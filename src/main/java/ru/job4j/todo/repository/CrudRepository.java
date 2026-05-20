package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.CommonQueryContract;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

@Repository
@AllArgsConstructor
@SuppressWarnings("SqlSourceToSinkFlow")
public class CrudRepository {

    private final SessionFactory sessionFactory;

    private <T> T transaction(Function<Session, T> command) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            T result = command.apply(session);
            transaction.commit();
            return result;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
    }

    public void run(Consumer<Session> command) {
        transaction(session -> {
            command.accept(session);
            return null;
        });
    }

    private void setParameters(CommonQueryContract query, Map<String, Object> args) {
        for (Map.Entry<String, Object> entry : args.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
    }

    public void run(String query, Map<String, Object> args) {
        Consumer<Session> command = session -> {
            var sessionQuery = session.createMutationQuery(query);
            setParameters(sessionQuery, args);
            sessionQuery.executeUpdate();
        };
        run(command);
    }

    public <T> List<T> query(String query, Class<T> clazz) {
        Function<Session, List<T>> command = session ->
                session.createQuery(query, clazz).list();
        return transaction(command);
    }

    public <T> List<T> query(String query, Class<T> clazz, Map<String, Object> args) {
        Function<Session, List<T>> command = session -> {
            var sessionQuery = session.createQuery(query, clazz);
            setParameters(sessionQuery, args);
            return sessionQuery.list();
        };
        return transaction(command);
    }

    public int affectedRowsQuery(String query, Map<String, Object> args) {
        Function<Session, Integer> command = session -> {
            var sessionQuery = session.createMutationQuery(query);
            setParameters(sessionQuery, args);
            return sessionQuery.executeUpdate();
        };
        return transaction(command);
    }

    public <T> Optional<T> optional(String query, Class<T> clazz, Map<String, Object> args) {
        Function<Session, Optional<T>> command = session -> {
            var sessionQuery = session.createQuery(query, clazz);
            setParameters(sessionQuery, args);
            return sessionQuery.uniqueResultOptional();
        };
        return transaction(command);
    }
}
