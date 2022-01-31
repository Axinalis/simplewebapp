package com.mastery.java.task.service.impl;

import com.mastery.java.task.dto.EmployeeDto;
import com.mastery.java.task.entity.Employee;
import com.mastery.java.task.exceptions.EmployeeNotFoundException;
import com.mastery.java.task.repository.EmployeeRepository;
import com.mastery.java.task.service.EmployeeMapper;
import com.mastery.java.task.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DefaultEmployeeService implements EmployeeService {

    private EmployeeRepository repository;

    public DefaultEmployeeService(@Autowired EmployeeRepository repository){
        this.repository = repository;
    }

    @Override
    public List<EmployeeDto> employeeList(Map<String, String> params) {
        if(params == null || params.get("firstName") == null || params.get("secondName") == null){
            return repository.findAll().stream().map(EmployeeMapper::mapToDto).collect(Collectors.toList());
        } else {
            return repository.findByFirstNameAndSecondName(params.get("firstName"), params.get("secondName"))
                    .stream().map(EmployeeMapper::mapToDto).collect(Collectors.toList());
        }
    }

    @Override
    public EmployeeDto employeeById(Long id) {
        if(id == null){
            throw new EmployeeNotFoundException("Id is null");
        }
        return repository.findById(id).map(EmployeeMapper::mapToDto).orElseThrow(EmployeeNotFoundException::new);
    }

    @Override
    public EmployeeDto createEmployee(EmployeeDto employeeDto) {
        if(employeeDto == null){
            throw new EmployeeNotFoundException("Employee is null");
        }
        employeeDto.setEmployeeId(null);
        Employee employee = repository.save(EmployeeMapper.mapToEntity(employeeDto));
        return EmployeeMapper.mapToDto(employee);
    }

    @Override
    public EmployeeDto updateEmployee(Long id, EmployeeDto employeeDto) {
        if(id == null || employeeDto == null){
            throw new EmployeeNotFoundException("Id or employee is null");
        }
        employeeDto.setEmployeeId(id);
        Employee employee = repository.save(EmployeeMapper.mapToEntity(employeeDto));
        return EmployeeMapper.mapToDto(employee);
    }

    @Override
    public void deleteEmployee(Long id) {
        if(id == null){
            throw new EmployeeNotFoundException("Id is null");
        }
        repository.deleteById(id);
    }
}
