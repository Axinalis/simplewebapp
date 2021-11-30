package com.mastery.java.task.dao;

import com.mastery.java.task.dto.Employee;

import java.util.List;

public interface EmployeeDao {

    List<Employee> list();

    Employee get(Long id);

    Employee create(Employee employee);

    Employee update(Long id, Employee employee);

    void delete(Long id);

}
