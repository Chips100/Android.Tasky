package com.tasky.android.utilities;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Provides utility methods for reflection operations.
 */
public class ReflectionTools {
    /**
     * Gets all fields of the specified type, including inherited ones.
     * @param type Type of which to get the fields.
     * @return A list of all fields of the specified type.
     */
    public static List<Field> getFieldsWithInheritance(Class<?> type) {
        // Implementation from: https://stackoverflow.com/a/2405757
        List<Field> fields = new ArrayList();
        for (Class<?> c = type; c != null; c = c.getSuperclass()) {
            fields.addAll(Arrays.asList(c.getDeclaredFields()));
        }
        return fields;
    }
}