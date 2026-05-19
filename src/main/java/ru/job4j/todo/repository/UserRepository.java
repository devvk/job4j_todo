package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.User;

import java.sql.SQLException;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class UserRepository {

    private final SessionFactory sessionFactory;

    public Optional<User> save(User user) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.persist(user);
                transaction.commit();
                return Optional.of(user);
            } catch (HibernateException e) {
                transaction.rollback();
                if (isUniqueConstraintViolation(e)) {
                    return Optional.empty();
                }
                throw e;
            }
        }
    }

    private static boolean isUniqueConstraintViolation(Throwable exception) {
        while (exception != null) {
            if (exception instanceof SQLException sqlException
                    && "23505".equals(sqlException.getSQLState())) {
                return true;
            }
            exception = exception.getCause();
        }
        return false;
    }

    public Optional<User> findByLoginAndPassword(String login, String password) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from User where login = :login and password = :password",
                            User.class)
                    .setParameter("login", login)
                    .setParameter("password", password)
                    .uniqueResultOptional();
        }
    }

    public Optional<User> findById(int id) {
        try (Session session = sessionFactory.openSession()) {
            User user = session.get(User.class, id);
            return Optional.ofNullable(user);
        }
    }
}
