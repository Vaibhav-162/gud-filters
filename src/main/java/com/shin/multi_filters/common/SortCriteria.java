package com.shin.multi_filters.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SortCriteria {
    private String field;
    private String order;
}
