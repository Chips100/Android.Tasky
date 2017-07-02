package com.tasky.android.utilities;

import java.util.Map;

/**
 * Provides utility methods dealing with parameter checking.
 */
public class ParameterCheck {
    /**
     * Throws an exception if the specified value is null.
     * @param value Value that should be checked for null.
     * @param parameterName Name of the parameter for which the value was passed.
     */
    public static void notNull(Object value, String parameterName) {
        if (value == null) {
            throw new IllegalArgumentException(parameterName + " cannot be null.");
        }
    }

    /**
     * Throws an exception if the specified key is not part of the specified map.
     * @param key Key that should be contained in the map.
     * @param map Map that should contain the key.
     * @param keyName Name of the parameter that holds the key.
     * @param mapName Name of the parameter or variable that holds the map.
     * @param <TKey> Type of the keys of the map.
     * @param <TValue> Type of the values of the map.
     */
    public static <TKey, TValue>void isKeyOf(TKey key, Map<TKey, TValue> map, String keyName, String mapName) {
        ParameterCheck.notNull(map, mapName);

        if (!map.containsKey(key)) {
            throw new IllegalArgumentException(keyName + " is not a valid key of " + mapName);
        }
    }
}