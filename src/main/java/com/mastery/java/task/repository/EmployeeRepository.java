package com.mastery.java.task.repository;

import com.mastery.java.task.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findByFirstNameAndSecondName(String firstName, String secondName);
}
