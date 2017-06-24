package com.tasky.android.logic;

import com.tasky.android.entities.Task;
import com.tasky.android.storage.TaskyContract;
import com.tasky.android.storage.TaskyDataProvider;
import com.tasky.android.storage.queries.QueryFilter;
import com.tasky.android.storage.queries.QueryFilterBase;
import com.tasky.android.storage.queries.QueryFilterFactory;
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
        task.setCreatedOn(DateTime.now());

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
        task.setDoneOn(DateTime.now());
        dataprovider.updateTask(task);
    }

    /**
     * Reverts the done-state of the specified task.
     * @param id Id of the task that should not be done anymore.
     */
    @Override
    public void revertTaskDone(long id) {
        Task task = getTaskById(id);
        task.setDoneOn(null);
        dataprovider.updateTask(task);
    }

    /**
     * Gets all tasks that are relevant to display to the user.
     * @return A list with all relevant tasks.
     */
    @Override
    public List<Task> getRelevantTasks() {
        // Relevant tasks include only tasks which...
        // ... are not done.
        QueryFilterBase doneFilter = new ValueQueryFilter(TaskyContract.Task.COLUMN_NAME_DONE_ON, ValueQueryFilter.Type.IsNull, null);
        // ... are due.
        QueryFilter dueDateFilter = QueryFilterFactory.smallerThanOrNull(
                TaskyContract.Task.COLUMN_NAME_DUE_DATE, DateTime.now());
        // ... are not postponed until later than now.
        QueryFilter postponedUntilFilter = QueryFilterFactory.smallerThanOrNull(
                TaskyContract.Task.COLUMN_NAME_POSTPONED_UNTIL, DateTime.now());

        return dataprovider.queryTasks(doneFilter.And(dueDateFilter).And(postponedUntilFilter));
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