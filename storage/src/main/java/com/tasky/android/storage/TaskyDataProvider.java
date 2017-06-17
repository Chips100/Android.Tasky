package com.tasky.android.storage;

import com.tasky.android.entities.Task;

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
}