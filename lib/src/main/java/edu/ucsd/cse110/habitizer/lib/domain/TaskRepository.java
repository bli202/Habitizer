package edu.ucsd.cse110.habitizer.lib.domain;

import java.util.List;

import edu.ucsd.cse110.habitizer.lib.data.InMemoryDataSource;
import edu.ucsd.cse110.observables.Subject;

public class TaskRepository {
    private final InMemoryDataSource dataSource;

    public TaskRepository(InMemoryDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Integer count() {
        return dataSource.getTasks().size();
    }

    public Subject<Task> find(String name) {
        return dataSource.getTaskSubject(name);
    }

    public Subject<List<Task>> findAll() {
        return dataSource.getAllTasksSubject();
    }

    public void save(Task task) {
        dataSource.putTask(task);
    }
}
