package com.tasky.android.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.tasky.android.entities.Task;

import org.joda.time.DateTime;

/**
 * Provides access to an Sqlite database holding Tasky application data.
 */
public class SqliteTaskyDataProvider implements TaskyDataProvider {
    private final TaskyDbHelper dbHelper;

    /**
     * Creates an SqliteTaskyDataProvider.
     * @param context Context used by the DbHelper to access the Sqlite database.
     */
    public SqliteTaskyDataProvider(Context context) {
        dbHelper = new TaskyDbHelper(context);
    }

    /**
     * Inserts a new task into the storage. On insertion, a new Id
     * will be generated and assigned to the Id property of the task.
     * @param task Task that should be inserted into the storage.
     */
    @Override
    public void insertTask(Task task) {
        if (task == null) throw new IllegalArgumentException("Parameter task cannot be null.");

        // Fill values to use for insert operation.
        ContentValues values = new ContentValues();
        values.put(TaskyContract.Task.COLUMN_NAME_TITLE, task.getTitle());
        values.put(TaskyContract.Task.COLUMN_NAME_DONE, task.isDone());
        values.put(TaskyContract.Task.COLUMN_NAME_POSTPONED_UNTIL, convertDateTime(task.getPostponedUntil()));
        values.put(TaskyContract.Task.COLUMN_NAME_CREATED_FROM_RECURRING_TASK_ID, task.getCreatedFromRecurringTaskId());
        values.put(TaskyContract.Task.COLUMN_NAME_DUE_DATE, convertDateTime(task.getDueDate()));

        // Insert new task and put generated Id into the task object.
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long id = db.insert(TaskyContract.Task.TABLE_NAME, null, values);
        task.setId(id);
    }

    /**
     * Converts a Joda DateTime value to a value appropriate for storage in an Sqlite database.
     * @param value Joda DateTime value to store; can be null.
     * @return A value representing the Joda DateTime that can be stored in an Sqlite database.
     */
    private Long convertDateTime(DateTime value) {
        if (value == null) return null;
        return value.getMillis();
    }
}