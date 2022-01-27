package com.mastery.java.task.dto;

import com.mastery.java.task.validator.Adult;
import com.mastery.java.task.validator.IsGender;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class EmployeeDto implements Serializable {

    private Long employeeId;
    @NotBlank
    private String firstName;
    @IsGender
    private String gender;
    private String secondName;
    @NotNull
    private Long departmentId;
    @NotBlank
    private String jobTitle;
    @NotNull
    @Adult
    private LocalDate dateOfBirth;

    public EmployeeDto() {
    }

    public EmployeeDto(Long employeeId, String firstName, String gender) {
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

    public String getGender() {
        return gender;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setGender(String gender) {
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
        EmployeeDto employee = (EmployeeDto) o;
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

    @Override
    public String toString(){
        return String.format("[ employeeId=%d; firstName=%s; gender=%s; secondName=%s; " +
                        "departmentId=%s; jobTitle=%s; dateOfBirth=%s; ]",
                this.employeeId,
                this.firstName,
                this.gender,
                this.secondName,
                this.departmentId,
                this.jobTitle,
                this.dateOfBirth
        );
    }
}
