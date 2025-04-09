package com.shin.gud_filters.controller;

import com.shin.gud_filters.common.FilterSortAndPageRequest;
import com.shin.gud_filters.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;
    Logger logger = LoggerFactory.getLogger(EmployeeController.class);

    @PostMapping("/getDetails")
    public Page<?> getEmployeeDetails(@RequestBody FilterSortAndPageRequest filterPageAndSortCriteria) {
        logger.info("json in param {}", filterPageAndSortCriteria);
        return employeeService.findByCriteria(filterPageAndSortCriteria);
    }
}
