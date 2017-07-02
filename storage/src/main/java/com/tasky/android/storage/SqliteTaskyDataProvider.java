package com.tasky.android.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.tasky.android.entities.Task;
import com.tasky.android.entities.TaskPriority;
import com.tasky.android.storage.queries.QueryFilter;
import com.tasky.android.utilities.ParameterCheck;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

        // Insert new task and put generated Id into the task object.
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long id = db.insert(TaskyContract.Task.TABLE_NAME, null, createContentValuesFromTask(task));
        task.setId(id);
    }

    /**
     * Queries the stored task filtered by the specified conditions.
     * @param filter Filter that should be applied to exclude elements from the result.
     * @return A list of the tasks from the storage that match the filter.
     */
    @Override
    public List<Task> queryTasks(QueryFilter filter) {
        ParameterCheck.notNull(filter, "filter");

        List<String> params = filter.getSqliteParameters();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(TaskyContract.Task.TABLE_NAME, null,
                filter.buildSqliteWhereClause(),
                params.toArray(new String[params.size()]), null, null, null);

        List<Task> tasks = new ArrayList();
        Map<String, Integer> indices = SqliteTools.getColumnIndices(cursor);
        while(cursor.moveToNext()) {
            tasks.add(readTaskFromCursor(cursor, indices));
        }
        cursor.close();
        return tasks;
    }

    /**
     * Updates the task in the storage to match the current state of the specified entity.
     * @param task Task with the state that should be stored; will be identified by the Id.
     */
    @Override
    public void updateTask(Task task) {
        ParameterCheck.notNull(task, "task");

        // Update task with the current data, matching it by its Id.
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.update(TaskyContract.Task.TABLE_NAME, createContentValuesFromTask(task),
                TaskyContract.Task._ID + "=?", new String[] { String.valueOf(task.getId()) });
    }

    private ContentValues createContentValuesFromTask(Task task) {
        ContentValues values = new ContentValues();
        values.put(TaskyContract.Task.COLUMN_NAME_TITLE, task.getTitle());
        values.put(TaskyContract.Task.COLUMN_NAME_CREATED_ON, SqliteTools.convertDateTime(task.getCreatedOn()));
        values.put(TaskyContract.Task.COLUMN_NAME_DONE_ON, SqliteTools.convertDateTime(task.getDoneOn()));
        values.put(TaskyContract.Task.COLUMN_NAME_PRIORITY, SqliteTools.convertEnum(task.getPriority()));
        values.put(TaskyContract.Task.COLUMN_NAME_POSTPONED_UNTIL, SqliteTools.convertDateTime(task.getPostponedUntil()));
        values.put(TaskyContract.Task.COLUMN_NAME_CREATED_FROM_RECURRING_TASK_ID, task.getCreatedFromRecurringTaskId());
        values.put(TaskyContract.Task.COLUMN_NAME_DUE_DATE, SqliteTools.convertDateTime(task.getDueDate()));
        return values;
    }

    private Task readTaskFromCursor(Cursor cursor, Map<String, Integer> indices) {
        Task task = new Task();
        task.setId(cursor.getLong(indices.get(TaskyContract.Task._ID)));
        task.setTitle(cursor.getString(indices.get(TaskyContract.Task.COLUMN_NAME_TITLE)));
        task.setCreatedOn(SqliteTools.getDateTime(cursor, indices.get(TaskyContract.Task.COLUMN_NAME_CREATED_ON)));
        task.setDoneOn(SqliteTools.getDateTime(cursor, indices.get(TaskyContract.Task.COLUMN_NAME_DONE_ON)));
        task.setPriority(SqliteTools.<TaskPriority>getEnum(cursor, indices.get(TaskyContract.Task.COLUMN_NAME_PRIORITY), TaskPriority.class));
        task.setPostponedUntil(SqliteTools.getDateTime(cursor, indices.get(TaskyContract.Task.COLUMN_NAME_POSTPONED_UNTIL)));
        task.setCreatedFromRecurringTaskId(SqliteTools.getNullableInt(cursor, indices.get(TaskyContract.Task.COLUMN_NAME_CREATED_FROM_RECURRING_TASK_ID)));
        task.setDueDate(SqliteTools.getDateTime(cursor, indices.get(TaskyContract.Task.COLUMN_NAME_DUE_DATE)));
        return task;
    }
}