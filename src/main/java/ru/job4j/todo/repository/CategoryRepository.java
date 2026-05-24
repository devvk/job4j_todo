package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Category;

import java.util.List;
import java.util.Map;

@Repository
@AllArgsConstructor
public class CategoryRepository {

    private final CrudRepository crudRepository;

    public List<Category> findAll() {
        return crudRepository.query("FROM Category", Category.class);
    }

    public List<Category> findByIds(List<Integer> ids) {
        return crudRepository.query("FROM Category WHERE id IN :ids", Category.class,
                Map.of("ids", ids));
    }
}
