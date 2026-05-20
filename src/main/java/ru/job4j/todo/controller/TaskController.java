package ru.job4j.todo.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.service.TaskService;

import java.util.Optional;

@Controller
@AllArgsConstructor
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
    public String createTask(@ModelAttribute Task task) {
        Task savedTask = taskService.save(task);
        return "redirect:/tasks/" + savedTask.getId();
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
        task.setId(id);
        boolean isUpdated = taskService.update(task);
        if (!isUpdated) {
            model.addAttribute("error", "Задача не найдена.");
            return "error/404";
        }
        return "redirect:/tasks/" + id;
    }

    @PostMapping("/delete/{id}")
    public String deleteTask(@PathVariable int id, Model model) {
        boolean isDeleted = taskService.delete(id);
        if (!isDeleted) {
            model.addAttribute("error", "Задача не найдена.");
            return "error/404";
        }
        return "redirect:/tasks";
    }

    @PostMapping("/markdone/{id}")
    public String markDoneTask(@PathVariable int id, Model model) {
        boolean isDone = taskService.markDone(id);
        if (!isDone) {
            model.addAttribute("error", "Задача не найдена.");
            return "error/404";
        }
        return "redirect:/tasks/" + id;
    }
}
