package com.tasky.android.storage;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;

/**
 * Provides access to the database used by the Tasky application.
 */
public class TaskyDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Tasky.db";

    public TaskyDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_RECURRING_TASKS);
        db.execSQL(SQL_CREATE_TASKS);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Do nothing yet.
    }

    /**
     * SQL-Statement that creates the Task table.
     */
    private static final String SQL_CREATE_TASKS =
        "CREATE TABLE " + TaskyContract.Task.TABLE_NAME + " (" +
            TaskyContract.Task._ID + " INTEGER PRIMARY KEY," +
            TaskyContract.Task.COLUMN_NAME_TITLE + " TEXT NOT NULL," +
            TaskyContract.Task.COLUMN_NAME_CREATED_ON + " DATE NOT NULL," +
            TaskyContract.Task.COLUMN_NAME_DONE_ON + " DATE," +
            TaskyContract.Task.COLUMN_NAME_PRIORITY + " INTEGER NOT NULL," +
            TaskyContract.Task.COLUMN_NAME_POSTPONED_UNTIL + " DATE," +
            TaskyContract.Task.COLUMN_NAME_CREATED_FROM_RECURRING_TASK_ID + " INTEGER" +
                " REFERENCES "+ TaskyContract.RecurringTask.TABLE_NAME + "(" + TaskyContract.RecurringTask._ID + ")," +
            TaskyContract.Task.COLUMN_NAME_DUE_DATE + " DATE)";

    /**
     * SQL-Statement that creates the Task table.
     */
    private static final String SQL_CREATE_RECURRING_TASKS =
        "CREATE TABLE " + TaskyContract.RecurringTask.TABLE_NAME + " (" +
            TaskyContract.RecurringTask._ID + " INTEGER PRIMARY KEY," +
            TaskyContract.RecurringTask.COLUMN_NAME_TITLE + " TEXT NOT NULL," +
            TaskyContract.RecurringTask.COLUMN_NAME_START_DATE + " DATE NOT NULL," +
            TaskyContract.RecurringTask.COLUMN_NAME_END_DATE + " DATE," +
            TaskyContract.RecurringTask.COLUMN_NAME_INTERVAL + " INTEGER NOT NULL," +
            TaskyContract.RecurringTask.COLUMN_NAME_INTERVAL_UNIT + " INTEGER NOT NULL)";
}