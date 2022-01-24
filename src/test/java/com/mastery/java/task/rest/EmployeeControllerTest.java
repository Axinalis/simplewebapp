package com.mastery.java.task.rest;

import com.mastery.java.task.dto.EmployeeDto;
import com.mastery.java.task.dto.Gender;
import com.mastery.java.task.service.impl.DefaultEmployeeService;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EmployeeControllerTest {

    private EmployeeController controller;
    @Mock
    private DefaultEmployeeService service;
    private List<EmployeeDto> list;

    @Before
    public void setup(){
        list = new ArrayList<>();
        String male = Gender.MALE.toString();
        String female = Gender.FEMALE.toString();
        String[] firstNames = {"Anton","Nikolay","Andrey","Natalia"};
        String[] secondNames = {"Trus","Golubov","Shulgach","Mironova"};
        String[] jobTitles = {"Developer","Project manager","Team lead","HR"};
        String[] genders = {male, male, male, female};
        LocalDate[] dates = {
                LocalDate.of(2000, 5, 29),
                LocalDate.of(1998, 8, 20),
                LocalDate.of(1985, 1, 2),
                LocalDate.of(1995, 4, 15)
        };
        Long[] departments = {5L, 3L, 3L, 4L};

        for(int i = 0; i < 4; i++){
            EmployeeDto employeeDto = new EmployeeDto((long)i + 1, firstNames[i], genders[i]);
            employeeDto.setSecondName(secondNames[i]);
            employeeDto.setDateOfBirth(dates[i]);
            employeeDto.setJobTitle(jobTitles[i]);
            employeeDto.setDepartmentId(departments[i]);
            list.add(employeeDto);
        }

        service = mock(DefaultEmployeeService.class);
        controller = new EmployeeController(service);
    }

    @Test
    public void testEmployeeList(){
        when(service.employeeList(new HashMap<>())).thenReturn(list);
        List<EmployeeDto> list = controller.employeeList(new HashMap<>());

        assertNotNull(list);
        assertTrue(list.size() > 0);
        assertTrue(list.stream().anyMatch(employee -> employee.getEmployeeId().equals(1L)));
        assertTrue(list.stream().anyMatch(employee -> employee.getEmployeeId().equals(2L)));
        assertTrue(list.stream().anyMatch(employee -> employee.getEmployeeId().equals(3L)));
        assertTrue(list.stream().noneMatch(employee -> employee.getEmployeeId().equals(50L)));
    }

    @Ignore
    @Test
    public void testEmployeeById(){
        when(service.employeeById(1L)).thenReturn(list.get(0));
        when(service.employeeById(2L)).thenReturn(list.get(1));
        when(service.employeeById(3L)).thenReturn(list.get(2));
        when(service.employeeById(4L)).thenReturn(list.get(3));
        EmployeeDto buf = controller.employeeById(1L);

        assertNotNull(buf);
        assertEquals("Anton", buf.getFirstName());
        assertEquals("Trus", buf.getSecondName());
        assertEquals("Developer", buf.getJobTitle());
        assertEquals(Gender.MALE, buf.getGender());
        assertEquals(5L, (long) buf.getDepartmentId());
    }

    @Test
    public void testCreateEmployee(){
        EmployeeDto employee = new EmployeeDto(4L, "Natalia", Gender.FEMALE.toString());
        employee.setDateOfBirth(LocalDate.of(1995, 4, 15));
        employee.setSecondName("Mironova");
        employee.setJobTitle("HR");
        employee.setDepartmentId(4L);

        //Test will pass if there will be no exception
        controller.createEmployee(employee);
    }

    @Test
    public void testUpdateEmployee(){
        EmployeeDto employee = new EmployeeDto(4L, "Natalia", Gender.FEMALE.toString());
        employee.setDateOfBirth(LocalDate.of(1995, 4, 15));
        employee.setSecondName("Mironova");
        employee.setJobTitle("HR");
        employee.setDepartmentId(4L);

        //Test will pass if there will be no exception
        controller.updateEmployee(4L, employee);
    }

    @Test
    public void testDeleteEmployee(){
        controller.deleteEmployee(4L);
    }

}
