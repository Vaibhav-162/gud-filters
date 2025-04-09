package com.shin.gud_filters.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SortCriteria {
    private String field;
    private String order;

    public static class SortOperations {
        public static final String ASC = "asc";
        public static final String DESC = "desc";
    }
}
