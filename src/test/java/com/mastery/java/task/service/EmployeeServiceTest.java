package com.mastery.java.task.service;

import com.mastery.java.task.dto.Employee;
import com.mastery.java.task.dto.Gender;
import com.mastery.java.task.jpa.EmployeeRepository;
import com.mastery.java.task.service.impl.DefaultEmployeeService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.time.LocalDate;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository repository;
    private DefaultEmployeeService service;
    private List<Employee> list;
    private Map<String, String> params;

    @Before
    public void setup(){
        //For now there aren't much logic in this class and therefore no enough aspects to test
        repository = mock(EmployeeRepository.class);
        list = new ArrayList<>();
        Employee employee1 = new Employee(1L, "Anton", Gender.MALE);
        employee1.setDateOfBirth(LocalDate.of(2000, 5, 29));
        employee1.setSecondName("Trus");
        employee1.setJobTitle("Developer");
        employee1.setDepartmentId(5L);
        list.add(employee1);
        Employee employee2 = new Employee(2L, "Nikolay", Gender.MALE);
        employee2.setDateOfBirth(LocalDate.of(1998, 8, 20));
        employee2.setSecondName("Golubov");
        employee2.setJobTitle("Project manager");
        employee2.setDepartmentId(3L);
        list.add(employee2);
        Employee employee3 = new Employee(3L, "Andrey", Gender.MALE);
        employee3.setDateOfBirth(LocalDate.of(1985, 1, 2));
        employee3.setSecondName("Shulgach");
        employee3.setJobTitle("Team lead");
        employee3.setDepartmentId(3L);
        list.add(employee3);
        Employee employee4 = new Employee(4L, "Natalia", Gender.FEMALE);
        employee4.setDateOfBirth(LocalDate.of(1995, 4, 15));
        employee4.setSecondName("Mironova");
        employee4.setJobTitle("HR");
        employee4.setDepartmentId(4L);
        list.add(employee4);
        params = new HashMap<>();
        params.put("firstName", "Anton");
        params.put("secondName", "Trus");
        service = new DefaultEmployeeService(repository);
        when(repository.findAll()).thenReturn(list);
        when(repository.findById(1L)).thenReturn(Optional.of(employee1));
        when(repository.findById(2L)).thenReturn(Optional.of(employee2));
        when(repository.findById(3L)).thenReturn(Optional.of(employee3));
        when(repository.findById(4L)).thenReturn(Optional.of(employee4));
        when(repository.findByFirstNameAndSecondName("Anton", "Trus")).thenReturn(Arrays.asList(employee1));
        when(repository.findById(5L)).thenReturn(Optional.empty());
        when(repository.count()).thenReturn((long) list.size());

    }

    @Test
    public void testList(){
        List<Employee> list = service.employeeList(new HashMap<>());

        assertNotNull(list);
        assertTrue(list.size() > 0);
        assertTrue(list.stream().anyMatch(employee -> employee.getEmployeeId().equals(1L)));
        assertTrue(list.stream().anyMatch(employee -> employee.getEmployeeId().equals(2L)));
        assertTrue(list.stream().anyMatch(employee -> employee.getEmployeeId().equals(3L)));
        assertTrue(list.stream().noneMatch(employee -> employee.getEmployeeId().equals(50L)));
    }

    @Test
    public void testListWithNames(){
        List<Employee> list = service.employeeList(params);

        assertNotNull(list);
        assertTrue(list.size() > 0);
        assertTrue(list.stream().anyMatch(employee -> employee.getEmployeeId().equals(1L)));
        assertTrue(list.stream().noneMatch(employee -> employee.getEmployeeId().equals(2L)));
        assertTrue(list.stream().noneMatch(employee -> employee.getEmployeeId().equals(3L)));
        assertTrue(list.stream().noneMatch(employee -> employee.getEmployeeId().equals(50L)));
    }

    @Test
    public void testEmployeeById(){
        Employee buf = service.employeeById(1L);

        assertNotNull(buf);
        assertEquals("Anton", buf.getFirstName());
        assertEquals("Trus", buf.getSecondName());
        assertEquals("Developer", buf.getJobTitle());
        assertEquals(Gender.MALE, buf.getGender());
        assertEquals(5L, (long) buf.getDepartmentId());
    }

    @Test
    public void testCreateEmployee(){
        Employee employee = new Employee(4L, "Natalia", Gender.FEMALE);
        employee.setDateOfBirth(LocalDate.of(1995, 4, 15));
        employee.setSecondName("Mironova");
        employee.setJobTitle("HR");
        employee.setDepartmentId(4L);

        //Test will pass if there will be no exception
        service.createEmployee(employee);
    }

    @Test
    public void testUpdateEmployee(){
        Employee employee = new Employee(4L, "Natalia", Gender.FEMALE);
        employee.setDateOfBirth(LocalDate.of(1995, 4, 15));
        employee.setSecondName("Mironova");
        employee.setJobTitle("HR");
        employee.setDepartmentId(4L);

        //Test will pass if there will be no exception
        service.updateEmployee(4L, employee);
    }

    @Test
    public void testDeleteEmployee(){
        service.deleteEmployee(4L);
    }

}
