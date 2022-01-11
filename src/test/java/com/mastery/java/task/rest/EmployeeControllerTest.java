package com.mastery.java.task.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mastery.java.task.dto.EmployeeDto;
import com.mastery.java.task.dto.Gender;
import com.mastery.java.task.exceptions.NotFoundException;
import com.mastery.java.task.service.EmployeeService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//This test has a lot of pre-defined IDs, it will be fixed in future
public class EmployeeControllerTest {

    @Mock
    private EmployeeService service;
    @InjectMocks
    private EmployeeController controller;
    private MockMvc mockMvc;

    private ObjectMapper mapper;
    private List<EmployeeDto> list;
    //Indexes for list. Shows what element every method should work with
    private int forNameSearch = 0;
    private int forIdSearch = 1;
    private int forCreating = 2;
    private int forUpdating = 3;
    private int forDeleting = 4;
    private Long nonExistentId = 100L;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        list = new ArrayList<>();
        String male = Gender.MALE.toString();
        String female = Gender.FEMALE.toString();
        String[] firstNames = {"Anton", "Nikolay", "Andrey", "Natalia", "Egor"};
        String[] secondNames = {"Trus", "Golubov", "Shulgach", "Mironova", "Pomidorov"};
        String[] jobTitles = {"Developer", "Project manager", "Team lead", "HR", "Business analyst"};
        String[] genders = {male, male, male, female, male};
        LocalDate[] dates = {
                LocalDate.of(2000, 5, 29),
                LocalDate.of(1998, 8, 20),
                LocalDate.of(1985, 1, 2),
                LocalDate.of(1995, 4, 15),
                LocalDate.of(1985, 12, 21)
        };
        Long[] departments = {5L, 3L, 3L, 4L, 10L};

        for(int i = 0; i < 5; i++){
            EmployeeDto employee = new EmployeeDto((long)i + 1, firstNames[i], genders[i]);
            employee.setSecondName(secondNames[i]);
            employee.setDateOfBirth(dates[i]);
            employee.setJobTitle(jobTitles[i]);
            employee.setDepartmentId(departments[i]);

            list.add(employee);
        }
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

    @Test
    public void getListsWithAndWithoutNames() throws Exception {
        String firstName = list.get(forNameSearch).getFirstName();
        String secondName = list.get(forNameSearch).getSecondName();
        Map<String, String> params = new HashMap<>();
        params.put("firstName", firstName);
        params.put("secondName", secondName);
        when(service.employeeList(new HashMap<>())).thenReturn(list);
        when(service.employeeList(params)).thenReturn(Arrays.asList(list.get(forNameSearch)));

        //Check get method with path "/employee"
        MvcResult result = mockMvc.perform(get("/employee"))
                .andExpect(status().isOk()).andReturn();

        List<EmployeeDto> empList = mapFromJson(result.getResponse().getContentAsString(),
                new TypeReference<List<EmployeeDto>>(){});
        Assertions.assertThat(empList.size()).isPositive();
        EmployeeDto employee = empList.stream().findAny().get();
        Assertions.assertThat(employee).isNotNull();
        Assertions.assertThat(employee.getFirstName()).isNotBlank();

        //Check get method with path "/employee" and with names parameters
        String parameters = String.format("?firstName=%s&secondName=%s", firstName, secondName);
        result = mockMvc.perform(get("/employee" + parameters))
                .andExpect(status().isOk()).andReturn();

        empList = mapFromJson(result.getResponse().getContentAsString(),
                new TypeReference<List<EmployeeDto>>(){});
        Assertions.assertThat(empList.size()).isEqualTo(1);
        employee = empList.stream().findAny().get();
        Assertions.assertThat(employee).isNotNull();
        Assertions.assertThat(employee.getFirstName()).isEqualTo(firstName);
        Assertions.assertThat(employee.getSecondName()).isEqualTo(secondName);
    }

    @Test
    public void testSpecificEmployees() throws Exception {
        EmployeeDto actualEmployee = list.get(forIdSearch);
        Long id = actualEmployee.getEmployeeId();
        when(service.employeeById(id)).thenReturn(actualEmployee);
        when(service.employeeById(nonExistentId)).thenThrow(new NotFoundException());

        //Check get method with path "/employee/{id}", where id exists
        MvcResult result = mockMvc.perform(get("/employee/" + id))
                .andExpect(status().isOk()).andReturn();

        EmployeeDto employee = mapFromJson(result.getResponse().getContentAsString(), new TypeReference<EmployeeDto>() {});

        Assertions.assertThat(employee).isNotNull();
        Assertions.assertThat(employee.getEmployeeId()).isEqualTo(id);
        Assertions.assertThat(employee.getFirstName()).isNotNull();
        Assertions.assertThat(employee.getSecondName()).isNotNull();
        Assertions.assertThat(employee.getJobTitle()).isNotNull();
        Assertions.assertThat(employee.getDepartmentId()).isNotNull();
        Assertions.assertThat(employee.getDateOfBirth()).isNotNull();
        Assertions.assertThat(employee.getGender()).isNotNull();

        //Check get method with path "/employee/{id}", where id doesn't exist
        mockMvc.perform(get("/employee/" + nonExistentId))
                .andExpect(status().isNotFound()).andReturn();
    }

    @Test
    public void testCreate() throws Exception {
        EmployeeDto employee = list.get(forCreating);
        when(service.createEmployee(employee)).thenReturn(employee);

        MvcResult result = mockMvc.perform(post("/employee")
                        .contentType("application/json")
                        .content(mapToJson(employee)))
                .andExpect(status().isOk()).andReturn();

        EmployeeDto returnedEmployee = mapFromJson(result.getResponse().getContentAsString(), new TypeReference<EmployeeDto>() {});
        employee.setEmployeeId(returnedEmployee.getEmployeeId());
        Assertions.assertThat(employee).isEqualTo(returnedEmployee);
    }

    @Test
    public void testUpdate() throws Exception {
        EmployeeDto employee = list.get(forUpdating);
        Long id = employee.getEmployeeId();
        when(service.updateEmployee(id, employee)).thenReturn(employee);

        MvcResult result = mockMvc.perform(put("/employee/" + id)
                        .contentType("application/json")
                        .content(mapToJson(employee)))
                .andExpect(status().isOk()).andReturn();

        EmployeeDto returnedEmployee = mapFromJson(result.getResponse().getContentAsString(), new TypeReference<EmployeeDto>() {});
        Assertions.assertThat(employee).isEqualTo(returnedEmployee);
    }

    @Test
    public void testDelete() throws Exception {
        Long id = list.get(forDeleting).getEmployeeId();

        mockMvc.perform(delete("/employee/" + id)).andExpect(status().isOk());

        verify(service, times(1)).deleteEmployee(id);
    }
}
