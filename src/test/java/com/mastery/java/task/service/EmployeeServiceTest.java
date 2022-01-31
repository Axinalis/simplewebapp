package com.mastery.java.task.service;

import com.mastery.java.task.dto.EmployeeDto;
import com.mastery.java.task.entity.Employee;
import com.mastery.java.task.dto.Gender;
import com.mastery.java.task.repository.EmployeeRepository;
import com.mastery.java.task.service.impl.DefaultEmployeeService;
import org.junit.Before;
import org.junit.Ignore;
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

    @Before
    public void setup(){
        list = new ArrayList<>();

        String[] firstNames = {"Anton","Nikolay","Andrey","Natalia"};
        String[] secondNames = {"Trus","Golubov","Shulgach","Mironova"};
        String[] jobTitles = {"Developer","Project manager","Team lead","HR"};
        Gender[] genders = {Gender.MALE, Gender.MALE, Gender.MALE, Gender.FEMALE};
        LocalDate[] dates = {
                LocalDate.of(2000, 5, 29),
                LocalDate.of(1998, 8, 20),
                LocalDate.of(1985, 1, 2),
                LocalDate.of(1995, 4, 15)
        };
        Long[] departments = {5L, 3L, 3L, 4L};

        for(int i = 0; i < 4; i++){
            Employee employee = new Employee((long)i + 1, firstNames[i], genders[i]);
            employee.setSecondName(secondNames[i]);
            employee.setDateOfBirth(dates[i]);
            employee.setJobTitle(jobTitles[i]);
            employee.setDepartmentId(departments[i]);
            list.add(employee);
        }

        repository = mock(EmployeeRepository.class);
        service = new DefaultEmployeeService(repository);
    }

    @Test
    public void testList(){
        when(repository.findAll()).thenReturn(list);
        List<EmployeeDto> list = service.employeeList(new HashMap<>());

        assertNotNull(list);
        assertTrue(list.size() > 0);
        assertTrue(list.stream().anyMatch(employee -> employee.getEmployeeId().equals(1L)));
        assertTrue(list.stream().anyMatch(employee -> employee.getEmployeeId().equals(2L)));
        assertTrue(list.stream().anyMatch(employee -> employee.getEmployeeId().equals(3L)));
        assertTrue(list.stream().noneMatch(employee -> employee.getEmployeeId().equals(50L)));
    }

    @Test
    public void testListWithNames(){
        when(repository.findByFirstNameAndSecondName("Anton", "Trus")).thenReturn(Arrays.asList(list.get(0)));
        Map<String, String> params;
        params = new HashMap<>();
        params.put("firstName", "Anton");
        params.put("secondName", "Trus");
        List<EmployeeDto> list = service.employeeList(params);

        assertNotNull(list);
        assertTrue(list.size() > 0);
        assertTrue(list.stream().anyMatch(employee -> employee.getEmployeeId().equals(1L)));
        assertTrue(list.stream().noneMatch(employee -> employee.getEmployeeId().equals(2L)));
        assertTrue(list.stream().noneMatch(employee -> employee.getEmployeeId().equals(3L)));
        assertTrue(list.stream().noneMatch(employee -> employee.getEmployeeId().equals(50L)));
    }

    @Ignore
    @Test
    public void testEmployeeById(){
        when(repository.findById(1L)).thenReturn(Optional.of(list.get(0)));
        when(repository.findById(2L)).thenReturn(Optional.of(list.get(1)));
        when(repository.findById(3L)).thenReturn(Optional.of(list.get(2)));
        when(repository.findById(4L)).thenReturn(Optional.of(list.get(3)));
        when(repository.findById(5L)).thenReturn(Optional.empty());
        EmployeeDto buf = service.employeeById(1L);

        assertNotNull(buf);
        assertEquals("Anton", buf.getFirstName());
        assertEquals("Trus", buf.getSecondName());
        assertEquals("Developer", buf.getJobTitle());
        assertEquals(Gender.MALE, buf.getGender());
        assertEquals(5L, (long) buf.getDepartmentId());
    }

    @Test
    public void testCreateEmployee(){
        Long id = 4L;
        EmployeeDto employeeDto = new EmployeeDto(id, "Natalia", Gender.FEMALE.toString());
        employeeDto.setDateOfBirth(LocalDate.of(1995, 4, 15));
        employeeDto.setSecondName("Mironova");
        employeeDto.setJobTitle("HR");
        employeeDto.setDepartmentId(4L);

        Employee employee = new Employee(null, "Natalia", Gender.FEMALE);
        employee.setDateOfBirth(LocalDate.of(1995, 4, 15));
        employee.setSecondName("Mironova");
        employee.setJobTitle("HR");
        employee.setDepartmentId(4L);

        Employee createdEmployee = new Employee(id + 1, "Natalia", Gender.FEMALE);
        createdEmployee.setDateOfBirth(LocalDate.of(1995, 4, 15));
        createdEmployee.setSecondName("Mironova");
        createdEmployee.setJobTitle("HR");
        createdEmployee.setDepartmentId(4L);

        when(repository.save(employee)).thenReturn(createdEmployee);

        //Test will pass if there will be no exception
        EmployeeDto employeeDtoChanged = service.createEmployee(employeeDto);
        assertEquals(employeeDtoChanged.getFirstName(), employeeDto.getFirstName());
        assertEquals(employeeDtoChanged.getSecondName(), employeeDto.getSecondName());
        assertEquals(employeeDtoChanged.getGender(), employeeDto.getGender());
        assertEquals(employeeDtoChanged.getJobTitle(), employeeDto.getJobTitle());
        assertNotEquals(employeeDtoChanged.getEmployeeId(), employeeDto.getEmployeeId());

    }

    @Test
    public void testUpdateEmployee(){
        Long id = 4L;
        EmployeeDto employeeDto = new EmployeeDto(id, "Natalia", Gender.FEMALE.toString());
        employeeDto.setDateOfBirth(LocalDate.of(1995, 4, 15));
        employeeDto.setSecondName("Mironova");
        employeeDto.setJobTitle("HR");
        employeeDto.setDepartmentId(4L);

        Employee employee = new Employee(id, "Natalia", Gender.FEMALE);
        employee.setDateOfBirth(LocalDate.of(1995, 4, 15));
        employee.setSecondName("Mironova");
        employee.setJobTitle("HR");
        employee.setDepartmentId(4L);

        when(repository.save(employee)).thenReturn(employee);

        //Test will pass if there will be no exception
        EmployeeDto employeeDtoChanged = service.updateEmployee(id, employeeDto);
        assertEquals(employeeDtoChanged.getFirstName(), employeeDto.getFirstName());
        assertEquals(employeeDtoChanged.getSecondName(), employeeDto.getSecondName());
        assertEquals(employeeDtoChanged.getGender(), employeeDto.getGender());
        assertEquals(employeeDtoChanged.getJobTitle(), employeeDto.getJobTitle());
        assertEquals(employeeDtoChanged.getEmployeeId(), employeeDto.getEmployeeId());
    }

    @Test
    public void testDeleteEmployee(){
        service.deleteEmployee(4L);
    }

}
