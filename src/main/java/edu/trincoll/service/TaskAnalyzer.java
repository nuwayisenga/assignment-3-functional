package edu.trincoll.service;

import edu.trincoll.functional.TaskPredicate;
import edu.trincoll.model.Task;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TaskAnalyzer {

    /**
     * AI Collaboration Report:
            * Tool: Gemini, claude
            * Most Helpful Prompts:
            * 2. My test was failing with 'Expected size: 11 but was: 10' for the getAllTagsSorted method. Helped with the specific issue with duplicate tags).
            * 3. helped with adding Javadoc comments to the completed Java class.".

            * Concepts Learned:
            * - Stream Pipeline: Learned to chain stream operations like filter, map, sorted, and flatMap to process collections declaratively.
            * - Collectors API: Gained experience with terminal operations using Collectors, especially for grouping (groupingBy), partitioning (partitioningBy), and data aggregation (counting, toSet, toList).
            * - Optional API: Understood how to use Optional for null-safe code, including chaining operations with .map() and providing default values with .orElse() and .orElseGet().
            * - Higher-Order Functions: Learned to implement methods that accept functions as arguments (e.g., Predicate, Function, Consumer, Supplier) to create flexible and reusable code.
            * - Function Composition: Practiced combining multiple functions and comparators to build complex logic from simple, reusable pieces (e.g., processPipeline and sortByMultipleCriteria).
            */

    private final List<Task> tasks;
    /**
     * Constructs a TaskAnalyzer with a list of tasks to analyze.
     * @param tasks The initial list of tasks.
     */
    public TaskAnalyzer(List<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }
    /**
     * Filters the list of tasks based on a given predicate.
     * @param predicate The condition to test each task against.
     * @return A new list containing only the tasks that match the predicate.
     */
    public List<Task> filterTasks(Predicate<Task> predicate) {
        return tasks.stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }
    /**
     * Finds a task by its unique ID.
     * @param id The ID of the task to find.
     * @return An Optional containing the found task, or an empty Optional if no task matches the ID.
     */
    public Optional<Task> findTaskById(Long id) {
        return tasks.stream()
                .filter(task -> task.id().equals(id))
                .findFirst();
    }
    /**
     * Gets a list of the highest priority tasks, up to a specified limit.
     * @param limit The maximum number of tasks to return.
     * @return A sorted list of the top-priority tasks.
     */
    public List<Task> getTopPriorityTasks(int limit) {
        return tasks.stream()
                .sorted(Comparator.comparing(Task::priority,
                        Comparator.comparing(Task.Priority::getWeight).reversed()))
                .limit(limit)
                .collect(Collectors.toList());
    }

    /**
     * Groups all tasks by their current status.
     * @return A Map where keys are task statuses and values are lists of tasks with that status.
     */
    public Map<Task.Status, List<Task>> groupByStatus() {
        return tasks.stream()
                .collect(Collectors.groupingBy(Task::status));
    }

    /**
     * Partitions tasks into two groups: overdue and not overdue.
     * @return A Map where the key 'true' corresponds to overdue tasks and 'false' to non-overdue tasks.
     */
    public Map<Boolean, List<Task>> partitionByOverdue() {
        return tasks.stream()
                .collect(Collectors.partitioningBy(Task::isOverdue));
    }
    /**
     * Collects all unique tags from all tasks.
     * @return A Set containing every unique tag.
     */
    public Set<String> getAllUniqueTags() {
        return tasks.stream()
                .flatMap(task -> task.tags().stream())
                .collect(Collectors.toSet());
    }
    /**
     * Calculates the total sum of estimated hours for all tasks.
     * @return An Optional containing the total hours, or an empty Optional if there are no tasks.
     */
    public Optional<Integer> getTotalEstimatedHours() {
        return tasks.stream()
                .map(Task::estimatedHours)
                .filter(Objects::nonNull)
                .reduce(Integer::sum);
    }
    /**
     * Calculates the average of estimated hours for all tasks.
     * @return An OptionalDouble containing the average, or an empty OptionalDouble if there are no tasks.
     */
    public OptionalDouble getAverageEstimatedHours() {
        return tasks.stream()
                .map(Task::estimatedHours)
                .filter(Objects::nonNull)
                .mapToInt(Integer::intValue)
                .average();
    }
    /**
     * Extracts a list of all task titles.
     * @return A List of strings, where each string is a task title.
     */
    public List<String> getTaskTitles() {
        return tasks.stream()
                .map(Task::title)
                .collect(Collectors.toList());
    }
    /**
     * Filters tasks using a custom functional interface.
     * @param predicate The custom predicate to apply.
     * @return A new list containing only the tasks that match the predicate.
     */
    public List<Task> filterWithCustomPredicate(TaskPredicate predicate) {
        return tasks.stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }
    /**
     * Collects all tags from all tasks (including duplicates) and sorts them alphabetically.
     * @return A sorted List of all tags.
     */
    public List<String> getAllTagsSorted() {
        return tasks.stream()
                .flatMap(task -> task.tags().stream())
                .sorted()
                .collect(Collectors.toList());
    }
    /**
     * Counts the number of tasks for each priority level.
     * @return A Map where keys are priorities and values are the count of tasks with that priority.
     */
    public Map<Task.Priority, Long> countTasksByPriority() {
        return tasks.stream()
                .collect(Collectors.groupingBy(
                        Task::priority,
                        Collectors.counting()
                ));
    }
    /**
     * Generates a brief summary string for a specific task.
     * @param taskId The ID of the task to summarize.
     * @return A formatted string with the task's title and status, or a "not found" message.
     */
    public String getTaskSummary(Long taskId) {
        return findTaskById(taskId)
                .map(task -> String.format("%s - %s", task.title(), task.status()))
                .orElse("Task not found");
    }
    /**
     * Checks if there are any overdue tasks in the list.
     * @return true if at least one task is overdue, false otherwise.
     */
    public boolean hasOverdueTasks() {
        return tasks.stream()
                .anyMatch(Task::isOverdue);
    }
    /**
     * Checks if all active tasks have an assignee.
     * @return true if all tasks IN_PROGRESS state are assigned or not done, false otherwise.
     */
    public boolean areAllTasksAssigned() {
        return tasks.stream()
                .allMatch(Task::isActive);
    }
}