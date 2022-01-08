package com.mastery.java.task.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mastery.java.task.dto.EmployeeDto;
import com.mastery.java.task.dto.Gender;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//This test has a lot of pre-defined IDs, it will be fixed in future
@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private static ObjectMapper mapper;
    private static EmployeeDto egorPomidorov;

    @BeforeAll
    public static void setup(){
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        egorPomidorov = new EmployeeDto(1L, "Egor", Gender.MALE);
        egorPomidorov.setDepartmentId(1L);
        egorPomidorov.setJobTitle("Not a worker");
        egorPomidorov.setSecondName("Pomidorov");
        egorPomidorov.setDateOfBirth(LocalDate.of(2001, 5, 6));

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
        //Check get method with path "/employee"
        MvcResult result = mockMvc.perform(get("/employee"))
                .andExpect(status().isOk()).andReturn();

        String responseBody = result.getResponse().getContentAsString();
        List<EmployeeDto> strList = mapFromJson(responseBody, new TypeReference<List<EmployeeDto>>(){});
        Assertions.assertThat(strList.size()).isPositive();
        EmployeeDto employee = strList.stream().findAny().get();
        Assertions.assertThat(employee).isNotNull();
        Assertions.assertThat(employee.getFirstName()).isNotBlank();

        //Check get method with path "/employee" and with names parameters
        result = mockMvc.perform(get("/employee?firstName=Egor&secondName=Pomidorov"))
                .andExpect(status().isOk()).andReturn();
        responseBody = result.getResponse().getContentAsString();
        strList = mapFromJson(responseBody, new TypeReference<List<EmployeeDto>>(){});
        Assertions.assertThat(strList.size()).isPositive();
        employee = strList.stream().findAny().get();
        Assertions.assertThat(employee).isNotNull();
        Assertions.assertThat(employee.getFirstName()).isEqualTo("Egor");
        Assertions.assertThat(employee.getSecondName()).isEqualTo("Pomidorov");
    }

    @Test
    public void testSpecificEmployees() throws Exception {
        Long id = 44L;
        Long nonExistentId = 1000L;
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
        result = mockMvc.perform(get("/employee/" + nonExistentId))
                .andExpect(status().isNotFound()).andReturn();
    }

    @Test
    public void testCreate() throws Exception {
        String jsonEmployee = mapToJson(egorPomidorov);

        MvcResult result = mockMvc.perform(post("/employee").contentType("application/json").content(jsonEmployee))
                .andExpect(status().isOk()).andReturn();

        EmployeeDto returnedEmployee = mapFromJson(result.getResponse().getContentAsString(), new TypeReference<EmployeeDto>() {});
        egorPomidorov.setEmployeeId(returnedEmployee.getEmployeeId());
        Assertions.assertThat(egorPomidorov).isEqualTo(returnedEmployee);
    }

    @Test
    public void testUpdate() throws Exception {
        Long originId = egorPomidorov.getEmployeeId();
        String jsonEmployee = mapToJson(egorPomidorov);
        Long id = 30L;
        Long nonExistentId = 1000L;

        MvcResult result = mockMvc.perform(put("/employee/" + id).contentType("application/json").content(jsonEmployee))
                .andExpect(status().isOk()).andReturn();

        EmployeeDto returnedEmployee = mapFromJson(result.getResponse().getContentAsString(), new TypeReference<EmployeeDto>() {});
        egorPomidorov.setEmployeeId(id);
        Assertions.assertThat(egorPomidorov).isEqualTo(returnedEmployee);
        egorPomidorov.setEmployeeId(originId);
    }

    @Test
    public void testDelete() throws Exception {
        //Creating new employee to delete it afterwards

        String jsonEmployee = mapToJson(egorPomidorov);
        MvcResult result = mockMvc.perform(post("/employee").contentType("application/json").content(jsonEmployee))
                .andExpect(status().isOk()).andReturn();

        EmployeeDto returnedEmployee = mapFromJson(result.getResponse().getContentAsString(), new TypeReference<EmployeeDto>() {});
        Long id = returnedEmployee.getEmployeeId();
        Assertions.assertThat(id).isNotNull();

        //Deleting
        mockMvc.perform(delete("/employee/" + id)).andExpect(status().isOk());

        Assertions.assertThat(mockMvc.perform(get("/employee/" + id))
                .andReturn()
                .getResponse()
                .getStatus())
                .isEqualTo(404);
    }
}
