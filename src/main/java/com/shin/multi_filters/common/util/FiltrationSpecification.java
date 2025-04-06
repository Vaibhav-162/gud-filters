package com.shin.multi_filters.common.util;

import com.shin.multi_filters.common.FilterCriteria;
import com.shin.multi_filters.common.FilterOperations;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class FiltrationSpecification<T> {
    Logger logger = LoggerFactory.getLogger(FiltrationSpecification.class);

    public Specification<T> createSpecification(List<FilterCriteria> filterCriteriaList, Boolean andClause) {
        return (root, query, cb) -> {
            if (filterCriteriaList.isEmpty()) cb.conjunction();
            List<Predicate> predicates = new ArrayList<>();
            for (FilterCriteria filterCriteria : filterCriteriaList) {
                Predicate predicate = getPredicate(filterCriteria, cb, root);
                if (Optional.ofNullable(predicate).isPresent()) {
                    predicates.add(predicate);
                }
            }
            return andClause ? cb.and(predicates.toArray(new Predicate[0]))
                    : cb.or(predicates.toArray(new Predicate[0]));
        };
    }

    private Predicate getPredicate(FilterCriteria filterCriteria, CriteriaBuilder cb, Root<T> root) {
        String field = filterCriteria.getFilterField();
        String operator = filterCriteria.getOperator();
        var value = getValueAsPerJavaDataType(filterCriteria.getValue(), root, field);

        Expression<String> tableField = root.get(field);
        Expression<LocalDate> dateExpression = cb.function("DATE", LocalDate.class, tableField);
        return switch (operator) {
            case FilterOperations.IS_NULL -> cb.isNull(tableField);
            case FilterOperations.EQUALS -> {
                if (value instanceof LocalDate || value instanceof LocalDateTime)
                    yield cb.equal(dateExpression, value);
                else if (value instanceof Boolean || value instanceof Number)
                    yield cb.equal(tableField, value);
                else if (value instanceof String)
                    yield cb.equal(cb.lower(cb.trim(tableField)), ((String) value).trim().toLowerCase());
                yield cb.equal(tableField, value);
            }
            case FilterOperations.CONTAINS -> cb.like(tableField, "%" + value + "%");
            case FilterOperations.STARTS_WITH -> cb.like(tableField, "%" + value);
            case FilterOperations.ENDS_WITH -> cb.like(tableField, value + "%");
            case FilterOperations.LESS_THAN -> {
                if (value instanceof LocalDate)
                    yield cb.lessThan(dateExpression, DateTimeParser.parseLocalDate(value.toString()));
                else if (value instanceof LocalDateTime)
                    yield cb.lessThan(dateExpression, DateTimeParser.parseLocalDate(value.toString()));
                yield cb.lessThan(tableField, (Comparable) value);
            }
            case FilterOperations.LESS_THAN_OR_EQUAL_TO -> {
                if (value instanceof LocalDate)
                    yield cb.lessThanOrEqualTo(dateExpression, DateTimeParser.parseLocalDate(value.toString()));
                else if (value instanceof LocalDateTime)
                    yield cb.lessThanOrEqualTo(dateExpression, DateTimeParser.parseLocalDate(value.toString()));
                yield cb.lessThanOrEqualTo(tableField, (Comparable) value);
            }
            case FilterOperations.GREATER_THAN -> {
                if (value instanceof LocalDate)
                    yield cb.greaterThan(dateExpression, DateTimeParser.parseLocalDate(value.toString()));
                else if (value instanceof LocalDateTime)
                    yield cb.greaterThan(dateExpression, DateTimeParser.parseLocalDate(value.toString()));
                yield cb.greaterThan(tableField, (Comparable) value);
            }
            case FilterOperations.GREATER_THAN_OR_EQUAL_TO -> {
                if (value instanceof LocalDate)
                    yield cb.greaterThanOrEqualTo(dateExpression, DateTimeParser.parseLocalDate(value.toString()));
                else if (value instanceof LocalDateTime)
                    yield cb.greaterThanOrEqualTo(dateExpression, DateTimeParser.parseLocalDate(value.toString()));
                yield cb.greaterThanOrEqualTo(tableField, (Comparable) value);
            }
            default -> cb.notEqual(tableField, value);
        };
    }

    private Object getValueAsPerJavaDataType(Object value, Root<T> root, String field) {
        if (Optional.ofNullable(value).isEmpty()) return null;
        else if (root.get(field).getJavaType().equals(Integer.class) && value instanceof String) {
            return Integer.valueOf(value.toString());
        } else if (root.get(field).getJavaType().equals(Long.class) && value instanceof String) {
            return Long.valueOf(value.toString());
        } else if (root.get(field).getJavaType().equals(Float.class) && value instanceof String) {
            return Float.valueOf(value.toString());
        } else if (root.get(field).getJavaType().equals(Double.class) && value instanceof String) {
            return Double.valueOf(value.toString());
        } else if (root.get(field).getJavaType().equals(BigDecimal.class) && value instanceof String) {
            return BigDecimal.valueOf(Long.parseLong(value.toString()));
        } else if (root.get(field).getJavaType().equals(LocalDate.class) && value instanceof String) {
            return DateTimeParser.parseLocalDate(value.toString());
        } else if (root.get(field).getJavaType().equals(LocalDateTime.class) && value instanceof String) {
            return DateTimeParser.parseLocalDateTime(value.toString());
        } else if (root.get(field).getJavaType().equals(Boolean.class) && value instanceof String) {
            return Boolean.parseBoolean(value.toString());
        } else return value;
    }
}
