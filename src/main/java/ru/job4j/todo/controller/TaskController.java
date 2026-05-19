package ru.job4j.todo.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.service.TaskService;

import java.util.Optional;

@Controller
@AllArgsConstructor
@Slf4j
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    public String getAllTasks(Model model) {
        model.addAttribute("tasks", taskService.findAll());
        return "tasks/list";
    }

    @GetMapping("/done")
    public String getDoneTasks(Model model) {
        model.addAttribute("tasks", taskService.findAllDone());
        return "tasks/list";
    }

    @GetMapping("/active")
    public String getActiveTasks(Model model) {
        model.addAttribute("tasks", taskService.findAllActive());
        return "tasks/list";
    }

    @GetMapping("/{id}")
    public String getTaskById(@PathVariable int id, Model model) {
        Optional<Task> taskOptional = taskService.findById(id);
        if (taskOptional.isEmpty()) {
            model.addAttribute("error", "Задача не найдена.");
            return "error/404";
        }
        model.addAttribute("task", taskOptional.get());
        return "tasks/details";
    }

    @GetMapping("/create")
    public String getCreateForm(Model model) {
        model.addAttribute("task", new Task());
        return "tasks/create";
    }

    @PostMapping("/create")
    public String createTask(@ModelAttribute Task task, Model model) {
        try {
            Task savedTask = taskService.save(task);
            return "redirect:/tasks/" + savedTask.getId();
        } catch (Exception e) {
            model.addAttribute("error", "Не удалось создать задачу.");
            model.addAttribute("task", task);
            log.error("Failed to create task. Task data: {}", task, e);
            return "tasks/create";
        }
    }

    @GetMapping("/edit/{id}")
    public String getEditForm(@PathVariable int id, Model model) {
        Optional<Task> taskOptional = taskService.findById(id);
        if (taskOptional.isEmpty()) {
            model.addAttribute("error", "Задача не найдена.");
            return "error/404";
        }
        model.addAttribute("task", taskOptional.get());
        return "tasks/edit";
    }

    @PostMapping("/edit/{id}")
    public String editTask(@PathVariable int id, @ModelAttribute Task task, Model model) {
        try {
            task.setId(id);
            Task editedTask = taskService.update(task);
            return "redirect:/tasks/" + editedTask.getId();
        } catch (Exception e) {
            model.addAttribute("error", "Не удалось обновить задачу.");
            model.addAttribute("task", task);
            log.error("Failed to edit task. Task data: {}", task, e);
            return "tasks/edit";
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteTask(@PathVariable int id, Model model) {
        try {
            boolean isDeleted = taskService.delete(id);
            if (!isDeleted) {
                model.addAttribute("error", "Задача не найдена.");
                return "error/404";
            }
            return "redirect:/tasks";
        } catch (Exception e) {
            model.addAttribute("error", "Не удалось удалить задачу.");
            log.error("Failed to delete task. Task data: {}", id, e);
            return "error/500";
        }
    }

    @PostMapping("/markdone/{id}")
    public String markDoneTask(@PathVariable int id, Model model) {
        try {
            boolean isDone = taskService.markDone(id);
            if (!isDone) {
                model.addAttribute("error", "Задача не найдена.");
                return "error/404";
            }
            return "redirect:/tasks/" + id;
        } catch (Exception e) {
            model.addAttribute("error", "Не удалось обновить задачу.");
            log.error("Failed to mark task done. Task data: {}", id, e);
            return "error/500";
        }
    }
}
