package edu.trincoll.service;

import edu.trincoll.functional.TaskPredicate;
import edu.trincoll.model.Task;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import edu.trincoll.functional.TaskPredicate;
import edu.trincoll.model.Task;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TaskAnalyzer {
    private final List<Task> tasks;

    public TaskAnalyzer(List<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }

    // TODO: Implement using streams and filter
    public List<Task> filterTasks(Predicate<Task> predicate) {
        return tasks.stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

    // TODO: Implement using Optional
    public Optional<Task> findTaskById(Long id) {
        return tasks.stream()
                .filter(task -> task.id().equals(id))
                .findFirst();
    }

    // TODO: Implement using streams, sorted, and limit
    public List<Task> getTopPriorityTasks(int limit) {
        return tasks.stream()
                .sorted(Comparator.comparing(Task::priority,
                        Comparator.comparing(Task.Priority::getWeight).reversed()))
                .limit(limit)
                .collect(Collectors.toList());
    }

    // TODO: Implement using streams and groupingBy
    public Map<Task.Status, List<Task>> groupByStatus() {
        return tasks.stream()
                .collect(Collectors.groupingBy(Task::status));
    }

    // TODO: Implement using streams and partitioningBy
    public Map<Boolean, List<Task>> partitionByOverdue() {
        return tasks.stream()
                .collect(Collectors.partitioningBy(Task::isOverdue));
    }

    // TODO: Implement using streams, map, and collect
    public Set<String> getAllUniqueTags() {
        return tasks.stream()
                .flatMap(task -> task.tags().stream())
                .collect(Collectors.toSet());
    }

    // TODO: Implement using streams and reduce
    public Optional<Integer> getTotalEstimatedHours() {
        return tasks.stream()
                .map(Task::estimatedHours)
                .filter(Objects::nonNull)
                .reduce(Integer::sum);
    }

    // TODO: Implement using streams, map, and average
    public OptionalDouble getAverageEstimatedHours() {
        return tasks.stream()
                .map(Task::estimatedHours)
                .filter(Objects::nonNull)
                .mapToInt(Integer::intValue)
                .average();
    }

    // TODO: Implement using method references and map
    public List<String> getTaskTitles() {
        return tasks.stream()
                .map(Task::title)
                .collect(Collectors.toList());
    }

    // TODO: Implement using custom functional interface
    public List<Task> filterWithCustomPredicate(TaskPredicate predicate) {
        return tasks.stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

    // TODO: Implement using streams and flatMap
    public List<String> getAllTagsSorted() {
        return tasks.stream()
                .flatMap(task -> task.tags().stream())
                .sorted()
                .collect(Collectors.toList());
    }

    // TODO: Implement using streams and counting collector
    public Map<Task.Priority, Long> countTasksByPriority() {
        return tasks.stream()
                .collect(Collectors.groupingBy(
                        Task::priority,
                        Collectors.counting()
                ));
    }

    // TODO: Implement using Optional operations
    public String getTaskSummary(Long taskId) {
        return findTaskById(taskId)
                .map(task -> String.format("%s - %s", task.title(), task.status()))
                .orElse("Task not found");
    }

    // TODO: Implement using streams and anyMatch
    public boolean hasOverdueTasks() {
        return tasks.stream()
                .anyMatch(Task::isOverdue);
    }

    // TODO: Implement using streams and allMatch
    public boolean areAllTasksAssigned() {
        return tasks.stream()
                .allMatch(Task::isActive);
    }
}