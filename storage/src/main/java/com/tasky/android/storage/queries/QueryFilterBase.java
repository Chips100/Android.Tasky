package com.tasky.android.storage.queries;

import java.util.List;

/**
 * Base implementation of a QueryFilter with basic features.
 */
public abstract class QueryFilterBase implements QueryFilter {
    /**
     * Generates a where clause that can be used in an Sqlite query.
     * @return A String with the where clause.
     */
    public abstract String buildSqliteWhereClause();

    /**
     * Gets a list of the parameters used in the where clause in the order of their appearance.
     * @return A list of the parameters.
     */
    public abstract List<String> getSqliteParameters();

    /**
     * Checks if the specified object satisfies the filter condition.
     * @param target Object on which to test the filter condition.
     * @return True, if the object satisfies the filter condition; otherwise false.
     */
    public abstract boolean evaluate(Object target) throws IllegalAccessException;

    /**
     * Combines this QueryFilter with another QueryFilter with AND semantics.
     * @param other Other QueryFilter that should make up the combination.
     * @return A QueryFilter that is the combination of this and the specified QueryFilter.
     */
    public QueryFilterBase And(QueryFilter other) {
        return new CombinedQueryFilter(this, other, CombinedQueryFilter.CombinationType.And);
    }

    /**
     * Combines this QueryFilter with another QueryFilter with OR semantics.
     * @param other Other QueryFilter that should make up the combination.
     * @return A QueryFilter that is the combination of this and the specified QueryFilter.
     */
    public QueryFilterBase Or(QueryFilter other) {
        return new CombinedQueryFilter(this, other, CombinedQueryFilter.CombinationType.Or);
    }
}