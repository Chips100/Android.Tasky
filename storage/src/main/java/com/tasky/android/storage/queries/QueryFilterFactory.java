package com.tasky.android.storage.queries;

/**
 * Provides shorthand methods for creating typical QueryFilters.
 */
public class QueryFilterFactory {
    /**
     * Creates a QueryFilter that checks if a value is either null or smaller than the compare value.
     * @param columnName Name of the column with the value to check.
     * @param value Compare value that should be greater than the checked value.
     * @param <TValue> Type of the value to compare to.
     * @return A QueryFilter that checks if a value is either null or smaller than the compare value.
     */
    public static<TValue> QueryFilter smallerThanOrNull(String columnName, TValue value) {
        return new ValueQueryFilter(columnName, ValueQueryFilter.Type.SmallerThan, value)
            .Or(new ValueQueryFilter(columnName, ValueQueryFilter.Type.IsNull, null));
    }
}