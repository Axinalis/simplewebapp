package com.mastery.java.task.service;

import com.mastery.java.task.dto.EmployeeDto;
import com.mastery.java.task.jpa.entity.Employee;

import java.util.List;
import java.util.Map;

public interface EmployeeService {

    List<EmployeeDto> employeeList(Map<String, String> params);
    EmployeeDto employeeById(Long id);
    EmployeeDto createEmployee(EmployeeDto employee);
    EmployeeDto updateEmployee(Long id, EmployeeDto employee);
    void deleteEmployee(Long id);

}
