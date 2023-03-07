package Ispit.TasksManagement;

import java.io.InputStream;
import java.io.OutputStream;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

class DeadlineNotValidException extends Exception{
    public String getMessage(LocalDateTime deadline){
        return String.format("The deadline %s has already passed", deadline.toString());
    }
}

interface ITask {
    String getCategory();

    int getPriority();

    LocalDateTime getDeadline();
}

class Task implements ITask{
    private String category;
    private String problemName;
    private String description;

    public Task(String category, String problemName, String description) {
        this.category = category;
        this.problemName = problemName;
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    @Override
    public int getPriority() {
        return Integer.MAX_VALUE;
    }

    @Override
    public LocalDateTime getDeadline() {
        return LocalDateTime.MAX;
    }

    @Override
    public String toString() {
        return String.format("Task{name='%s', description='%s'}", problemName, description);
    }
}

abstract class TaskDecorator implements ITask{
    ITask taskWrapper;

    public TaskDecorator(ITask taskWrapper) {
        this.taskWrapper = taskWrapper;
    }
}

class PriorityDecorator extends TaskDecorator {
    int priority;
    public PriorityDecorator(ITask taskWrapper, int priority) {
        super(taskWrapper);
        this.priority = priority;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(taskWrapper.toString(), 0, taskWrapper.toString().length() - 1);
        sb.append(", priority=").append(priority);
        sb.append("}");
        return sb.toString();
    }

    @Override
    public String getCategory() {
        return taskWrapper.getCategory();
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public LocalDateTime getDeadline() {
        return LocalDateTime.MAX;
    }
}

class DeadlineDecorator extends TaskDecorator {
    LocalDateTime deadline;
    public DeadlineDecorator(ITask taskWrapper, LocalDateTime deadline) {
        super(taskWrapper);
        this.deadline = deadline;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(taskWrapper.toString(), 0, taskWrapper.toString().length() - 1);
        sb.append(", deadline=").append(deadline.toString());
        sb.append("}");
        return sb.toString();
    }

    @Override
    public String getCategory() {
        return taskWrapper.getCategory();
    }

    @Override
    public int getPriority() {
        return Integer.MAX_VALUE;
    }

    @Override
    public LocalDateTime getDeadline() {
        return deadline;
    }
}

class TaskManager{
    Map<String,List<ITask>> tasks;
    public TaskManager() {
        tasks = new TreeMap<>();
    }

    public void readTasks(InputStream inputStream) throws DeadlineNotValidException {
        Scanner scanner = new Scanner(inputStream);

        while (scanner.hasNext()){
            String line = scanner.nextLine();
            String[] parts = line.split(",");
            String category = parts[0];
            String problemName = parts[1];
            String description = parts[2];

            tasks.putIfAbsent(category, new ArrayList<>());

            ITask task = new Task(category, problemName, description);

            if (parts.length == 4){
                if (parts[3].length() > 1){
                    LocalDateTime deadline = LocalDateTime.parse(parts[3]);
                    if (deadline.isBefore(LocalDateTime.of(2020,6,23, 23,59,58)))
                        try {
                            throw new DeadlineNotValidException();
                        }catch (DeadlineNotValidException e){
                            System.out.println(e.getMessage(deadline));
                        }


                    task = new DeadlineDecorator(task, deadline);
                }else {
                    int priority = Integer.parseInt(parts[3]);
                    task = new PriorityDecorator(task, priority);
                }
            }else if (parts.length == 5){
                LocalDateTime deadline = LocalDateTime.parse(parts[3]);
                int priority = Integer.parseInt(parts[4]);
                task = new DeadlineDecorator(task, deadline);
                task = new PriorityDecorator(task, priority);
            }

            tasks.get(category).add(task);
        }
    }


    public void printTasks(OutputStream os, boolean includePriority, boolean includeCategory) {
        if (includeCategory){
            if (includePriority){
                for (String k : tasks.keySet()){
                    System.out.println(k.toUpperCase());
                    tasks.get(k).stream().sorted(Comparator.comparing(ITask::getPriority)).forEach(System.out::println);
                }
            } else {
                for (String k : tasks.keySet()){
                    System.out.println(k.toUpperCase());
//                    tasks.get(k)
//                            .stream()
//                            .sorted(Comparator.comparing(task -> Duration.between(LocalDateTime.now(), task.getDeadline())))
//                                    .forEach(System.out::println);
                    tasks.get(k).forEach(System.out::println);
                }
            }
        }else {
            if (includePriority){
                Set<ITask> tmp = new HashSet<>();
                tasks.forEach((key1, value) -> value.forEach(System.out::println));
//                tmp.forEach(System.out::println);
            }
            else {
                for (String k : tasks.keySet()) {
                    tasks.get(k).forEach(System.out::println);
                }
            }
        }
    }
}

public class TasksManagerTest {

    public static void main(String[] args) throws DeadlineNotValidException {

        TaskManager manager = new TaskManager();

        System.out.println("Tasks reading");
        manager.readTasks(System.in);
        System.out.println("By categories with priority");
        manager.printTasks(System.out, true, true);
        System.out.println("-------------------------");
        System.out.println("By categories without priority");
        manager.printTasks(System.out, false, true);
        System.out.println("-------------------------");
        System.out.println("All tasks without priority");
        manager.printTasks(System.out, false, false);
        System.out.println("-------------------------");
        System.out.println("All tasks with priority");
        manager.printTasks(System.out, true, false);
        System.out.println("-------------------------");

    }
}
