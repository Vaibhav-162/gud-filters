package com.shin.gud_filters.common;

import lombok.Data;
import java.util.List;

@Data
public class FilterSortAndPageRequest {
    private List<FilterCriteria> filterCriteriaList;
    private Boolean andClause;
    private Integer pageNumber;
    private Integer pageSize;
    private List<SortCriteria> sortCriteriaList;
}
