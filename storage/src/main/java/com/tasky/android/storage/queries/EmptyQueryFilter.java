package com.tasky.android.storage.queries;

import java.util.ArrayList;
import java.util.List;

/**
 * QueryFilter that will always be a match.
 */
public class EmptyQueryFilter implements QueryFilter {

    @Override
    public String buildSqliteWhereClause() { return null; }

    @Override
    public List<String> getSqliteParameters() { return new ArrayList(); }

    @Override
    public boolean evaluate(Object target) throws IllegalAccessException { return true; }
}