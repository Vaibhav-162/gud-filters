package com.shin.gud_filters.repository;

import com.shin.gud_filters.common.repository.GudFiltersRepository;
import com.shin.gud_filters.model.Employee;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends GudFiltersRepository<Employee, Long> {
}
