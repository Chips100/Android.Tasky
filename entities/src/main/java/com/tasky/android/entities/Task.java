package com.tasky.android.entities;

import org.joda.time.DateTime;

/**
 * Represents a task that the user wants to do.
 */
public final class Task extends TaskyEntityBase {
    private String title;
    private boolean done;
    private DateTime postponedUntil;
    private Integer createdFromRecurringTaskId;
    private DateTime dueDate;

    /**
     * Gets the title of this Task.
     * @return The title of this Task.
     */
    public String getTitle() { return title; }

    /**
     * Sets the title of this Task.
     * @param value The title that should be set.
     */
    public void setTitle(String value) { title = value; }


    /**
     * Indicates if this Task has been done.
     * @return True, if this Task has been done; otherwise false.
     */
    public boolean isDone() { return done; }

    /**
     * Sets if this Task has been done.
     * @param value True, if this Task has been done; otherwise false.
     */
    public void setDone(boolean value) { done = value; }


    /**
     * Gets the date until which this Task has been postponed.
     * @return The date until which this Task has been postponed.
     */
    public DateTime getPostponedUntil() { return postponedUntil; }

    /**
     * Sets the date until which this Task should be postponed.
     * @param value The date until which this Task should be postponed.
     */
    public void setPostponedUntil(DateTime value) { postponedUntil = value; }


    /**
     * Gets the Id of the RecurringTask from which this Task was created.
     * @return The Id of the RecurringTask from which this Task has been created;
     * or null if it has not been created by a RecurringTask.
     */
    public Integer getCreatedFromRecurringTaskId() { return createdFromRecurringTaskId; }

    /**
     * Sets the Id of the RecurringTask from which this Task was created.
     * @param value The Id of the RecurringTask from which this Task has been created;
     * or null if it has not been created by a RecurringTask.
     */
    public void setCreatedFromRecurringTaskId(Integer value) { createdFromRecurringTaskId = value; }


    /**
     * Gets the date on which this task is due.
     * @return The date on which this task is due; or null if it is immediately due.
     */
    public DateTime getDueDate() { return dueDate; }

    /**
     * Sets the date on which this task is due.
     * @param value The date on which this task should be due; or null if should be immediately due.
     */
    public void setDueDate(DateTime value) { dueDate = value; }
}