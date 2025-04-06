package com.shin.multi_filters.common.repository;

import com.shin.multi_filters.common.FilterCriteria;
import com.shin.multi_filters.common.FilterPageAndSortResponse;
import com.shin.multi_filters.common.SortCriteria;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;

@NoRepositoryBean
public interface FilterPageAndSortRepository<T, ID extends Serializable> extends JpaRepositoryImplementation<T, ID> {
    FilterPageAndSortResponse findAllByCriteria(List<FilterCriteria> filterCriteriaList);
    FilterPageAndSortResponse findAllByCriteria(List<FilterCriteria> filterCriteriaList, Integer pageNumber, Integer pageSize);
    FilterPageAndSortResponse findAllByCriteria(List<FilterCriteria> filterCriteriaList, Integer pageNumber, Integer pageSize, List<SortCriteria> sortCriteriaList);
    FilterPageAndSortResponse findAllByCriteria(List<FilterCriteria> filterCriteriaList, Boolean andClause, Integer pageNumber, Integer pageSize, List<SortCriteria> sortCriteriaList);

}
