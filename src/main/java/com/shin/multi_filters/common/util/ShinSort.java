package com.shin.multi_filters.common.util;

import com.shin.multi_filters.common.SortCriteria;
import com.shin.multi_filters.common.SortOperations;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public class ShinSort<T> {
    public Sort sortByCriteria(List<SortCriteria> sortCriteriaList) {
        Sort sort = Sort.unsorted();
        if (Optional.ofNullable(sortCriteriaList).isEmpty() || sortCriteriaList.isEmpty()) {
            return sort;
        }
        for (SortCriteria sortCriteria : sortCriteriaList) {
            String orderBy = sortCriteria.getOrder();
            Sort.Order order = orderBy.equalsIgnoreCase(SortOperations.DESC)
                    ? Sort.Order.desc(orderBy)
                    : Sort.Order.asc(orderBy);
            sort.and(Sort.by(order));
        }
        return sort;
    }
}
