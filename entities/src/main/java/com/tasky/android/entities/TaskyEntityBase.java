package com.tasky.android.entities;

/**
 * Base implementation for all entities managed and stored by the Tasky application.
 */
public abstract class TaskyEntityBase {
    private long _id;

    /**
     * Gets the identifier of this entity.
     * @return the identifier of this entity.
     */
    public long getId() { return _id; }

    /**
     * Sets the identifier of this entity.
     * @param value Identifier that should be set.
     */
    public void setId(long value) { _id = value; }
}