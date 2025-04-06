package com.shin.multi_filters.repository;

import com.shin.multi_filters.common.repository.FilterPageAndSortRepository;
import com.shin.multi_filters.model.Employee;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends FilterPageAndSortRepository<Employee, Long> {
}
