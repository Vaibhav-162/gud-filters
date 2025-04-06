package com.shin.multi_filters.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilterSortAndPageRequest {
    private List<FilterCriteria> filterCriteriaList;
    private boolean andClause;
    private Integer pageNumber;
    private Integer pageSize;
    private List<SortCriteria> sortCriteriaList;
}
