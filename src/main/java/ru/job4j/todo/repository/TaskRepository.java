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
        return crudRepository.query("FROM Task ORDER BY created DESC", Task.class);
    }

    public List<Task> findAllDone() {
        return crudRepository.query("FROM Task WHERE done IS TRUE ORDER BY created DESC", Task.class);
    }

    public List<Task> findAllActive() {
        return crudRepository.query("FROM Task WHERE done IS FALSE ORDER BY created DESC", Task.class);
    }

    public Optional<Task> findById(int id) {
        return crudRepository.optional("FROM Task WHERE id = :id", Task.class,
                Map.of("id", id));
    }

    public Task save(Task task) {
        crudRepository.run(session -> session.persist(task));
        return task;
    }

    public boolean update(Task task) {
        int affectedRows = crudRepository.affectedRowsQuery(
                """
                         UPDATE Task
                         SET title = :title, description = :description
                         WHERE id = :id
                        """,
                Map.of("title", task.getTitle(),
                        "description", task.getDescription(), "id", task.getId())
        );
        return affectedRows > 0;
    }

    public boolean delete(int id) {
        int affectedRows = crudRepository.affectedRowsQuery(
                "DELETE FROM Task WHERE id = :id",
                Map.of("id", id)
        );
        return affectedRows > 0;
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
