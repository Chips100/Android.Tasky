package com.tasky.android.utilities;

/**
 * Provides utility methods dealing with parameter checking.
 */
public class ParameterCheck {
    /**
     * Throws an exception if the specified value is null.
     * @param value Value that should be checked for null.
     * @param parameterName Name of the parameter for which the value was passed.
     */
    public static void NotNull(Object value, String parameterName) {
        if (value == null) {
            throw new IllegalArgumentException(parameterName + " cannot be null.");
        }
    }
}