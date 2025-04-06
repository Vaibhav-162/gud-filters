package com.shin.multi_filters.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.repository.NoRepositoryBean;

@AllArgsConstructor
@NoRepositoryBean
@Data
public class FilterCriteria {
    private String filterField;
    private String operator;
    private Object value;
}
