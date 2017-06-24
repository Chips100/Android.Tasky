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

    /**
     * Copies the values from all fields from one object to another.
     * @param source Object with the values to copy.
     * @param target Object that should be updated.
     * @param <T> Type of the objects.
     * @return The target object with updated fields.
     */
    public static<T> T copyFields(T source, T target) throws IllegalAccessException {
        ParameterCheck.NotNull(source, "source");
        ParameterCheck.NotNull(target, "target");

        List<Field> fields = getFieldsWithInheritance(source.getClass());
        for (Field field : fields) {
            field.setAccessible(true);
            field.set(target, field.get(source));
        }

        return target;
    }
}