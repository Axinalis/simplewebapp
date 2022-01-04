package com.mastery.java.task.jpa;

import com.mastery.java.task.jpa.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findByFirstNameAndSecondName(String firstName, String secondName);
}
