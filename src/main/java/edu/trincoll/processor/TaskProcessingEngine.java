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

    // TODO: Implement pipeline processing using Function composition
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

    // TODO: Implement using Supplier for lazy evaluation
    // Get task from Optional or create default using Supplier (lazy evaluation)
    public Task getOrCreateDefault(Optional<Task> taskOpt, Supplier<Task> defaultSupplier) {
        return taskOpt.orElseGet(defaultSupplier);
    }

    // TODO: Implement using Consumer for side effects
    // Process tasks with side effects using Consumer
    public void processTasksWithSideEffects(
            List<Task> tasks,
            Consumer<Task> sideEffect) {
        tasks.forEach(sideEffect);
    }

    // TODO: Implement using BiFunction
    // Merge two tasks using BiFunction
    public Task mergeTasks(Task task1, Task task2, BiFunction<Task, Task, Task> merger) {
        return merger.apply(task1, task2);
    }

    // TODO: Implement using UnaryOperator
    // Transform all tasks using UnaryOperator
    public List<Task> transformAll(List<Task> tasks, UnaryOperator<Task> transformer) {
        return tasks.stream()
                .map(transformer)
                .collect(Collectors.toList());
    }

    // TODO: Implement using custom functional interfaces
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

    // TODO: Implement batch processing with TaskProcessor
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

    // TODO: Implement Optional chaining
    // Get the title of the highest priority task using Optional chaining
    public Optional<String> getHighestPriorityTaskTitle(List<Task> tasks) {
        return tasks.stream()
                .max(Comparator.comparing(Task::priority,
                        Comparator.comparing(Task.Priority::getWeight)))
                .map(Task::title);
    }

    // TODO: Implement stream generation using Stream.generate
    // Generate an infinite stream of tasks using Stream.generate
    public Stream<Task> generateTaskStream(Supplier<Task> taskSupplier) {
        return Stream.generate(taskSupplier);
    }

    // TODO: Implement using Comparator composition
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