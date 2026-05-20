package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import org.hibernate.HibernateException;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.User;

import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class UserRepository {

    private final CrudRepository crudRepository;

    public Optional<User> save(User user) {
        try {
            crudRepository.run(session -> session.persist(user));
            return Optional.of(user);
        } catch (HibernateException e) {
            if (isUniqueConstraintViolation(e)) {
                return Optional.empty();
            }
            throw e;
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
        return crudRepository.optional(
                "FROM User WHERE login = :login AND password = :password", User.class,
                Map.of("login", login,
                        "password", password)
        );
    }

    public Optional<User> findById(int id) {
        return crudRepository.optional(
                "FROM User WHERE id = :id", User.class,
                Map.of("id", id)
        );
    }
}
