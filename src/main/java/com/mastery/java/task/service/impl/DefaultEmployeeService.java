package com.mastery.java.task.service.impl;

import com.mastery.java.task.dto.EmployeeDto;
import com.mastery.java.task.jpa.entity.Employee;
import com.mastery.java.task.exceptions.NotFoundException;
import com.mastery.java.task.jpa.EmployeeRepository;
import com.mastery.java.task.service.EmployeeService;
import com.mastery.java.task.service.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class DefaultEmployeeService implements EmployeeService {

    private EmployeeRepository repository;

    public DefaultEmployeeService(@Autowired EmployeeRepository repository){
        this.repository = repository;
    }

    @Override
    public List<EmployeeDto> employeeList(Map<String, String> params) {
        if(params == null || params.get("firstName") == null || params.get("secondName") == null){
            return repository.findAll().stream().map(Mapper::mapToDto).collect(Collectors.toList());
        } else {
            return repository.findByFirstNameAndSecondName(params.get("firstName"), params.get("secondName"))
                    .stream().map(Mapper::mapToDto).collect(Collectors.toList());
        }
    }

    @Override
    public EmployeeDto employeeById(Long id) {
        if(id == null){
            throw new NotFoundException("Id is null");
        }
        return repository.findById(id).map(Mapper::mapToDto).orElseThrow(NotFoundException::new);
    }

    @Override
    public EmployeeDto createEmployee(EmployeeDto employeeDto) {
        if(employeeDto == null){
            throw new NotFoundException("Employee is null");
        }
        employeeDto.setEmployeeId(null);
        Employee employee = repository.saveAndFlush(Mapper.mapToEntity(employeeDto));
        return Mapper.mapToDto(employee);
    }

    @Override
    public EmployeeDto updateEmployee(Long id, EmployeeDto employeeDto) {
        if(id == null || employeeDto == null){
            throw new NotFoundException("Id or employee is null");
        }
        employeeDto.setEmployeeId(id);
        Employee employee = repository.saveAndFlush(Mapper.mapToEntity(employeeDto));
        return Mapper.mapToDto(employee);
    }

    @Override
    public void deleteEmployee(Long id) {
        if(id == null){
            throw new NotFoundException("Id is null");
        }
        repository.deleteById(id);
    }
}
