package com.tasky.android.storage.queries;

import com.tasky.android.entities.TaskPriority;
import com.tasky.android.storage.EnumMapper;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the EnumMapper implementation.
 */
public class EnumMapperTests {
    @Test
    public void EnumMapper_getsIntegerByEnum() {
        TaskPriority input = TaskPriority.High;
        Integer result = EnumMapper.getIntegerByEnum(input);
        assertEquals((Object)2, result);
    }

    @Test
    public void EnumMapper_getsEnumByInteger() {
        Integer input = 2;
        TaskPriority result = EnumMapper.getEnumByInteger(input, TaskPriority.class);
        assertEquals(TaskPriority.High, result);
    }
}