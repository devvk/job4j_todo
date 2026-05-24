package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Task;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class TaskRepository {

    private final CrudRepository crudRepository;

    public List<Task> findAll() {
        return crudRepository.query("""
                SELECT DISTINCT t
                FROM Task t
                LEFT JOIN FETCH t.priority
                LEFT JOIN FETCH t.user
                LEFT JOIN FETCH t.categories
                ORDER BY t.created DESC
                """, Task.class);
    }

    public List<Task> findAllDone() {
        return crudRepository.query("""
                SELECT DISTINCT t
                FROM Task t
                LEFT JOIN FETCH t.priority
                LEFT JOIN FETCH t.user
                LEFT JOIN FETCH t.categories
                WHERE t.done IS TRUE
                ORDER BY t.created DESC
                """, Task.class);
    }

    public List<Task> findAllActive() {
        return crudRepository.query("""
                SELECT DISTINCT t
                FROM Task t
                LEFT JOIN FETCH t.priority
                LEFT JOIN FETCH t.user
                LEFT JOIN FETCH t.categories
                WHERE t.done IS FALSE
                ORDER BY t.created DESC
                """, Task.class);
    }

    public Optional<Task> findById(int id) {
        return crudRepository.optional("""
                        SELECT DISTINCT t
                        FROM Task t
                        LEFT JOIN FETCH t.priority
                        LEFT JOIN FETCH t.user
                        LEFT JOIN FETCH t.categories
                        WHERE t.id = :id
                        """, Task.class,
                Map.of("id", id));
    }

    public Task save(Task task) {
        crudRepository.run(session -> session.persist(task));
        return task;
    }

    public boolean update(Task task) {
        Optional<Task> foundTaskOptional = findById(task.getId());
        if (foundTaskOptional.isEmpty()) {
            return false;
        }

        Task foundTask = foundTaskOptional.get();
        foundTask.setTitle(task.getTitle());
        foundTask.setDescription(task.getDescription());
        foundTask.setPriority(task.getPriority());
        foundTask.setDone(task.isDone());
        foundTask.setCategories(task.getCategories());
        crudRepository.run(session -> session.merge(foundTask));
        return true;
    }

    public boolean delete(int id) {
        Optional<Task> foundTaskOptional = findById(id);
        if (foundTaskOptional.isEmpty()) {
            return false;
        }
        crudRepository.run(session -> session.remove(foundTaskOptional.get()));
        return true;
    }

    public boolean markDone(int id) {
        int affectedRows = crudRepository.affectedRowsQuery(
                """
                        UPDATE Task
                        SET done = TRUE
                        WHERE id = :id
                        """,
                Map.of("id", id)
        );
        return affectedRows > 0;
    }
}
