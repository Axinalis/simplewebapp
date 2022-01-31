package com.mastery.java.task.rest;

import com.mastery.java.task.dto.EmployeeDto;
import com.mastery.java.task.service.EmployeeService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("employee")
public class EmployeeController {

    private EmployeeService employeeService;
    private static Logger log = LogManager.getLogger(EmployeeController.class);

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public List<EmployeeDto> employeeList(@RequestParam Map<String, String> params) {
        log.info("Request for list of employees came, parameters are: {}", params);
        return employeeService.employeeList(params);
    }

    @GetMapping("{id}")
    public EmployeeDto employeeById(@PathVariable Long id){
        log.info("Employee with {} id was requested", id);
        return employeeService.employeeById(id);
    }

    @PostMapping
    public EmployeeDto createEmployee(@RequestBody @Valid EmployeeDto employee){
        log.info("Request to create new employee came, fields are: {}", employee);
        return employeeService.createEmployee(employee);
    }

    @PutMapping("{id}")
    public EmployeeDto updateEmployee(@PathVariable Long id, @RequestBody @Valid EmployeeDto employee){
        log.info("Request to update employee with id {} came, fields are: {}", id, employee);
        return employeeService.updateEmployee(id, employee);
    }

    @DeleteMapping("{id}")
    public void deleteEmployee(@PathVariable Long id){
        log.info("Request to delete employee with id {} came", id);
        employeeService.deleteEmployee(id);
    }

}
