package com.tasky.android.entities;

/**
 * Represents a priority of a task.
 */
public enum TaskPriority {
    Low(3),
    Normal(2),
    High(1);

    private final int sortIndex;

    TaskPriority(int sortIndex) {
        this.sortIndex = sortIndex;
    }

    /**
     * Gets an integer representing the index at which this
     * priority should be sorted if a list is sorted by the priority.
     * @return The index at which this priority should be sorted.
     */
    public int getSortIndex() { return sortIndex; }
}