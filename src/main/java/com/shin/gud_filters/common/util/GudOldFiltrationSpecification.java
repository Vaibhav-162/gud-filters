package com.shin.gud_filters.common.util;

import com.shin.gud_filters.common.FilterCriteria;
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
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Component
public class GudOldFiltrationSpecification {
    Logger logger = LoggerFactory.getLogger(GudOldFiltrationSpecification.class);

    public static <T> Specification<T> createSpecification(List<FilterCriteria> filterCriteriaList, Boolean andClause) {
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

    private static <T> Predicate getPredicate(FilterCriteria filterCriteria, CriteriaBuilder cb, Root<T> root) {
        String field = filterCriteria.getFilterField();
        String operator = filterCriteria.getOperator();
        var value = getValueAsPerJavaDataType(filterCriteria.getValue(), root, field);

        Expression<LocalDate> dateExpression = cb.function("DATE", LocalDate.class, root.get(field));
        return switch (operator) {
            case FilterCriteria.FilterOperations.IS_NULL -> cb.isNull(root.get(field));
            case FilterCriteria.FilterOperations.EQUALS -> {
                if (value instanceof LocalDate || value instanceof LocalDateTime)
                    yield cb.equal(dateExpression, value);
                else if (value instanceof Boolean || value instanceof Number)
                    yield cb.equal(root.get(field), value);
                else if (value instanceof String)
                    yield cb.equal(cb.lower(cb.trim(root.get(field))), ((String) value).trim().toLowerCase());
                yield cb.equal(root.get(field), value);
            }
            case FilterCriteria.FilterOperations.CONTAINS -> cb.like(root.get(field), "%" + value + "%");
            case FilterCriteria.FilterOperations.STARTS_WITH -> cb.like(root.get(field), "%" + value);
            case FilterCriteria.FilterOperations.ENDS_WITH -> cb.like(root.get(field), value + "%");
            case FilterCriteria.FilterOperations.LESS_THAN -> {
                if (value instanceof LocalDate)
                    yield cb.lessThan(dateExpression, (LocalDate) DateTimeParser.smartParse(value.toString()));
                else if (value instanceof LocalDateTime)
                    yield cb.lessThan(root.get(field), (LocalDateTime) DateTimeParser.smartParse(value.toString()));
                yield cb.lessThan(root.get(field), (Comparable) value);
            }
            case FilterCriteria.FilterOperations.LESS_THAN_OR_EQUAL_TO -> {
                if (value instanceof LocalDate)
                    yield cb.lessThanOrEqualTo(dateExpression, (LocalDate) DateTimeParser.smartParse(value.toString()));
                else if (value instanceof LocalDateTime)
                    yield cb.lessThanOrEqualTo(root.get(field), (LocalDateTime) DateTimeParser.smartParse(value.toString()));
                yield cb.lessThanOrEqualTo(root.get(field), (Comparable) value);
            }
            case FilterCriteria.FilterOperations.GREATER_THAN -> {
                if (value instanceof LocalDate)
                    yield cb.greaterThan(dateExpression, (LocalDate) DateTimeParser.smartParse(value.toString()));
                else if (value instanceof LocalDateTime)
                    yield cb.greaterThan(root.get(field), (LocalDateTime) DateTimeParser.smartParse(value.toString()));
                yield cb.greaterThan(root.get(field), (Comparable) value);
            }
            case FilterCriteria.FilterOperations.GREATER_THAN_OR_EQUAL_TO -> {
                if (value instanceof LocalDate)
                    yield cb.greaterThanOrEqualTo(dateExpression, (LocalDate) DateTimeParser.smartParse(value.toString()));
                else if (value instanceof LocalDateTime)
                    yield cb.greaterThanOrEqualTo(root.get(field), (LocalDateTime) DateTimeParser.smartParse(value.toString()));
                yield cb.greaterThanOrEqualTo(root.get(field), (Comparable) value);
            }
            default -> cb.notEqual(root.get(field), value);
        };
    }

    private static <T> Object getValueAsPerJavaDataType(Object value, Root<T> root, String field) {
        Class<?> targetType = root.get(field).getJavaType();
        if (value == null) return null;
        if (!(value instanceof String)) return value;
        String stringValue = value.toString();

        Map<Class<?>, Function<String, ?>> converters = Map.of(
                Integer.class, Integer::valueOf,
                Long.class, Long::valueOf,
                Float.class, Float::valueOf,
                Double.class, Double::valueOf,
                BigDecimal.class, s -> BigDecimal.valueOf(Long.parseLong(s)),
                LocalDate.class, DateTimeParser::smartParse,
                LocalDateTime.class, DateTimeParser::smartParse,
                Boolean.class, Boolean::parseBoolean
        );
        return converters.getOrDefault(targetType, s -> s).apply(stringValue);
    }
}
