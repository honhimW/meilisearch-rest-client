/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.honhimw.ms.support;

import java.io.Serializable;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @author hon_him
 * @since 2024-01-05
 */

public class FilterBuilder {

    private StringBuilder stringBuilder;

    private FilterBuilder(StringBuilder stringBuilder) {
        this.stringBuilder = stringBuilder;
    }

    /**
     * Get a new instance of {@link FilterBuilder}
     *
     * @return {@link FilterBuilder}
     */
    public static FilterBuilder builder() {
        StringBuilder sb = new StringBuilder();
        return new FilterBuilder(sb);
    }

    /**
     * Creates a new instance of FilterBuilder and initializes it with the given initial expression.
     *
     * @param initial the initial expression to be set on the FilterBuilder
     * @return the newly created FilterBuilder instance
     */
    public static FilterBuilder builder(Consumer<Expression> initial) {
        FilterBuilder filterBuilder = new FilterBuilder(null);
        filterBuilder.base(initial);
        return filterBuilder;
    }

    /**
     * A function that takes a Consumer of Expression as a parameter, creates a new Expression object,
     * accepts the Consumer, and returns the result of the valid method of the Expression object.
     *
     * @param expression a Consumer of Expression
     * @return the result of the valid method of the Expression object
     */
    public static String singleExpression(Consumer<Expression> expression) {
        Expression _expression = new Expression();
        expression.accept(_expression);
        return _expression.vaild();
    }

    /**
     * Builds the filter expression and returns it as a string.
     *
     * @return the filter expression
     */
    public String build() {
        return stringBuilder.toString();
    }

    /**
     * Set the base expression.
     *
     * @param base the base expression
     * @return this
     */
    public FilterBuilder base(Consumer<Expression> base) {
        stringBuilder = new StringBuilder();
        Expression nextExpression = new Expression();
        base.accept(nextExpression);
        String expression = nextExpression.vaild();
        stringBuilder.append(expression);
        return this;
    }

    /**
     * Add next expression as an AND condition.
     *
     * @param next the next expression
     * @return this
     */
    public FilterBuilder and(Consumer<Expression> next) {
        if (stringBuilder.length() != 0) {
            stringBuilder.append(" ").append(Operator.AND.symbol()).append(" ");
        }
        Expression nextExpression = new Expression();
        next.accept(nextExpression);
        String expression = nextExpression.vaild();
        stringBuilder.append(expression);
        return this;
    }

    /**
     * Add next expression as an OR condition.
     *
     * @param next the next expression
     * @return this
     */
    public FilterBuilder or(Consumer<Expression> next) {
        if (stringBuilder.length() != 0) {
            stringBuilder.append(" ").append(Operator.OR.symbol()).append(" ");
        }
        Expression nextExpression = new Expression();
        next.accept(nextExpression);
        String expression = nextExpression.vaild();
        stringBuilder.append(expression);
        return this;
    }

    /**
     * Set the base group expression.
     *
     * @param sub group expression
     * @return this
     */
    public FilterBuilder baseGroup(Consumer<FilterBuilder> sub) {
        stringBuilder = new StringBuilder();
        FilterBuilder subFilter = FilterBuilder.builder();
        sub.accept(subFilter);
        if (subFilter.stringBuilder.length() != 0) {
            stringBuilder
                .append("(")
                .append(subFilter.stringBuilder)
                .append(")");
        }
        return this;
    }

    /**
     * Add next group expression as an AND condition.
     *
     * @param sub group expression
     * @return this
     */
    public FilterBuilder andGroup(Consumer<FilterBuilder> sub) {
        if (stringBuilder.length() != 0) {
            stringBuilder.append(" ").append(Operator.AND.symbol()).append(" ");
        }
        FilterBuilder subFilter = FilterBuilder.builder();
        sub.accept(subFilter);
        if (subFilter.stringBuilder.length() != 0) {
            stringBuilder
                .append("(")
                .append(subFilter.stringBuilder)
                .append(")");
        }
        return this;
    }

    /**
     * Add next group expression as an OR condition.
     *
     * @param sub group expression
     * @return this
     */
    public FilterBuilder orGroup(Consumer<FilterBuilder> sub) {
        if (stringBuilder.length() != 0) {
            stringBuilder.append(" ").append(Operator.OR.symbol()).append(" ");
        }
        FilterBuilder subFilter = FilterBuilder.builder();
        sub.accept(subFilter);
        if (subFilter.stringBuilder.length() != 0) {
            stringBuilder
                .append("(")
                .append(subFilter.stringBuilder)
                .append(")");
        }
        return this;
    }

    /**
     * Filter expression.
     */
    public static class Expression implements Serializable {

        private String expression;

        private Expression() {

        }

        private String vaild() {
            if (StringUtils.isBlank(expression)) {
                throw new IllegalArgumentException("filter expression is not yet set");
            }
            return expression;
        }

        /**
         * Equal expression.
         *
         * @param attribute attribute name
         * @param value     filter value
         * @return this
         */
        public Expression equal(String attribute, String value) {
            this.expression = String.format("%s %s '%s'", attribute, Operator.EQUAL.symbol(), value);
            return this;
        }

        /**
         * Equal expression.
         *
         * @param attribute attribute name
         * @param value     filter value
         * @return this
         */
        public Expression equal(String attribute, Number value) {
            this.expression = String.format("%s %s %s", attribute, Operator.EQUAL.symbol(), value);
            return this;
        }

        /**
         * Unequal expression.
         *
         * @param attribute attribute name
         * @param value     filter value
         * @return this
         */
        public Expression unequal(String attribute, String value) {
            this.expression = String.format("%s %s '%s'", attribute, Operator.UNEQUAL.symbol(), value);
            return this;
        }

        /**
         * Unequal expression.
         *
         * @param attribute attribute name
         * @param value     filter value
         * @return this
         */
        public Expression unequal(String attribute, Number value) {
            this.expression = String.format("%s %s %s", attribute, Operator.UNEQUAL.symbol(), value);
            return this;
        }

        /**
         * Greater than expression.
         *
         * @param attribute attribute name
         * @param value     filter value
         * @return this
         */
        public Expression gt(String attribute, Number value) {
            this.expression = String.format("%s %s %s", attribute, Operator.GT.symbol(), value);
            return this;
        }

        /**
         * Greater than or equal expression.
         *
         * @param attribute attribute name
         * @param value     filter value
         * @return this
         */
        public Expression ge(String attribute, Number value) {
            this.expression = String.format("%s %s %s", attribute, Operator.GE.symbol(), value);
            return this;
        }

        /**
         * Less than expression.
         *
         * @param attribute attribute name
         * @param value     filter value
         * @return this
         */
        public Expression lt(String attribute, Number value) {
            this.expression = String.format("%s %s %s", attribute, Operator.LT.symbol(), value);
            return this;
        }

        /**
         * Less than or equal expression.
         *
         * @param attribute attribute name
         * @param value     filter value
         * @return this
         */
        public Expression le(String attribute, Number value) {
            this.expression = String.format("%s %s %s", attribute, Operator.LE.symbol(), value);
            return this;
        }

        /**
         * Range expression.
         *
         * @param attribute attribute name
         * @param min       min
         * @param max       max
         * @return this
         */
        public Expression to(String attribute, Number min, Number max) {
            this.expression = String.format("%s %s %s %s", attribute, min, Operator.TO.symbol(), max);
            return this;
        }

        /**
         * Exists expression.
         *
         * @param attribute attribute name
         * @return this
         */
        public Expression exists(String attribute) {
            this.expression = String.format("%s %s", attribute, Operator.EXISTS.symbol());
            return this;
        }

        /**
         * Not exists expression.
         *
         * @param attribute attribute name
         * @return this
         */
        public Expression notExists(String attribute) {
            this.expression = String.format("%s %s", attribute, Operator.NOT_EXISTS.symbol());
            return this;
        }

        /**
         * Is empty expression.
         *
         * @param attribute attribute name
         * @return this
         */
        public Expression isEmpty(String attribute) {
            this.expression = String.format("%s %s", attribute, Operator.IS_EMPTY.symbol());
            return this;
        }

        /**
         * Not empty expression.
         *
         * @param attribute attribute name
         * @return this
         */
        public Expression notEmpty(String attribute) {
            this.expression = String.format("%s %s", attribute, Operator.NOT_EMPTY.symbol());
            return this;
        }

        /**
         * If you trying to determine whether a parameter has a value, you may mean to use {@link #isNullOrNotExists(String)}.
         *
         * @param attribute attribute name
         * @return this
         */
        public Expression isNull(String attribute) {
            this.expression = String.format("%s %s", attribute, Operator.IS_NULL.symbol());
            return this;
        }

        /**
         * Using 'xxx IS NULL' will return documents that doesn't contain the attribute,
         * it may cause some confusing results.
         * <p>
         * Basically, this method generate a filter expression as fallow:
         * <pre>
         * "(xxx NOT EXISTS OR xxx IS NULL)"
         * </pre>
         *
         * @param attribute attribute name
         * @return this
         */
        public Expression isNullOrNotExists(String attribute) {
            this.expression = String.format("(%s %s %s %s %s)", attribute, Operator.NOT_EXISTS.symbol(), Operator.OR.symbol(), attribute, Operator.IS_NULL.symbol());
            return this;
        }

        /**
         * Not null expression.
         *
         * @param attribute attribute name
         * @return this
         */
        public Expression notNull(String attribute) {
            this.expression = String.format("%s %s", attribute, Operator.NOT_NULL.symbol());
            return this;
        }

        /**
         * In expression.
         *
         * @param attribute attribute name
         * @param values    values
         * @return this
         */
        public Expression in(String attribute, String... values) {
            this.expression = Arrays.stream(values).collect(Collectors.joining(
                "', '",
                attribute + " " + Operator.IN.symbol() + " ['",
                "']"));
            return this;
        }

        /**
         * In expression.
         *
         * @param attribute attribute name
         * @param values    values
         * @return this
         */
        public Expression in(String attribute, Number... values) {
            this.expression = Arrays.stream(values).map(String::valueOf).collect(Collectors.joining(
                ", ",
                attribute + " " + Operator.IN.symbol() + " [",
                "]"));
            return this;
        }

        /**
         * Not in expression.
         *
         * @param attribute attribute name
         * @param values    values
         * @return this
         */
        public Expression notIn(String attribute, String... values) {
            this.expression = Arrays.stream(values).collect(Collectors.joining(
                "','",
                attribute + " " + Operator.NOT_IN.symbol() + " ['",
                "']"));
            return this;
        }

        /**
         * Not expression.
         *
         * @return this
         */
        public Expression not() {
            if (StringUtils.isNotBlank(expression)) {
                if (StringUtils.startsWith(expression, Operator.NOT.symbol())) {
                    this.expression = StringUtils.removeStart(this.expression, Operator.NOT.symbol() + " ");
                } else {
                    this.expression = Operator.NOT.symbol() + " " + this.expression;
                }
            }
            return this;
        }

    }

    /**
     * Operator enum.
     */
    public enum Operator implements Serializable {
        /**
         * =
         */
        EQUAL("="),
        /**
         * !=
         */
        UNEQUAL("!="),
        /**
         * >
         */
        GT(">"),
        /**
         * >=
         */
        GE(">="),
        /**
         * %3C
         */
        LT("<"),
        /**
         * %3C=
         */
        LE("<="),
        /**
         * TO
         */
        TO("TO"),
        /**
         * EXISTS
         */
        EXISTS("EXISTS"),
        /**
         * NOT EXISTS
         */
        NOT_EXISTS("NOT EXISTS"),
        /**
         * IS EMPTY
         */
        IS_EMPTY("IS EMPTY"),
        /**
         * IS NOT EMPTY
         */
        NOT_EMPTY("IS NOT EMPTY"),
        /**
         * IS NULL
         */
        IS_NULL("IS NULL"),
        /**
         * IS NOT NULL
         */
        NOT_NULL("IS NOT NULL"),
        /**
         * IN
         */
        IN("IN"),
        /**
         * NOT IN
         */
        NOT_IN("NOT IN"),
        /**
         * NOT
         */
        NOT("NOT"),
        /**
         * AND
         */
        AND("AND"),
        /**
         * OR
         */
        OR("OR"),
        ;

        private final String symbol;


        Operator(String symbol) {
            this.symbol = symbol;
        }

        /**
         * Operator symbol.
         * @return symbol string
         */
        public String symbol() {
            return this.symbol;
        }
    }


}
