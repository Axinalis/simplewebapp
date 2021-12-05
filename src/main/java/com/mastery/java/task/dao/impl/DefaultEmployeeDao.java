package com.mastery.java.task.dao.impl;

import com.mastery.java.task.dao.EmployeeDao;
import com.mastery.java.task.dto.Employee;
import com.mastery.java.task.dto.Gender;
import com.mastery.java.task.exceptions.NotFoundException;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDate;
import java.util.List;

public class DefaultEmployeeDao implements EmployeeDao {

    private JdbcTemplate jdbcTemplate;

    private Long idCounter;
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

    public DefaultEmployeeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Employee> list() {
        String sql = "SELECT * FROM employee ORDER BY employee_id";
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public Employee get(Long id) {
        String sql = "SELECT * FROM employee WHERE employee_id = ?";
        try{
            return jdbcTemplate.queryForObject(sql, rowMapper, id);
        } catch (DataAccessException exception){
            throw new NotFoundException();
        }
    }

    @Override
    public Employee create(Employee employee) {
        String sql = "INSERT INTO employee(first_name, second_name, department_id, job_title, gender," +
                " date_of_birth) VALUES(?,?,?,?,?,?)";
        jdbcTemplate.update(sql,
                employee.getFirstName(),
                employee.getSecondName(),
                employee.getDepartmentId(),
                employee.getJobTitle(),
                employee.getGender().toString(),
                employee.getDateOfBirth());
        //Maybe "INSERT INTO ... VALUES ... RETURNING employee_id" could be more efficient, but it sure would complicate this code
        sql = "SELECT * FROM employee WHERE employee_id = (SELECT currval(pg_get_serial_sequence('employee','employee_id')))";
        return jdbcTemplate.queryForObject(sql, rowMapper);
    }

    @Override
    public Employee update(Long id, Employee employee) {
        String sql = "UPDATE employee SET first_name = ?, second_name = ?, department_id = ?, job_title = ?, gender = ?," +
                " date_of_birth = ? WHERE employee_id = ?";
        jdbcTemplate.update(sql,
                employee.getFirstName(),
                employee.getSecondName(),
                employee.getDepartmentId(),
                employee.getJobTitle(),
                employee.getGender().toString(),
                employee.getDateOfBirth(),
                id);

        sql = "SELECT * FROM employee WHERE employee_id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM employee WHERE employee_id = ?";
        jdbcTemplate.update(sql, id);
    }
}
