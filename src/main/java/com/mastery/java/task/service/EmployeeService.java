package com.mastery.java.task.service;

import com.mastery.java.task.dto.Employee;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface EmployeeService {

    List<Employee> employeeList();
    Employee employeeById(@PathVariable Long id);
    Employee createEmployee(@RequestBody Employee employee);
    Employee updateEmployee(@PathVariable Long id, @RequestBody Employee employee);
    void deleteEmployee(@PathVariable Long id);

}
