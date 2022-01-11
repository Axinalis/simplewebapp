package com.mastery.java.task.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mastery.java.task.dto.EmployeeDto;
import com.mastery.java.task.dto.Gender;
import com.mastery.java.task.exceptions.EmployeeNotFoundException;
import com.mastery.java.task.repository.EmployeeRepository;
import com.mastery.java.task.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@AutoConfigureMockMvc
public class EmployeeControllerTest {

    @MockBean
    private EmployeeService service;
    @MockBean
    private EmployeeRepository repository;
    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper mapper;
    private List<EmployeeDto> list;
    //Indexes for list. Shows what element every method should work with
    private int forNameSearch = 0;
    private int forIdSearch = 1;
    private int forCreating = 2;
    private int forUpdating = 3;
    private int forDeleting = 4;
    private int forCreateValidation = 5;
    private int forUpdateValidation = 6;
    private Long nonExistentId = 100L;

    @BeforeEach
    public void setup(){
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        list = new ArrayList<>();
        String male = Gender.MALE.toString();
        String female = Gender.FEMALE.toString();
        String[] firstNames = {"Anton", "Nikolay", "Andrey", "Natalia", "Egor", "", ""};
        String[] secondNames = {"Trus", "Golubov", "Shulgach", "Mironova", "Pomidorov", "Smyrnova", "Vasyliev"};
        String[] jobTitles = {"Developer", "Project manager", "Team lead", "HR", "Business analyst", "", ""};
        String[] genders = {male, male, male, female, male, "notMale", "man"};
        LocalDate[] dates = {
                LocalDate.of(2000, 5, 29),
                LocalDate.of(1998, 8, 20),
                LocalDate.of(1985, 1, 2),
                LocalDate.of(1995, 4, 15),
                LocalDate.of(2001, 2, 8),
                LocalDate.of(2005, 11, 1),
                LocalDate.of(2005, 12, 21)
        };
        Long[] departments = {5L, 3L, 3L, 4L, 10L, null, null};

        for(int i = 0; i < 7; i++){
            EmployeeDto employee = new EmployeeDto((long)i + 1, firstNames[i], genders[i]);
            employee.setSecondName(secondNames[i]);
            employee.setDateOfBirth(dates[i]);
            employee.setJobTitle(jobTitles[i]);
            employee.setDepartmentId(departments[i]);

            list.add(employee);
        }
    }

    @Test
    public void testGetListWithoutNames() throws Exception {
        when(service.employeeList(new HashMap<>())).thenReturn(list);

        mockMvc.perform(get("/employee"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void testGetListWithNames() throws Exception {
        String firstName = list.get(forNameSearch).getFirstName();
        String secondName = list.get(forNameSearch).getSecondName();
        Map<String, String> params = new HashMap<>();
        params.put("firstName", firstName);
        params.put("secondName", secondName);

        when(service.employeeList(params)).thenReturn(list.subList(forNameSearch, forNameSearch + 1));

        //Check get method with path "/employee" and with names parameters
        String parameters = String.format("?firstName=%s&secondName=%s", firstName, secondName);
        mockMvc.perform(get("/employee" + parameters))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].firstName").value(firstName))
                .andExpect(jsonPath("$[0].secondName").value(secondName));

    }

    @Test
    public void testGetEmployeeById() throws Exception {
        EmployeeDto employee = list.get(forIdSearch);
        Long id = employee.getEmployeeId();
        when(service.employeeById(id)).thenReturn(employee);

        mockMvc.perform(get("/employee/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("employeeId").isNotEmpty())
                .andExpect(jsonPath("firstName").value(employee.getFirstName()))
                .andExpect(jsonPath("secondName").value(employee.getSecondName()))
                .andExpect(jsonPath("gender").value(employee.getGender()))
                .andExpect(jsonPath("jobTitle").value(employee.getJobTitle()))
                .andExpect(jsonPath("departmentId").value(employee.getDepartmentId()))
                .andExpect(jsonPath("dateOfBirth").value(employee.getDateOfBirth().toString()));
    }

    @Test
    public void testShouldReturn404WhenEmployeesNotFound() throws Exception {
        when(service.employeeById(nonExistentId)).thenThrow(new EmployeeNotFoundException());

        mockMvc.perform(get("/employee/" + nonExistentId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("status").value("404 NOT_FOUND"));
    }

    @Test
    public void testCreate() throws Exception {
        EmployeeDto employee = list.get(forCreating);
        when(service.createEmployee(employee)).thenReturn(employee);

        mockMvc.perform(post("/employee")
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
    }

    @Test
    public void testCreateValidation() throws Exception {
        EmployeeDto employee = list.get(forCreateValidation);

        mockMvc.perform(post("/employee")
                        .contentType("application/json")
                        .content(mapToJson(employee)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value("400 BAD_REQUEST"))
                .andExpect(jsonPath("details").isNotEmpty())
                .andExpect(jsonPath("$.details.firstName").isNotEmpty())
                .andExpect(jsonPath("$.details.gender").isNotEmpty())
                .andExpect(jsonPath("$.details.departmentId").isNotEmpty())
                .andExpect(jsonPath("$.details.jobTitle").isNotEmpty())
                .andExpect(jsonPath("$.details.dateOfBirth").isNotEmpty());
    }

    @Test
    public void testUpdate() throws Exception {
        EmployeeDto employee = list.get(forUpdating);
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
        EmployeeDto employee = list.get(forUpdateValidation);

        mockMvc.perform(put("/employee/" + employee.getEmployeeId())
                        .contentType("application/json")
                        .content(mapToJson(employee)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status").value("400 BAD_REQUEST"))
                .andExpect(jsonPath("details").isNotEmpty())
                .andExpect(jsonPath("$.details.firstName").isNotEmpty())
                .andExpect(jsonPath("$.details.gender").isNotEmpty())
                .andExpect(jsonPath("$.details.departmentId").isNotEmpty())
                .andExpect(jsonPath("$.details.jobTitle").isNotEmpty())
                .andExpect(jsonPath("$.details.dateOfBirth").isNotEmpty());
    }

    @Test
    public void testDelete() throws Exception {
        Long id = list.get(forDeleting).getEmployeeId();

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
}
