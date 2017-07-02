package com.tasky.android.storage.queries;

import com.tasky.android.utilities.ParameterCheck;

import java.util.List;

/**
 * Represents a QueryFilter that is composed by other QueryFilters.
 */
public final class CombinedQueryFilter extends QueryFilterBase {
    private final QueryFilter first;
    private final QueryFilter second;
    private final CombinationType combinationType;

    /**
     * Creates a CombinedQueryFilter from the specified QueryFilters.
     * @param first First QueryFilter that should be combined.
     * @param second Second QueryFilter that should be combined.
     * @param combinationType Type indicating how the combination should behave.
     */
    public CombinedQueryFilter(QueryFilter first, QueryFilter second, CombinationType combinationType) {
        ParameterCheck.notNull(first, "first");
        ParameterCheck.notNull(second, "second");
        ParameterCheck.notNull(combinationType, "combinationType");

        this.first = first;
        this.second = second;
        this.combinationType = combinationType;
    }

    /**
     * Generates a where clause that can be used in an Sqlite query.
     * @return A String with the where clause.
     */
    @Override
    public String buildSqliteWhereClause() {
        return  "(" + first.buildSqliteWhereClause() + ")" +
                combinationType.getSqliteOperator() +
                "(" + second.buildSqliteWhereClause() + ")";
    }

    /**
     * Gets a list of the parameters used in the where clause in the order of their appearance.
     * @return A list of the parameters.
     */
    @Override
    public List<String> getSqliteParameters() {
        List<String> parameters = first.getSqliteParameters();
        parameters.addAll(second.getSqliteParameters());
        return parameters;
    }

    /**
     * Checks if the specified object satisfies the filter condition.
     * @param target Object on which to test the filter condition.
     * @return True, if the object satisfies the filter condition; otherwise false.
     */
    @Override
    public boolean evaluate(Object target) throws IllegalAccessException {
        return combinationType.evaluate(first.evaluate(target), second.evaluate(target));
    }


    /**
     * Types that indicate how a combination can behave.
     */
    public enum CombinationType {
        /**
         * Will result in a match if both of the combined QueryFilters match.
         */
        And("AND") {
            @Override
            public boolean evaluate(boolean first, boolean second) {
                return first && second;
            }
        },

        /**
         * Will result in a match if at least one of the combined QueryFilters match.
         */
        Or("OR") {
            @Override
            public boolean evaluate(boolean first, boolean second) {
                return first || second;
            }
        };

        private final String sqliteOperator;
        CombinationType(String sqliteOperator) {
            this.sqliteOperator = sqliteOperator;
        }

        /**
         * Gets the keyword that is used in Sqlite to represent the combination.
         * @return The keyword that is used in Sqlite to represent the combination.
         */
        public String getSqliteOperator() { return sqliteOperator; }

        /**
         * Checks if the combination is matched by the individual results of the combination.
         * @param first Result of the first combined QueryFilter.
         * @param second Result of the second combined QueryFilter.
         * @return True, if the combination is matched; otherwise false.
         */
        public abstract boolean evaluate(boolean first, boolean second);
    }
}