package com.tasky.android.storage;

import com.tasky.android.storage.queries.CombinedQueryFilter;
import com.tasky.android.storage.queries.QueryFilter;
import com.tasky.android.storage.queries.ValueQueryFilter;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class QueriesUnitTests {
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
    public void CombinedQueryFilter_whereClause_isCorrect() {
        QueryFilter first = new ValueQueryFilter("COLUMN1", ValueQueryFilter.Type.Equals, "VALUE1");
        QueryFilter second = new ValueQueryFilter("COLUMN2", ValueQueryFilter.Type.Equals, "VALUE2");
        QueryFilter sut = new CombinedQueryFilter(first, second, CombinedQueryFilter.CombinationType.And);
        String result = sut.buildSqliteWhereClause();
        assertEquals("(COLUMN1=?)AND(COLUMN2=?)", result);
    }

    @Test
    public void CombinedQueryFilter_parameters_areCorrect() {
        QueryFilter first = new ValueQueryFilter("COLUMN1", ValueQueryFilter.Type.Equals, "VALUE1");
        QueryFilter second = new ValueQueryFilter("COLUMN2", ValueQueryFilter.Type.Equals, "VALUE2");
        QueryFilter sut = new CombinedQueryFilter(first, second, CombinedQueryFilter.CombinationType.And);
        List<String> result = sut.getSqliteParameters();
        assertEquals(2, result.size());
        assertEquals("VALUE1", result.get(0));
        assertEquals("VALUE2", result.get(1));
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
    public void ValueQueryFilter_smallerthan_evaluates_correctly_true() throws Exception {
        QueryFilter sut = new ValueQueryFilter("COLUMN", ValueQueryFilter.Type.SmallerThan, 5);
        FakeEntity target = new FakeEntity(4);
        boolean result = sut.evaluate(target);
        assertEquals(true, result);
    }

    @Test
    public void ValueQueryFilter_smallerthan_evaluates_correctly_false() throws Exception {
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