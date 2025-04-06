package com.shin.multi_filters.common.repository;

import com.shin.multi_filters.common.FilterCriteria;
import com.shin.multi_filters.common.SortCriteria;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.data.repository.NoRepositoryBean;
import java.io.Serializable;
import java.util.List;

@NoRepositoryBean
public interface FilterPageAndSortRepository<T, ID extends Serializable> extends JpaRepositoryImplementation<T, ID> {
    Page<T> findAllByCriteria(List<FilterCriteria> filterCriteriaList);
    Page<T> findAllByCriteria(List<FilterCriteria> filterCriteriaList, Integer pageNumber, Integer pageSize);
    Page<T> findAllByCriteria(List<FilterCriteria> filterCriteriaList, Integer pageNumber, Integer pageSize, List<SortCriteria> sortCriteriaList);
    Page<T> findAllByCriteria(List<FilterCriteria> filterCriteriaList, Boolean andClause, Integer pageNumber, Integer pageSize, List<SortCriteria> sortCriteriaList);
}
