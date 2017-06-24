package com.tasky.android.logic;

import com.tasky.android.entities.Task;

import org.joda.time.DateTime;

import java.util.List;

/**
 * Provides methods handling use-cases for task management.
 */
public interface TaskManager {
    /**
     * Creates a new task.
     * @param title Title of the task.
     * @param dueDate Indicates when the task is due; or null if it is immediately due.
     * @return The created task.
     */
    Task createTask(String title, DateTime dueDate);

    /**
     * Sets the specified task to done.
     * @param id Id of the task that should be done.
     */
    void setTaskDone(long id);

    /**
     * Reverts the done-state of the specified task.
     * @param id Id of the task that should not be done anymore.
     */
    void revertTaskDone(long id);

    /**
     * Gets all tasks that are relevant to display to the user.
     * @return A list with all relevant tasks.
     */
    List<Task> getRelevantTasks();
}