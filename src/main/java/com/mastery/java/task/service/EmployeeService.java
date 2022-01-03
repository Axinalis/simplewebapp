package com.mastery.java.task.service;

import com.mastery.java.task.dto.Employee;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

public interface EmployeeService {

    List<Employee> employeeList(Map<String, String> params);
    Employee employeeById(Long id);
    Employee createEmployee( Employee employee);
    Employee updateEmployee(Long id, Employee employee);
    void deleteEmployee(Long id);

}
