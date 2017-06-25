package com.tasky.android.storage;

import android.provider.BaseColumns;

/**
 * Defines the contract of the database used by the Tasky application.
 */
public final class TaskyContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private TaskyContract() {}

    /**
     * Contract of the Task table.
     */
    public static class Task implements BaseColumns {
        public static final String TABLE_NAME = "task";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_CREATED_ON = "createdOn";
        public static final String COLUMN_NAME_DONE_ON = "doneOn";
        public static final String COLUMN_NAME_PRIORITY = "priority";
        public static final String COLUMN_NAME_POSTPONED_UNTIL = "postponedUntil";
        public static final String COLUMN_NAME_CREATED_FROM_RECURRING_TASK_ID = "createdFromRecurringTaskId";
        public static final String COLUMN_NAME_DUE_DATE = "dueDate";
    }

    /**
     * Contract of the RecurringTask table.
     */
    public static class RecurringTask implements BaseColumns {
        public static final String TABLE_NAME = "recurringTask";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_START_DATE = "startDate";
        public static final String COLUMN_NAME_END_DATE = "endDate";
        public static final String COLUMN_NAME_INTERVAL = "interval";
        public static final String COLUMN_NAME_INTERVAL_UNIT = "intervalUnit";
    }
}