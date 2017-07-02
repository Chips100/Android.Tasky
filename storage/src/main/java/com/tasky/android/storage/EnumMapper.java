package com.tasky.android.storage;

import com.tasky.android.entities.TaskPriority;
import com.tasky.android.utilities.ParameterCheck;

import java.util.HashMap;
import java.util.Map;

/**
 * Maps values of enums in entities to Integers for storage in the database.
 */
public class EnumMapper {
    private static final Map<Class, Map<Object, Integer>> EnumMaps;

    static {
        // Create maps of all known enums to integers.
        EnumMaps = new HashMap();
        addTaskPriorityMap();
    }

    /**
     * Gets an Integer that represents the specified enum value for storage in the database.
     * @param enumValue Enum value that should be stored in the database.
     * @return An Integer that represents the enum value in the database.
     */
    public static Integer getIntegerByEnum(Object enumValue) {
        // Map null values to null integers.
        if (enumValue == null) return null;

        ParameterCheck.IsKeyOf(enumValue.getClass(), EnumMaps, "Class of enumValue", "EnumMaps");
        Map<Object, Integer> enumMap = EnumMaps.get(enumValue.getClass());
        ParameterCheck.IsKeyOf(enumValue, enumMap, "enumValue", "enumMap");

        return enumMap.get(enumValue);
    }

    /**
     * Gets the enum value that is represented by the specified Integer.
     * @param integerValue Integer stored in the database that represents an enum value.
     * @param enumClass Class of the enum that is represented by the Integer.
     * @param <T> Type of the enum value that should be returned.
     * @return The enum value represented by the specified Integer.
     */
    public static<T> T getEnumByInteger(Integer integerValue, Class enumClass) {
        // Map null integers to null values.
        if (integerValue == null) return null;

        ParameterCheck.NotNull(enumClass, "enumClass");
        ParameterCheck.IsKeyOf(enumClass, EnumMaps, "enumClass", "EnumMaps");
        Map<Object, Integer> enumMap = EnumMaps.get(enumClass);

        // Search map for entry that has the specified Integer as its value.
        for (Map.Entry<Object, Integer> entry : enumMap.entrySet()) {
            if (entry.getValue().equals(integerValue)) {
                // If found, return the key.
                return (T)entry.getKey();
            }
        }

        // If not found, throw exception.
        throw new IllegalArgumentException("Integer " + integerValue + " could not be mapped to enum " + enumClass + ".");
    }

    private static void addTaskPriorityMap() {
        Map<Object, Integer> map = new HashMap();
        map.put(TaskPriority.Low, 0);
        map.put(TaskPriority.Normal, 1);
        map.put(TaskPriority.High, 2);

        EnumMaps.put(TaskPriority.class, map);
    }
}