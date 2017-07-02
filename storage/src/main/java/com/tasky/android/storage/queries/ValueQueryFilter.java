package com.tasky.android.storage.queries;

import com.tasky.android.storage.SqliteTools;
import com.tasky.android.utilities.ParameterCheck;
import com.tasky.android.utilities.ReflectionTools;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a QueryFilter that puts a condition on a single property of the target items.
 */
public class ValueQueryFilter<TValue> extends QueryFilterBase {
    private final String columnName;
    private final TValue expected;
    private final Type type;

    /**
     * Creates a ValueQueryFilter.
     * @param columnName Column that the ValueQueryFilter should compare.
     * @param type Type of the comparison that should be applied.
     * @param expected Value that should be used for the comparison.
     */
    public ValueQueryFilter(String columnName, Type type, TValue expected) {
        ParameterCheck.notNull(columnName, "columnName");
        ParameterCheck.notNull(type, "type");
        if (type.usesValue()) ParameterCheck.notNull(expected, "expected");

        this.columnName = columnName;
        this.type = type;
        this.expected = expected;
    }

    /**
     * Generates a where clause that can be used in an Sqlite query.
     * @return A String with the where clause.
     */
    @Override
    public String buildSqliteWhereClause() {
        return this.columnName + " "
                + type.getSqliteOperator()
                + (type.usesValue() ? " ?" : "");
    }

    /**
     * Gets a list of the parameters used in the where clause in the order of their appearance.
     * @return A list of the parameters.
     */
    @Override
    public List<String> getSqliteParameters() {
        List<String> result = new ArrayList();
        if (type.usesValue()) result.add(SqliteTools.convertSqliteParameter(expected));
        return result;
    }

    /**
     * Checks if the specified object satisfies the filter condition.
     * @param target Object on which to test the filter condition.
     * @return True, if the object satisfies the filter condition; otherwise false.
     */
    @Override
    public boolean evaluate(Object target) throws IllegalAccessException {
        // Search for field in object with same name as the column.
        // Ignoring case because Sqlite is not case-sensitive.
        List<Field> fields = ReflectionTools.getFieldsWithInheritance(target.getClass());
        for (Field field:fields) {
            if (field.getName().equalsIgnoreCase(columnName)) {
                // If found, compare the value in the field.
                field.setAccessible(true);
                return type.evaluate(expected, field.get(target));
            }
        }

        // Field not found in target object; cannot match.
        return false;
    }

    /**
     * Semantics that can be used for comparisons with the ValueQueryFilter.
     */
    public enum Type {
        /**
         * Compares values for equality.
         */
        Equals("=") {
            @Override
            public<TExpected> boolean evaluate(TExpected expected, Object actual) {
                return actual != null && actual.equals(expected);
            }
        },

        /**
         * Checks if the value of the entity is smaller than the specified value.
         */
        SmallerThan("<") {
            @Override
            public<TExpected> boolean evaluate(TExpected expected, Object actual) {
                if (actual == null) return false;
                Comparable<TExpected> asComparable = (Comparable<TExpected>)actual;
                return asComparable.compareTo(expected) < 0;
            }
        },

        /**
         * Checks if the value of the entity is null.
         */
        IsNull("ISNULL", false) {
            @Override
            public <TExpected> boolean evaluate(TExpected expected, Object actual) {
                return actual == null;
            }
        };

        private final String _sqliteOperator;
        private final boolean _usesValue;

        Type(String sqliteOperator) { this(sqliteOperator, true); }
        Type(String sqliteOperator, boolean usesValue) {
            _sqliteOperator = sqliteOperator;
            _usesValue = usesValue;
        }

        /**
         * Gets the identifier for the comparison operation in Sqlite syntax.
         * @return The identifier for the comparison operation in Sqlite syntax.
         */
        public String getSqliteOperator() { return _sqliteOperator; }

        /**
         * Indicates if the comparison operation makes use of the second operand.
         * @return True, if the operand is used; otherwise false.
         */
        public boolean usesValue() { return _usesValue; }

        /**
         * Executes the comparison operation with the specified values.
         * @param expected Value that should be used for the comparison.
         * @param actual Value that is present in the entity.
         * @param <TExpected> Type of the value that is used for the comparison.
         * @return True, if the comparison is a match, otherwise false.
         */
        public abstract<TExpected> boolean evaluate(TExpected expected, Object actual);
    }
}