package com.mastery.java.task.rest;

import com.mastery.java.task.dto.EmployeeDto;
import com.mastery.java.task.service.EmployeeService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("employee")
public class EmployeeController {

    private EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public List<EmployeeDto> employeeList(@RequestParam Map<String, String> params) {
        return employeeService.employeeList(params);
    }

    @GetMapping("{id}")
    public EmployeeDto employeeById(@PathVariable Long id){
        return employeeService.employeeById(id);
    }

    @PostMapping()
    public EmployeeDto createEmployee(@RequestBody @Valid EmployeeDto employee){
        return employeeService.createEmployee(employee);
    }

    @PutMapping("{id}")
    public EmployeeDto updateEmployee(@PathVariable Long id, @RequestBody @Valid EmployeeDto employee){
        return employeeService.updateEmployee(id, employee);
    }

    @DeleteMapping("{id}")
    public void deleteEmployee(@PathVariable Long id){
        employeeService.deleteEmployee(id);
    }

}
