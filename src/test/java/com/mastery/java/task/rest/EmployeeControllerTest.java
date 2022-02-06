package com.mastery.java.task.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mastery.java.task.dto.EmployeeDto;
import com.mastery.java.task.exceptions.EmployeeNotFoundException;
import com.mastery.java.task.repository.EmployeeRepository;
import com.mastery.java.task.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private EmployeeService service;
    @MockBean
    private EmployeeRepository repository;

    @Test
    public void testGetListWithoutNames() throws Exception {
        when(service.employeeList(new HashMap<>())).thenReturn(Arrays.asList(createInvalidEmployeeDto()));

        mockMvc.perform(get("/employee"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void testGetListWithNames() throws Exception {
        String firstName = "Ivan";
        String secondName = "Ivanov";
        Map<String, String> params = new HashMap<>();
        params.put("firstName", firstName);
        params.put("secondName", secondName);

        when(service.employeeList(params)).thenReturn(Arrays.asList(createValidEmployeeDto()));

        //Check get method with path "/employee" and with names parameters
        String parameters = String.format("/employee?firstName=%s&secondName=%s", firstName, secondName);
        mockMvc.perform(get(parameters))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].firstName").value(firstName))
                .andExpect(jsonPath("$[0].secondName").value(secondName));

    }

    @Test
    public void testGetEmployeeById() throws Exception {
        EmployeeDto employeeDto = createValidEmployeeDto();
        Long id = employeeDto.getEmployeeId();
        when(service.employeeById(id)).thenReturn(employeeDto);

        mockMvc.perform(get("/employee/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("employeeId").isNotEmpty())
                .andExpect(jsonPath("firstName").value(employeeDto.getFirstName()))
                .andExpect(jsonPath("secondName").value(employeeDto.getSecondName()))
                .andExpect(jsonPath("gender").value(employeeDto.getGender()))
                .andExpect(jsonPath("jobTitle").value(employeeDto.getJobTitle()))
                .andExpect(jsonPath("departmentId").value(employeeDto.getDepartmentId()))
                .andExpect(jsonPath("dateOfBirth").value(employeeDto.getDateOfBirth().toString()));
    }

    @Test
    public void testShouldReturn404WhenEmployeesNotFound() throws Exception {
        Long nonExistentId = 100L;
        when(service.employeeById(nonExistentId)).thenThrow(new EmployeeNotFoundException());

        mockMvc.perform(get("/employee/" + nonExistentId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("status").value("404 NOT_FOUND"));
    }

    @Test
    public void testCreate() throws Exception {
        EmployeeDto employeeDto = createValidEmployeeDto();
        when(service.createEmployee(employeeDto)).thenReturn(employeeDto);

        mockMvc.perform(post("/employee")
                        .contentType("application/json")
                        .content(mapToJson(employeeDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("employeeId").isNotEmpty())
                .andExpect(jsonPath("firstName").value(employeeDto.getFirstName()))
                .andExpect(jsonPath("secondName").value(employeeDto.getSecondName()))
                .andExpect(jsonPath("gender").value(employeeDto.getGender()))
                .andExpect(jsonPath("jobTitle").value(employeeDto.getJobTitle()))
                .andExpect(jsonPath("departmentId").value(employeeDto.getDepartmentId()))
                .andExpect(jsonPath("dateOfBirth").value(employeeDto.getDateOfBirth().toString()));
    }

    @Test
    public void testCreateValidation() throws Exception {
        EmployeeDto employeeDto = createInvalidEmployeeDto();

        mockMvc.perform(post("/employee")
                        .contentType("application/json")
                        .content(mapToJson(employeeDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value("400 BAD_REQUEST"))
                .andExpect(jsonPath("message").value("Some of the fields are not valid"))
                .andExpect(jsonPath("details").isNotEmpty())
                .andExpect(jsonPath("$.details.firstName").value("must not be blank"))
                .andExpect(jsonPath("$.details.gender").value("Gender is not valid"))
                .andExpect(jsonPath("$.details.departmentId").value("must not be null"))
                .andExpect(jsonPath("$.details.jobTitle").value("must not be blank"))
                .andExpect(jsonPath("$.details.dateOfBirth").value("Person is not an adult"));;
    }

    @Test
    public void testCreateEmployeeWithNullFields() throws Exception {
        EmployeeDto employeeDto = new EmployeeDto();

        mockMvc.perform(post("/employee")
                        .contentType("application/json")
                        .content(mapToJson(employeeDto)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testUpdate() throws Exception {
        EmployeeDto employee = createValidEmployeeDto();
        Long id = employee.getEmployeeId();
        when(service.updateEmployee(id, employee)).thenReturn(employee);

        mockMvc.perform(put("/employee/" + id)
                        .contentType("application/json")
                        .content(mapToJson(employee)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("employeeId").isNotEmpty())
                .andExpect(jsonPath("firstName").value(employee.getFirstName()))
                .andExpect(jsonPath("secondName").value(employee.getSecondName()))
                .andExpect(jsonPath("gender").value(employee.getGender()))
                .andExpect(jsonPath("jobTitle").value(employee.getJobTitle()))
                .andExpect(jsonPath("departmentId").value(employee.getDepartmentId()))
                .andExpect(jsonPath("dateOfBirth").value(employee.getDateOfBirth().toString()));
        //Isn't it worse, than simply convert object from json and compare it with "equals"?
    }

    @Test
    public void testUpdateValidation() throws Exception {
        EmployeeDto employee = createInvalidEmployeeDto();

        mockMvc.perform(put("/employee/" + employee.getEmployeeId())
                        .contentType("application/json")
                        .content(mapToJson(employee)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value("400 BAD_REQUEST"))
                .andExpect(jsonPath("message").value("Some of the fields are not valid"))
                .andExpect(jsonPath("details").isNotEmpty())
                .andExpect(jsonPath("$.details.firstName").value("must not be blank"))
                .andExpect(jsonPath("$.details.gender").value("Gender is not valid"))
                .andExpect(jsonPath("$.details.departmentId").value("must not be null"))
                .andExpect(jsonPath("$.details.jobTitle").value("must not be blank"))
                .andExpect(jsonPath("$.details.dateOfBirth").value("Person is not an adult"));
    }

    //this test is not working yet, but as soon as I'll merge this branch with master, everything will work fine
    @Test
    public void testPathVariableValidation() throws Exception{
        mockMvc.perform(get("/employee/wrongVariable"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testDelete() throws Exception {
        Long id = 1L;

        mockMvc.perform(delete("/employee/" + id))
                .andExpect(status().isOk());

        verify(service).deleteEmployee(id);
    }

    private String mapToJson(Object object){
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "";
    }

    private <T> T mapFromJson(String json, TypeReference<T> reference){
        try{
            return mapper.readValue(json, reference);
        } catch (JsonProcessingException e){
            e.printStackTrace();
        }
        return null;
    }

    private EmployeeDto createValidEmployeeDto(){
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setEmployeeId(1L);
        employeeDto.setFirstName("Ivan");
        employeeDto.setSecondName("Ivanov");
        employeeDto.setGender("MALE");
        employeeDto.setDepartmentId(1L);
        employeeDto.setJobTitle("Manager");
        employeeDto.setDateOfBirth(LocalDate.of(2001, 12, 15));
        return employeeDto;
    }

    private EmployeeDto createInvalidEmployeeDto(){
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setEmployeeId(1L);
        employeeDto.setFirstName("");
        employeeDto.setSecondName("");
        employeeDto.setGender("NONE");
        employeeDto.setDepartmentId(null);
        employeeDto.setJobTitle("");
        employeeDto.setDateOfBirth(LocalDate.of(2011, 12, 15));
        return employeeDto;
    }
}
