package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Priority;

import java.util.List;

@Repository
@AllArgsConstructor
public class PriorityRepository {

    private final CrudRepository crudRepository;

    public List<Priority> findAll() {
        return crudRepository.query("FROM Priority ORDER BY position ASC", Priority.class);
    }
}
