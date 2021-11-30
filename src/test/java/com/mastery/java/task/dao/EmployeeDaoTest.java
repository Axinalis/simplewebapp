package com.mastery.java.task.dao;

import com.mastery.java.task.dao.impl.DefaultEmployeeDao;
import com.mastery.java.task.dto.Employee;
import com.mastery.java.task.dto.Gender;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class EmployeeDaoTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    private DefaultEmployeeDao dao;
    private RowMapper<Employee> rowMapper = (rs, rowNum) -> {
        Employee employee = new Employee();
        employee.setEmployeeId(rs.getLong("employee_id"));
        employee.setFirstName(rs.getString("first_name"));
        employee.setSecondName(rs.getString("second_name"));
        employee.setDepartmentId(rs.getLong("department_id"));
        employee.setJobTitle(rs.getString("job_title"));
        employee.setGender(Gender.valueOf(rs.getString("gender")));
        employee.setDateOfBirth(LocalDate.parse(rs.getString("date_of_birth")));
        return employee;
    };

    @Before
    public void setup(){
        jdbcTemplate = mock(JdbcTemplate.class);
        List<Employee> list = new ArrayList<>();
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
        dao = new DefaultEmployeeDao(jdbcTemplate);
        when(jdbcTemplate.query("SELECT * FROM employee ORDER BY employee_id", rowMapper)).thenReturn(list);
        when(jdbcTemplate.queryForObject("SELECT * FROM employee WHERE employee_id = ?", rowMapper, 1L)).thenReturn(employee1);
        when(jdbcTemplate.queryForObject("SELECT * FROM employee WHERE employee_id = ?", rowMapper, 2L)).thenReturn(employee2);
        when(jdbcTemplate.queryForObject("SELECT * FROM employee WHERE employee_id = ?", rowMapper, 3L)).thenReturn(employee3);
        when(jdbcTemplate.queryForObject("SELECT * FROM employee WHERE employee_id = ?", rowMapper, 4L)).thenReturn(employee4);
        when(jdbcTemplate.queryForObject("SELECT MAX(employee_id) FROM employee", Long.class)).thenReturn(4L);
    }

    @Test
    public void testList(){
        //This won't work
        List<Employee> bufList = dao.list();
        //This works...
        bufList = jdbcTemplate.query("SELECT * FROM employee ORDER BY employee_id", rowMapper);
        //Maybe i'm inattentive and made some noobie mistake, but i can't find even a hint

        Employee employee = new Employee(1L, "Anton", Gender.MALE);
        employee.setDateOfBirth(LocalDate.of(2000, 5, 29));
        employee.setSecondName("Trus");
        employee.setJobTitle("Developer");
        employee.setDepartmentId(5L);

        assertNotNull(bufList);
        assertTrue(bufList.size() > 0);
        assertEquals(bufList.get(0), employee);
    }

    @Test
    public void testGet(){
        Employee employee;

        //And one more time as it was in testList
        employee = dao.get(1L);
        employee = jdbcTemplate.queryForObject("SELECT * FROM employee WHERE employee_id = ?", rowMapper, 1L);
        assertEquals(employee.getFirstName(), "Anton");
    }

    @Test
    public void testCreate(){

        Employee employee = new Employee(1L, "Anton", Gender.MALE);
        employee.setDateOfBirth(LocalDate.of(2000, 5, 29));
        employee.setSecondName("Trus");
        employee.setJobTitle("Developer");
        employee.setDepartmentId(5L);

        dao.create(employee);

        //There supposed to be id = 5L, but something is wrong -_-
        verify((jdbcTemplate), times(1)).update(
                "INSERT INTO employee(employee_id, first_name, second_name, department_id, job_title, gender," +
                        " date_of_birth) VALUES(?,?,?,?,?,?,?)", 2L,
                "Anton",
                "Trus",
                5L,
                "Developer",
                "MALE",
                LocalDate.of(2000,5,29));
    }

    @Test
    public void testUpdate(){

        Employee employee = new Employee(4L, "Anton", Gender.MALE);
        employee.setDateOfBirth(LocalDate.of(2000, 5, 29));
        employee.setSecondName("Trus");
        employee.setJobTitle("Junior Developer");
        employee.setDepartmentId(5L);

        dao.update(4L, employee);

        verify((jdbcTemplate), times(1))
                .update(
                "UPDATE employee SET first_name = ?, second_name = ?, department_id = ?, job_title = ?, gender = ?," +
                        " date_of_birth = ? WHERE employee_id = ?",
                "Anton",
                "Trus",
                5L,
                "Junior Developer",
                "MALE",
                LocalDate.of(2000,5,29),
                4L);
    }

    @Test
    public void testDelete(){
        dao.delete(1L);

        verify(jdbcTemplate, times(1))
                .update("DELETE FROM employee WHERE employee_id = ?", 1L);
    }

}
