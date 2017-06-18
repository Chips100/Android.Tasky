package com.tasky.android.storage.queries;

import com.tasky.android.storage.SqliteTools;

import org.joda.time.DateTime;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Tests for the ValueQueryFilter implementations.
 */
public class ValueQueryFilterTests {
    @Test
    public void ValueQueryFilter_whereClause_isCorrect() {
        QueryFilter sut = new ValueQueryFilter("COLUMN", ValueQueryFilter.Type.Equals, "VALUE");
        String result = sut.buildSqliteWhereClause();
        assertEquals("COLUMN=?", result);
    }

    @Test
    public void ValueQueryFilter_parameters_areCorrect() {
        QueryFilter sut = new ValueQueryFilter("COLUMN", ValueQueryFilter.Type.Equals, "VALUE");
        List<String> result = sut.getSqliteParameters();
        assertEquals(1, result.size());
        assertEquals("VALUE", result.get(0));
    }

    @Test
    public void ValueQueryFilter_parameters_supportsDateTime() {
        DateTime parameter = new DateTime(2017, 6, 18, 0, 0);
        QueryFilter sut = new ValueQueryFilter("COLUMN", ValueQueryFilter.Type.Equals, parameter);
        List<String> result = sut.getSqliteParameters();
        assertEquals(1, result.size());
        assertEquals(SqliteTools.convertDateTime(parameter).toString(), result.get(0));
    }

    @Test
    public void ValueQueryFilter_parameters_unusedParameter() {
        QueryFilter sut = new ValueQueryFilter("COLUMN", ValueQueryFilter.Type.IsNull, null);
        List<String> result = sut.getSqliteParameters();
        assertEquals(0, result.size());
    }

    @Test
    public void ValueQueryFilter_equals_evaluates_correctly_true() throws Exception {
        QueryFilter sut = new ValueQueryFilter("COLUMN", ValueQueryFilter.Type.Equals, 1);
        FakeEntity target = new FakeEntity(1);
        boolean result = sut.evaluate(target);
        assertEquals(true, result);
    }

    @Test
    public void ValueQueryFilter_equals_evaluates_correctly_false() throws Exception {
        QueryFilter sut = new ValueQueryFilter("COLUMN", ValueQueryFilter.Type.Equals, 1);
        FakeEntity target = new FakeEntity(15);
        boolean result = sut.evaluate(target);
        assertEquals(false, result);
    }

    @Test
    public void ValueQueryFilter_smallerThan_evaluates_correctly_true() throws Exception {
        QueryFilter sut = new ValueQueryFilter("COLUMN", ValueQueryFilter.Type.SmallerThan, 5);
        FakeEntity target = new FakeEntity(4);
        boolean result = sut.evaluate(target);
        assertEquals(true, result);
    }

    @Test
    public void ValueQueryFilter_smallerThan_evaluates_correctly_false() throws Exception {
        QueryFilter sut = new ValueQueryFilter("COLUMN", ValueQueryFilter.Type.SmallerThan, 5);
        FakeEntity target = new FakeEntity(5);
        boolean result = sut.evaluate(target);
        assertEquals(false, result);
    }

    @Test
    public void ValueQueryFilter_isnull_evaluates_correctly_true() throws Exception {
        QueryFilter sut = new ValueQueryFilter("COLUMN", ValueQueryFilter.Type.IsNull, 0);
        FakeEntity target = new FakeEntity(null);
        boolean result = sut.evaluate(target);
        assertEquals(true, result);
    }

    @Test
    public void ValueQueryFilter_isnull_evaluates_correctly_false() throws Exception {
        QueryFilter sut = new ValueQueryFilter("COLUMN", ValueQueryFilter.Type.IsNull, 0);
        FakeEntity target = new FakeEntity(5);
        boolean result = sut.evaluate(target);
        assertEquals(false, result);
    }

    private class FakeEntity {
        private Integer column;
        public FakeEntity(Integer column) { this.column = column; }
    }
}