package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Task;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class TaskRepository {

    private final SessionFactory sessionFactory;

    public List<Task> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from Task", Task.class).list();
        }
    }

    public List<Task> findAllDone() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from Task where done is true", Task.class).list();
        }
    }

    public List<Task> findAllActive() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from Task where done is false", Task.class).list();
        }
    }

    public Optional<Task> findById(int id) {
        try (Session session = sessionFactory.openSession()) {
            Task task = session.get(Task.class, id);
            return Optional.ofNullable(task);
        }
    }

    public Task save(Task task) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.persist(task);
                transaction.commit();
                return task;
            } catch (HibernateException e) {
                transaction.rollback();
                throw e;
            }
        }
    }

    public boolean update(Task task) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                int affectedRows = session.createMutationQuery(
                                """
                                         update Task
                                         set title = :title, description = :description
                                         where id = :id
                                        """)
                        .setParameter("title", task.getTitle())
                        .setParameter("description", task.getDescription())
                        .setParameter("id", task.getId())
                        .executeUpdate();
                transaction.commit();
                return affectedRows > 0;
            } catch (HibernateException e) {
                transaction.rollback();
                throw e;
            }
        }
    }

    public boolean delete(int id) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                int affectedRows = session.createMutationQuery("delete from Task where id = :id")
                        .setParameter("id", id)
                        .executeUpdate();
                transaction.commit();
                return affectedRows > 0;
            } catch (HibernateException e) {
                transaction.rollback();
                throw e;
            }
        }
    }

    public boolean markDone(int id) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                int affectedRows = session.createMutationQuery("update Task set done = true where id = :id")
                        .setParameter("id", id)
                        .executeUpdate();
                transaction.commit();
                return affectedRows > 0;
            } catch (HibernateException e) {
                transaction.rollback();
                throw e;
            }
        }
    }
}
