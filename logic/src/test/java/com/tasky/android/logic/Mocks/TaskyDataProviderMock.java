package com.tasky.android.logic.Mocks;

import com.tasky.android.entities.Task;
import com.tasky.android.storage.TaskyDataProvider;
import com.tasky.android.storage.queries.QueryFilter;
import com.tasky.android.utilities.ReflectionTools;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation that mocks a TaskyDataProvider by managing an in-memory storage.
 */
public class TaskyDataProviderMock implements TaskyDataProvider {
    private final List<Task> tasks = new ArrayList();

    // Start from a big number so we can use fixed small numbers in unit tests
    // when adding to the task list directly.
    private int generateIdCursor = 10000;

    public TaskyDataProviderMock() {}

    public TaskyDataProviderMock(Task task) { tasks.add(task); }

    /**
     * Gets the underlying in-memory collection of stored tasks.
     * @return The list of stored tasks.
     */
    public List<Task> getTasks() { return tasks; }

    @Override
    public void insertTask(Task task) {
        task.setId(generateIdCursor++);
        tasks.add(cloneTask(task));
    }

    @Override
    public List<Task> queryTasks(QueryFilter filter) {
        List<Task> result = new ArrayList();
        for (Task task:tasks) {
            try{
                if (filter.evaluate(task)) {
                    result.add(cloneTask(task));
                }
            }
            catch(IllegalAccessException exception) {
                // Do nothing, should not occur.
                // Will cause error in unit test if relevant.
            }
        }

        return result;
    }

    @Override
    public void updateTask(Task task) {
        for (Task t: tasks) {
            if (t.getId() == task.getId()) {
                tasks.remove(t);
                tasks.add(cloneTask(task));
            }
        }
    }

    private Task cloneTask(Task task) {
        try {
            return ReflectionTools.copyFields(task, new Task());
        } catch (IllegalAccessException e) {
            // Do nothing, should not occur.
            // Will cause error in unit test if relevant.
            return null;
        }
    }
}