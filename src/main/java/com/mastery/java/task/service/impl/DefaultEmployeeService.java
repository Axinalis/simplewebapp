package com.mastery.java.task.service.impl;

import com.mastery.java.task.dao.EmployeeDao;
import com.mastery.java.task.dto.Employee;
import com.mastery.java.task.exceptions.NotFoundException;
import com.mastery.java.task.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

public class DefaultEmployeeService implements EmployeeService {

    private EmployeeDao employeeDao;

    public DefaultEmployeeService(EmployeeDao employeeDao){
        this.employeeDao = employeeDao;
    }

    @Override
    public List<Employee> employeeList() {
        return employeeDao.list();
    }

    @Override
    public Employee employeeById(Long id) {
        if(id == null){
            throw new NotFoundException("Id is null");
        }
        return employeeDao.get(id);
    }

    @Override
    public Employee createEmployee(Employee employee) {
        if(employee == null){
            throw new NotFoundException("Employee is null");
        }
        return employeeDao.create(employee);
    }

    @Override
    public Employee updateEmployee(Long id, Employee employee) {
        if(id == null || employee == null){
            throw new NotFoundException("Id or employee is null");
        }
        return employeeDao.update(id, employee);
    }

    @Override
    public void deleteEmployee(Long id) {
        if(id == null){
            throw new NotFoundException("Id is null");
        }
        employeeDao.delete(id);
    }
}
