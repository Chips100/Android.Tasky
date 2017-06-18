package com.tasky.android.storage.queries;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the CombinedQueryFilter implementations.
 */
public class CombinedQueryFilterTests {
    @Test
    public void CombinedQueryFilter_and_whereClause_isCorrect() {
        QueryFilter first = new FakeQueryFilter("CLAUSE1", null, false);
        QueryFilter second = new FakeQueryFilter("CLAUSE2", null, false);
        QueryFilter sut = new CombinedQueryFilter(first, second, CombinedQueryFilter.CombinationType.And);
        String result = sut.buildSqliteWhereClause();
        assertEquals("(CLAUSE1)AND(CLAUSE2)", result);
    }

    @Test
    public void CombinedQueryFilter_or_whereClause_isCorrect() {
        QueryFilter first = new FakeQueryFilter("CLAUSE1", null, false);
        QueryFilter second = new FakeQueryFilter("CLAUSE2", null, false);
        QueryFilter sut = new CombinedQueryFilter(first, second, CombinedQueryFilter.CombinationType.Or);
        String result = sut.buildSqliteWhereClause();
        assertEquals("(CLAUSE1)OR(CLAUSE2)", result);
    }

    @Test
    public void CombinedQueryFilter_parameters_areCorrect() {
        QueryFilter first = new FakeQueryFilter(null, "PARAMETER1", false);
        QueryFilter second = new FakeQueryFilter(null, "PARAMETER2", false);
        QueryFilter sut = new CombinedQueryFilter(first, second, CombinedQueryFilter.CombinationType.And);
        List<String> result = sut.getSqliteParameters();
        assertEquals(2, result.size());
        assertEquals("PARAMETER1", result.get(0));
        assertEquals("PARAMETER2", result.get(1));
    }

    @Test
    public void CombinedQueryFilter_and_evaluates_false() throws Exception {
        QueryFilter first = new FakeQueryFilter(null, null, true);
        QueryFilter second = new FakeQueryFilter(null, null, false);
        QueryFilter sut = new CombinedQueryFilter(first, second, CombinedQueryFilter.CombinationType.And);
        boolean result = sut.evaluate(null);
        assertEquals(false, result);
    }

    @Test
    public void CombinedQueryFilter_and_evaluates_true() throws Exception {
        QueryFilter first = new FakeQueryFilter(null, null, true);
        QueryFilter second = new FakeQueryFilter(null, null, true);
        QueryFilter sut = new CombinedQueryFilter(first, second, CombinedQueryFilter.CombinationType.And);
        boolean result = sut.evaluate(null);
        assertEquals(true, result);
    }

    @Test
    public void CombinedQueryFilter_or_evaluates_false() throws Exception {
        QueryFilter first = new FakeQueryFilter(null, null, false);
        QueryFilter second = new FakeQueryFilter(null, null, false);
        QueryFilter sut = new CombinedQueryFilter(first, second, CombinedQueryFilter.CombinationType.Or);
        boolean result = sut.evaluate(null);
        assertEquals(false, result);
    }

    @Test
    public void CombinedQueryFilter_or_evaluates_true() throws Exception {
        QueryFilter first = new FakeQueryFilter(null, null, false);
        QueryFilter second = new FakeQueryFilter(null, null, true);
        QueryFilter sut = new CombinedQueryFilter(first, second, CombinedQueryFilter.CombinationType.Or);
        boolean result = sut.evaluate(null);
        assertEquals(true, result);
    }

    /**
     * Fake implementation of a QueryFilter with configurable behaviour.
     */
    private class FakeQueryFilter implements QueryFilter {
        private final String sqliteWhereClause;
        private final String sqliteParameter;
        private boolean evaluationResult;

        public FakeQueryFilter(String sqliteWhereClause, String sqliteParameter, boolean evaluationResult) {
            this.sqliteWhereClause = sqliteWhereClause;
            this.sqliteParameter = sqliteParameter;
            this.evaluationResult = evaluationResult;
        }

        @Override
        public String buildSqliteWhereClause() { return sqliteWhereClause; }

        @Override
        public List<String> getSqliteParameters() {
            List<String> result = new ArrayList();
            result.add(sqliteParameter);
            return result;
        }

        @Override
        public boolean evaluate(Object target) throws IllegalAccessException { return evaluationResult; }
    }
}