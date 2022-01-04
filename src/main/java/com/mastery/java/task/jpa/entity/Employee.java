package com.mastery.java.task.jpa.entity;

import com.mastery.java.task.dto.Gender;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "employee_id")
    private Long employeeId;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Column(name = "second_name")
    private String secondName;
    @Column(name = "department_id")
    private Long departmentId;
    @Column(name = "job_title")
    private String jobTitle;
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    public Employee() {
    }

    public Employee(Long employeeId, String firstName, Gender gender) {
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.gender = gender;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId){
        this.employeeId = employeeId;
    }

    public String getFirstName() {
        return firstName;
    }

    public Gender getGender() {
        return gender;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Employee employee = (Employee) o;
        return Objects.equals(employeeId, employee.employeeId)
                && Objects.equals(firstName, employee.firstName)
                && gender == employee.gender
                && Objects.equals(secondName, employee.secondName)
                && Objects.equals(departmentId, employee.departmentId)
                && Objects.equals(jobTitle, employee.jobTitle)
                && Objects.equals(dateOfBirth, employee.dateOfBirth);
    }

    @Override
    public int hashCode() {
        return Objects.hash(employeeId, firstName, gender, secondName, departmentId, jobTitle, dateOfBirth);
    }
}
