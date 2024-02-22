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

    public static FilterBuilder builder() {
        StringBuilder sb = new StringBuilder();
        return new FilterBuilder(sb);
    }

    public static FilterBuilder builder(Consumer<Expression> initial) {
        FilterBuilder filterBuilder = new FilterBuilder(null);
        filterBuilder.base(initial);
        return filterBuilder;
    }

    public static String singleExpression(Consumer<Expression> expression) {
        Expression _expression = new Expression();
        expression.accept(_expression);
        return _expression.vaild();
    }

    public String build() {
        return stringBuilder.toString();
    }

    public FilterBuilder base(Consumer<Expression> base) {
        stringBuilder = new StringBuilder();
        Expression nextExpression = new Expression();
        base.accept(nextExpression);
        String expression = nextExpression.vaild();
        stringBuilder.append(expression);
        return this;
    }

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

        public Expression equal(String attribute, String value) {
            this.expression = String.format("%s %s '%s'", attribute, Operator.EQUAL.symbol(), value);
            return this;
        }

        public Expression equal(String attribute, Number value) {
            this.expression = String.format("%s %s %s", attribute, Operator.EQUAL.symbol(), value);
            return this;
        }

        public Expression unequal(String attribute, String value) {
            this.expression = String.format("%s %s '%s'", attribute, Operator.UNEQUAL.symbol(), value);
            return this;
        }

        public Expression unequal(String attribute, Number value) {
            this.expression = String.format("%s %s %s", attribute, Operator.UNEQUAL.symbol(), value);
            return this;
        }

        public Expression gt(String attribute, Number value) {
            this.expression = String.format("%s %s %s", attribute, Operator.GT.symbol(), value);
            return this;
        }

        public Expression ge(String attribute, Number value) {
            this.expression = String.format("%s %s %s", attribute, Operator.GE.symbol(), value);
            return this;
        }

        public Expression lt(String attribute, Number value) {
            this.expression = String.format("%s %s %s", attribute, Operator.LT.symbol(), value);
            return this;
        }

        public Expression le(String attribute, Number value) {
            this.expression = String.format("%s %s %s", attribute, Operator.LE.symbol(), value);
            return this;
        }

        public Expression to(String attribute, Number min, Number max) {
            this.expression = String.format("%s %s %s %s", attribute, min, Operator.TO.symbol(), max);
            return this;
        }

        public Expression exists(String attribute) {
            this.expression = String.format("%s %s", attribute, Operator.EXISTS.symbol());
            return this;
        }

        public Expression notExists(String attribute) {
            this.expression = String.format("%s %s", attribute, Operator.NOT_EXISTS.symbol());
            return this;
        }

        public Expression isEmpty(String attribute) {
            this.expression = String.format("%s %s", attribute, Operator.IS_EMPTY.symbol());
            return this;
        }

        public Expression notEmpty(String attribute) {
            this.expression = String.format("%s %s", attribute, Operator.NOT_EMPTY.symbol());
            return this;
        }

        public Expression isNull(String attribute) {
            this.expression = String.format("%s %s", attribute, Operator.IS_NULL.symbol());
            return this;
        }

        public Expression notNull(String attribute) {
            this.expression = String.format("%s %s", attribute, Operator.NOT_NULL.symbol());
            return this;
        }

        public Expression in(String attribute, String... values) {
            this.expression = Arrays.stream(values).collect(Collectors.joining(
                "', '",
                attribute + " " + Operator.IN.symbol() + " ['",
                "']"));
            return this;
        }

        public Expression in(String attribute, Number... values) {
            this.expression = Arrays.stream(values).map(String::valueOf).collect(Collectors.joining(
                ", ",
                attribute + " " + Operator.IN.symbol() + " [",
                "]"));
            return this;
        }

        public Expression notIn(String attribute, String... values) {
            this.expression = Arrays.stream(values).collect(Collectors.joining(
                "','",
                attribute + " " + Operator.NOT_IN.symbol() + " ['",
                "']"));
            return this;
        }

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

    public enum Operator implements Serializable {
        EQUAL("="),
        UNEQUAL("!="),
        GT(">"),
        GE(">="),
        LT("<"),
        LE("<="),
        TO("TO"),
        EXISTS("EXISTS"),
        NOT_EXISTS("NOT EXISTS"),
        IS_EMPTY("IS EMPTY"),
        NOT_EMPTY("IS NOT EMPTY"),
        IS_NULL("IS NULL"),
        NOT_NULL("IS NOT NULL"),
        IN("IN"),
        NOT_IN("NOT IN"),
        NOT("NOT"),
        AND("AND"),
        OR("OR"),
        ;

        private final String symbol;


        Operator(String symbol) {
            this.symbol = symbol;
        }

        public String symbol() {
            return this.symbol;
        }
    }


}
