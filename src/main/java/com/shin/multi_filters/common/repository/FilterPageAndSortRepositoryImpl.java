package com.shin.multi_filters.common.repository;

import com.shin.multi_filters.common.FilterCriteria;
import com.shin.multi_filters.common.SortCriteria;
import com.shin.multi_filters.common.util.FiltrationSpecification;
import com.shin.multi_filters.common.util.Pagination;
import com.shin.multi_filters.common.util.ShinSort;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.JpaMetamodelEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;


public class FilterPageAndSortRepositoryImpl<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements FilterPageAndSortRepository<T, ID> {
    private final FiltrationSpecification<T> filtrationSpecification;
    private final Pagination<T> paginationSpec;
    private final ShinSort<T> sortSpec;
    private final EntityManager entityManager;
    private final JpaMetamodelEntityInformation<T, ?> entityInformation;
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
    public Page<T> findAllByCriteria(List<FilterCriteria> filterCriteriaList) {
        return findAllByCriteria(filterCriteriaList, false, null, null, null);
    }

    @Override
    public Page<T> findAllByCriteria(List<FilterCriteria> filterCriteriaList, Integer pageNumber, Integer pageSize) {
        return findAllByCriteria(filterCriteriaList, false, pageNumber, pageSize, null);
    }

    @Override
    public Page<T> findAllByCriteria(List<FilterCriteria> filterCriteriaList, Integer pageNumber, Integer pageSize, List<SortCriteria> sortCriteriaList) {
        return findAllByCriteria(filterCriteriaList, false, pageNumber, pageSize, sortCriteriaList);
    }

    @Override
    public Page<T> findAllByCriteria(List<FilterCriteria> filterCriteriaList, Boolean andClause, Integer pageNumber, Integer pageSize, List<SortCriteria> sortCriteriaList) {
        Sort sort = sortSpec.createSort(sortCriteriaList);

        Pageable page = paginationSpec.paginate(pageNumber, pageSize, sort);
        Specification<T> spec = Optional.ofNullable(filterCriteriaList).isEmpty() || filterCriteriaList.isEmpty()
                ? null
                : filtrationSpecification.createSpecification(filterCriteriaList, andClause);
        return buildQuery(spec, page);
    }

    private Page<T> buildQuery(Specification<T> spec, Pageable page) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = criteriaBuilder.createQuery(getEntityClass());
        Root<T> root = query.from(getEntityClass());

        if (Optional.ofNullable(spec).isPresent()) {
            query.where(spec.toPredicate(root, query, criteriaBuilder));
        }

        if (!page.getSort().isEmpty()) {
            query.orderBy(sortSpec.buildOrderBy(page.getSort(), root, criteriaBuilder));
        }
        return executeQuery(query, page);
    }

    private Page<T> executeQuery(CriteriaQuery<T> query, Pageable page) {
        TypedQuery<T> typedQuery = entityManager.createQuery(query);
        List<T> content = typedQuery.setFirstResult(page.getPageNumber() * page.getPageSize())
                .setMaxResults(page.getPageSize()).getResultList();
        int total = entityManager.createQuery(query).getResultList().size();
        logger.info("total elements {}", total);
        return new PageImpl<T>(content, page, total);
    }

    private Class<T> getEntityClass() {
        return entityInformation.getJavaType();
    }
}
