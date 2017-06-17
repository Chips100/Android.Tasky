package com.tasky.android.storage.queries;

import java.util.List;

/**
 * Represents a filter that can be translated to Sqlite syntax.
 */
public interface QueryFilter {
    /**
     * Generates a where clause that can be used in an Sqlite query.
     * @return A String with the where clause.
     */
    String buildSqliteWhereClause();

    /**
     * Gets a list of the parameters used in the where clause in the order of their appearance.
     * @return A list of the parameters.
     */
    List<String> getSqliteParameters();

    /**
     * Checks if the specified object satisfies the filter condition.
     * @param target Object on which to test the filter condition.
     * @return True, if the object satisfies the filter condition; otherwise false.
     */
    boolean evaluate(Object target) throws IllegalAccessException;
}