package ru.job4j.todo.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.repository.TaskRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    public List<Task> findAllDone() {
        return taskRepository.findAllDone();
    }

    public List<Task> findAllActive() {
        return taskRepository.findAllActive();
    }

    public Optional<Task> findById(int id) {
        return taskRepository.findById(id);
    }

    public Task save(Task task) {
        task.setCreated(LocalDateTime.now());
        return taskRepository.save(task);
    }

    public Optional<Task> update(Task task) {
        return taskRepository.update(task);
    }

    public boolean delete(int id) {
        return taskRepository.delete(id);
    }

    public boolean markDone(int id) {
        return taskRepository.markDone(id);
    }
}
