package com.mastery.java.task.service.impl;

import com.mastery.java.task.dto.Employee;
import com.mastery.java.task.exceptions.NotFoundException;
import com.mastery.java.task.jpa.EmployeeRepository;
import com.mastery.java.task.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class DefaultEmployeeService implements EmployeeService {

    private EmployeeRepository repository;

    public DefaultEmployeeService(@Autowired EmployeeRepository repository){
        this.repository = repository;
    }

    @Override
    public List<Employee> employeeList(Map<String, String> params) {
        if(params == null || params.get("firstName") == null || params.get("secondName") == null){
            return repository.findAll();
        } else {
            return repository.findByFirstNameAndSecondName(params.get("firstName"), params.get("secondName"));
        }
    }

    @Override
    public Employee employeeById(Long id) {
        if(id == null){
            throw new NotFoundException("Id is null");
        }
        return repository.findById(id).orElseThrow(NotFoundException::new);
    }

    @Override
    public Employee createEmployee(Employee employee) {
        if(employee == null){
            throw new NotFoundException("Employee is null");
        }
        employee.setEmployeeId(null);
        return repository.saveAndFlush(employee);
    }

    @Override
    public Employee updateEmployee(Long id, Employee employee) {
        if(id == null || employee == null){
            throw new NotFoundException("Id or employee is null");
        }
        employee.setEmployeeId(id);
        return repository.saveAndFlush(employee);
    }

    @Override
    public void deleteEmployee(Long id) {
        if(id == null){
            throw new NotFoundException("Id is null");
        }
        repository.deleteById(id);
    }
}
