package com.tasky.android.logic;

import com.tasky.android.entities.Task;
import com.tasky.android.storage.TaskyContract;
import com.tasky.android.storage.TaskyDataProvider;
import com.tasky.android.storage.queries.QueryFilter;
import com.tasky.android.storage.queries.ValueQueryFilter;
import com.tasky.android.utilities.ParameterCheck;

import org.joda.time.DateTime;

import java.util.List;


/**
 * Provides methods handling use-cases for task management
 * operating on a TaskyDataProvider.
 */
public final class PersistentTaskManager implements TaskManager {
    private final TaskyDataProvider dataprovider;

    /**
     * Creates a PersistentTaskManager working with the specified dataprovider.
     * @param dataprovider TaskyDataProvider on which to operate.
     */
    public PersistentTaskManager(TaskyDataProvider dataprovider) {
        ParameterCheck.NotNull(dataprovider, "dataprovider");
        this.dataprovider = dataprovider;
    }

    /**
     * Creates a new task.
     * @param title Title of the task.
     * @param dueDate Indicates when the task is due; or null if it is immediately due.
     * @return The created task.
     */
    @Override
    public Task createTask(String title, DateTime dueDate) {
        Task task = new Task();
        task.setTitle(title);
        task.setDueDate(dueDate);

        dataprovider.insertTask(task);
        return task;
    }

    /**
     * Sets the specified task to done.
     * @param id Id of the task that should be done.
     */
    @Override
    public void setTaskDone(long id) {
        Task task = getTaskById(id);
        task.setDone(true);
        dataprovider.updateTask(task);
    }

    /**
     * Gets all tasks that are relevant to display to the user.
     * @return A list with all relevant tasks.
     */
    @Override
    public List<Task> getRelevantTasks() {
        // Check due date and postponeduntil.
        // How to deal with ValueQueryFilters comparing DateTimes?
        return dataprovider.queryTasks(
            new ValueQueryFilter(TaskyContract.Task.COLUMN_NAME_DONE, ValueQueryFilter.Type.Equals, false));
    }

    /**
     * Gets a task by its Id.
     * @param id Id of the task to retreive from the dataprovider.
     * @return The task with the specified Id.
     */
    private Task getTaskById(long id) {
        QueryFilter filter = new ValueQueryFilter(TaskyContract.Task._ID, ValueQueryFilter.Type.Equals, id);
        return dataprovider.queryTasks(filter).get(0);
    }
}