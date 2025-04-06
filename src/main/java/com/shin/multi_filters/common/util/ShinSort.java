package com.shin.multi_filters.common.util;

import com.shin.multi_filters.common.SortCriteria;
import com.shin.multi_filters.common.SortOperations;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Root;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import java.util.List;
import java.util.Optional;

public class ShinSort<T> {
    Logger logger = LoggerFactory.getLogger(ShinSort.class);
    public Sort createSort(List<SortCriteria> sortCriteriaList) {
        Sort sort = Sort.unsorted();
        if (Optional.ofNullable(sortCriteriaList).isEmpty() || sortCriteriaList.isEmpty()) {
            return sort;
        }
        for (SortCriteria sortCriteria : sortCriteriaList) {
            Sort.Order order = sortCriteria.getOrder().equalsIgnoreCase(SortOperations.DESC)
                    ? Sort.Order.desc(sortCriteria.getField())
                    : Sort.Order.asc(sortCriteria.getField());
            sort = sort.and(Sort.by(order));
        }
        return sort;
    }

    public List<Order> buildOrderBy(Sort sort, Root<T> root, CriteriaBuilder criteriaBuilder) {
        sort.forEach(crt -> logger.info("Sorting criteria : {} {}", crt.getProperty(), crt.getDirection()));
        return sort.stream().map(criteria ->
                criteria.isAscending()
                        ? criteriaBuilder.asc(root.get(criteria.getProperty()))
                        : criteriaBuilder.desc(root.get(criteria.getProperty()))

        ).toList();
    }
}
