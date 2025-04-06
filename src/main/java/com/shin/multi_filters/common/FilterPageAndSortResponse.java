package com.shin.multi_filters.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilterPageAndSortResponse<T> {
    private Page<T> resultList;

}
