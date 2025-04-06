package com.shin.multi_filters.common.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import java.util.Optional;

public class Pagination<T> {
    private final static Integer DEFAULT_PAGE_NUMBER=1;
    private final static Integer DEFAULT_PAGE_SIZE=10;
    public Pageable paginate(Integer pageNumber, Integer pageSize, Sort sort){

        if(Optional.ofNullable(pageNumber).isEmpty() || pageNumber < 0 && Optional.ofNullable(pageSize).isEmpty() || pageSize<0){
            pageSize=DEFAULT_PAGE_SIZE;
            pageNumber=DEFAULT_PAGE_NUMBER;
        }
        return PageRequest.of(pageNumber, pageSize, Optional.ofNullable(sort).isEmpty() ? org.springframework.data.domain.Sort.unsorted() : sort);
    }
}
