package edu.trincoll.processor;

import edu.trincoll.functional.TaskPredicate;
import edu.trincoll.functional.TaskProcessor;
import edu.trincoll.functional.TaskTransformer;
import edu.trincoll.model.Task;

import java.util.*;
import java.util.function.*;
import java.util.stream.Stream;
import edu.trincoll.functional.TaskPredicate;
import edu.trincoll.functional.TaskProcessor;
import edu.trincoll.functional.TaskTransformer;
import edu.trincoll.model.Task;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TaskProcessingEngine {

    /**
     * Processes a list of tasks by applying a sequence of operations in a pipeline.
     * @param tasks The initial list of tasks to process.
     * @param operations A list of functions, each representing a step in the processing pipeline.
     * @return A new list of tasks after all operations have been applied.
     */
    // Process tasks through a pipeline of operations using function composition
    public List<Task> processPipeline(
            List<Task> tasks,
            List<Function<List<Task>, List<Task>>> operations) {

        // Start with the identity function (returns input unchanged)
        Function<List<Task>, List<Task>> pipeline = Function.identity();

        // Compose all operations into a single function
        for (Function<List<Task>, List<Task>> operation : operations) {
            pipeline = pipeline.andThen(operation);
        }

        // Apply the composed pipeline to the tasks
        return pipeline.apply(tasks);
    }

    /**
     * Returns the task from an Optional if present, otherwise returns a default task provided by a Supplier.
     * The Supplier is only invoked if the Optional is empty (lazy evaluation).
     * @param taskOpt An Optional that may contain a task.
     * @param defaultSupplier A Supplier that provides a default task.
     * @return The task from the Optional or the default task.
     */
    // Get task from Optional or create default using Supplier (lazy evaluation)
    public Task getOrCreateDefault(Optional<Task> taskOpt, Supplier<Task> defaultSupplier) {
        return taskOpt.orElseGet(defaultSupplier);
    }

    /**
     * Applies an action with side effects (e.g., logging, saving to DB) to each task in a list.
     * @param tasks The list of tasks to process.
     * @param sideEffect A Consumer that defines the action to be performed on each task.
     */
    // Process tasks with side effects using Consumer
    public void processTasksWithSideEffects(
            List<Task> tasks,
            Consumer<Task> sideEffect) {
        tasks.forEach(sideEffect);
    }

    /**
     * Merges two tasks into a single task using a provided merging strategy.
     * @param task1 The first task.
     * @param task2 The second task.
     * @param merger A BiFunction that defines how to combine the two tasks.
     * @return The resulting merged task.
     */
    // Merge two tasks using BiFunction
    public Task mergeTasks(Task task1, Task task2, BiFunction<Task, Task, Task> merger) {
        return merger.apply(task1, task2);
    }

    /**
     * Applies a uniform transformation to every task in a list.
     * @param tasks The list of tasks to transform.
     * @param transformer A UnaryOperator that defines the transformation.
     * @return A new list containing the transformed tasks.
     */
    // Transform all tasks using UnaryOperator
    public List<Task> transformAll(List<Task> tasks, UnaryOperator<Task> transformer) {
        return tasks.stream()
                .map(transformer)
                .collect(Collectors.toList());
    }

    /**
     * Filters a list of tasks and then applies a transformation to the filtered results.
     * @param tasks The initial list of tasks.
     * @param filter A TaskPredicate to select which tasks to transform.
     * @param transformer A TaskTransformer to apply to the filtered tasks.
     * @return A new list containing the transformed tasks that passed the filter.
     */
    // Filter and transform tasks using custom functional interfaces
    public List<Task> filterAndTransform(
            List<Task> tasks,
            TaskPredicate filter,
            TaskTransformer transformer) {
        return tasks.stream()
                .filter(filter)
                .map(transformer)
                .collect(Collectors.toList());
    }
    /**
     * Processes a list of tasks in smaller batches.
     * @param tasks The full list of tasks to process.
     * @param batchSize The size of each batch.
     * @param processor The TaskProcessor that defines the action to perform on each batch.
     */
    public void batchProcess(
            List<Task> tasks,
            int batchSize,
            TaskProcessor processor) {

        // Process tasks in batches of the specified size
        for (int i = 0; i < tasks.size(); i += batchSize) {
            int endIndex = Math.min(i + batchSize, tasks.size());
            List<Task> batch = tasks.subList(i, endIndex);
            processor.process(batch);
        }
    }

    /**
     * Finds the task with the highest priority and returns its title.
     * @param tasks The list of tasks to search.
     * @return An Optional containing the title of the highest-priority task, or an empty Optional if the list is empty.
     */
    // Get the title of the highest priority task using Optional chaining
    public Optional<String> getHighestPriorityTaskTitle(List<Task> tasks) {
        return tasks.stream()
                .max(Comparator.comparing(Task::priority,
                        Comparator.comparing(Task.Priority::getWeight)))
                .map(Task::title);
    }

    /**
     * Generates an infinite stream of tasks using a Supplier.
     * @param taskSupplier A Supplier that creates new task objects.
     * @return An infinite Stream of tasks.
     */
    public Stream<Task> generateTaskStream(Supplier<Task> taskSupplier) {
        return Stream.generate(taskSupplier);
    }

    /**
     * Sorts a list of tasks based on a sequence of multiple criteria.
     * @param tasks The list of tasks to sort.
     * @param comparators A list of comparators defining the sorting order. The first comparator is primary, the second is secondary, and so on.
     * @return A new list of tasks sorted by the combined criteria.
     */
    public List<Task> sortByMultipleCriteria(
            List<Task> tasks,
            List<Comparator<Task>> comparators) {

        if (comparators.isEmpty()) {
            return new ArrayList<>(tasks);
        }

        // Start with the first comparator
        Comparator<Task> combinedComparator = comparators.get(0);

        // Chain all subsequent comparators using thenComparing
        for (int i = 1; i < comparators.size(); i++) {
            combinedComparator = combinedComparator.thenComparing(comparators.get(i));
        }

        // Sort the tasks using the combined comparator
        return tasks.stream()
                .sorted(combinedComparator)
                .collect(Collectors.toList());
    }
}