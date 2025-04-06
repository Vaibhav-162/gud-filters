package com.shin.multi_filters.common.repository;

import com.shin.multi_filters.common.FilterCriteria;
import com.shin.multi_filters.common.FilterPageAndSortResponse;
import com.shin.multi_filters.common.SortCriteria;
import com.shin.multi_filters.common.util.FiltrationSpecification;
import com.shin.multi_filters.common.util.Pagination;
import com.shin.multi_filters.common.util.ShinSort;
import jakarta.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.JpaMetamodelEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.swing.text.html.Option;
import java.awt.print.Pageable;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;


public class FilterPageAndSortRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements FilterPageAndSortRepository<T, ID> {
    private FiltrationSpecification<T> filtrationSpecification;
    private Pagination<T> paginationSpec;
    private ShinSort<T> sortSpec;
    private EntityManager entityManager;
    private JpaMetamodelEntityInformation<T, ?> entityInformation;
    private Integer totalPageCount;
    private Long totalRows;
    Logger logger = LoggerFactory.getLogger(FilterPageAndSortRepositoryImpl.class);


    public FilterPageAndSortRepositoryImpl(JpaMetamodelEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityInformation = entityInformation;
        this.entityManager = entityManager;
        this.filtrationSpecification = new FiltrationSpecification<T>();
        this.sortSpec = new ShinSort<T>();
        this.paginationSpec = new Pagination<T>();
    }

    @Override
    public FilterPageAndSortResponse<T> findAllByCriteria(List<FilterCriteria> filterCriteriaList) {
        return findAllByCriteria(filterCriteriaList, false, null, null, null);
    }

    @Override
    public FilterPageAndSortResponse<T> findAllByCriteria(List<FilterCriteria> filterCriteriaList, Integer pageNumber, Integer pageSize) {
        return findAllByCriteria(filterCriteriaList, false, pageNumber, pageSize, null);
    }

    @Override
    public FilterPageAndSortResponse<T> findAllByCriteria(List<FilterCriteria> filterCriteriaList, Integer pageNumber, Integer pageSize, List<SortCriteria> sortCriteriaList) {
        return findAllByCriteria(filterCriteriaList, false, pageNumber, pageSize, sortCriteriaList);
    }

    @Override
    public FilterPageAndSortResponse<T> findAllByCriteria(List<FilterCriteria> filterCriteriaList, Boolean andClause, Integer pageNumber, Integer pageSize, List<SortCriteria> sortCriteriaList) {
        Sort sort = sortSpec.sortByCriteria(sortCriteriaList);
        Pageable page = paginationSpec.paginate(Integer pageNumber, Integer pageSize, Sort sort);
        Specification<T> spec = Optional.ofNullable(filterCriteriaList).isEmpty() || filterCriteriaList.isEmpty()
                ? null
                : filtrationSpecification.createSpecification(filterCriteriaList, andClause);
        return new FilterPageAndSortResponse<T>(buildQuery(spec, page));
    }

    private Page<T> buildQuery(Specification<T> spec, Pageable page) {
        return null;
    }

}
