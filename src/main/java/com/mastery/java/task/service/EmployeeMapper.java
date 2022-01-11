package com.mastery.java.task.service;

import com.mastery.java.task.dto.EmployeeDto;
import com.mastery.java.task.dto.Gender;
import com.mastery.java.task.entity.Employee;

public class EmployeeMapper {

    public static EmployeeDto mapToDto(Employee employee){
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setEmployeeId(employee.getEmployeeId());
        employeeDto.setFirstName(employee.getFirstName());
        employeeDto.setSecondName(employee.getSecondName());
        employeeDto.setGender(employee.getGender().toString());
        employeeDto.setDateOfBirth(employee.getDateOfBirth());
        employeeDto.setDepartmentId(employee.getDepartmentId());
        employeeDto.setJobTitle(employee.getJobTitle());
        return employeeDto;
    }

    public static Employee mapToEntity(EmployeeDto employeeDto){
        Employee employee = new Employee();
        employee.setEmployeeId(employeeDto.getEmployeeId());
        employee.setFirstName(employeeDto.getFirstName());
        employee.setSecondName(employeeDto.getSecondName());
        employee.setGender(Gender.valueOf(employeeDto.getGender()));
        employee.setDateOfBirth(employeeDto.getDateOfBirth());
        employee.setDepartmentId(employeeDto.getDepartmentId());
        employee.setJobTitle(employeeDto.getJobTitle());
        return employee;
    }
}
