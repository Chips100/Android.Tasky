package com.tasky.android.storage;

import android.database.Cursor;
import android.support.annotation.Nullable;

import com.tasky.android.utilities.ParameterCheck;

import org.joda.time.DateTime;

import java.util.Map;
import java.util.TreeMap;

/**
 * Utility methods for working with an Sqlite database.
 */
public class SqliteTools {
    /**
     * Converts a Joda DateTime value to a value appropriate for storage in an Sqlite database.
     * @param value Joda DateTime value to store; can be null.
     * @return A value representing the Joda DateTime that can be stored in an Sqlite database.
     */
    public static Long convertDateTime(DateTime value) {
        if (value == null) return null;
        return value.getMillis();
    }

    /**
     * Converts the specified value to a String that can be used as an Sqlite query parameter.
     * @param value Value that should be converted.
     * @return A string that represents the value as an Sqlite query parameter.
     */
    public static String convertSqliteParameter(Object value) {
        ParameterCheck.NotNull(value, "value");

        if (value instanceof DateTime) {
            value = convertDateTime((DateTime)value);
        }
        if (value instanceof Boolean) {
            value = (Boolean) value ? 1 : 0;
        }

        return value.toString();
    }

    /**
     * Gets a map of the column names from the cursor with their indices.
     * @param cursor Cursor to get column names from.
     * @return A map of the column names with their indices.
     */
    public static Map<String, Integer> getColumnIndices(Cursor cursor) {
        Map<String, Integer> map = new TreeMap(String.CASE_INSENSITIVE_ORDER);

        for(String columnName : cursor.getColumnNames()) {
            map.put(columnName, cursor.getColumnIndex(columnName));
        }

        return map;
    }

    /**
     * Reads a boolean value from the cursor.
     * @param cursor Cursor to read the boolean value from.
     * @param columnIndex Index of the column from which to read the boolean value.
     * @return Value read from the cursor.
     */
    public static boolean getBoolean(Cursor cursor, Integer columnIndex) {
        return cursor.getInt(columnIndex) == 1;
    }

    /**
     * Reads a Joda DateTime value from the cursor.
     * @param cursor Cursor to read the Joda DateTime value from.
     * @param columnIndex Index of the column from which to read the Joda DateTime value.
     * @return Value read from the cursor.
     */
    @Nullable
    public static DateTime getDateTime(Cursor cursor, Integer columnIndex) {
        return cursor.isNull(columnIndex) ? null : new DateTime(cursor.getLong(columnIndex));
    }

    /**
     * Reads a nullable Integer value from the cursor.
     * @param cursor Cursor to read the nullable Integer value from.
     * @param columnIndex Index of the column from which to read the nullable Integer value.
     * @return Value read from the cursor; null if the column contains a NULL value.
     */
    @Nullable
    public static Integer getNullableInt(Cursor cursor, Integer columnIndex) {
        return cursor.isNull(columnIndex) ? null : cursor.getInt(columnIndex);
    }
}