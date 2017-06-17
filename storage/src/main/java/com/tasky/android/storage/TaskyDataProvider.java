package com.tasky.android.storage;

import com.tasky.android.entities.Task;
import com.tasky.android.storage.queries.QueryFilter;

import java.util.List;

/**
 * Provides access to a storage holding Tasky application data.
 */
public interface TaskyDataProvider {
    /**
     * Inserts a new task into the storage. On insertion, a new Id
     * will be generated and assigned to the Id property of the task.
     * @param task Task that should be inserted into the storage.
     */
    void insertTask(Task task);

    /**
     * Queries the stored task filtered by the specified conditions.
     * @param filter Filter that should be applied to exclude elements from the result.
     * @return A list of the tasks from the storage that match the filter.
     */
    List<Task> queryTasks(QueryFilter filter);
}