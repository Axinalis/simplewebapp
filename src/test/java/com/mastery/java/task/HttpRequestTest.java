package com.mastery.java.task;

import com.mastery.java.task.dto.EmployeeDto;
import com.mastery.java.task.dto.Gender;
import org.assertj.core.api.Assertions;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

@Ignore //for now
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HttpRequestTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate template;

    private EmployeeDto employeeDto;

    @Test
    public void testAvailability() {
        Assertions.assertThat(template.getForObject("http://localhost:" + port + "/simplewebapp", String.class)).isNotNull();
        Assertions.assertThat(template.getForObject("http://localhost:" + port + "/simplewebapp/employee/30", String.class)).contains("firstName");
        EmployeeDto dto = template.getForObject("http://localhost:" + port + "/simplewebapp/employee/30", EmployeeDto.class);
    }

    @Test
    public void testCreate(){
        EmployeeDto employeeDto = new EmployeeDto(1L, "Egor", Gender.MALE.toString());
        employeeDto.setDepartmentId(1L);
        employeeDto.setJobTitle("Not a worker");
        employeeDto.setSecondName("Pomidorov");
        employeeDto.setDateOfBirth(LocalDate.of(2001, 5, 6));

        EmployeeDto responseEmployeeDto = template.postForObject("http://localhost:" + port + "/simplewebapp/employee",
                employeeDto, EmployeeDto.class);
        Long newId = responseEmployeeDto.getEmployeeId();
        employeeDto.setEmployeeId(newId);
        Assertions.assertThat(responseEmployeeDto).isEqualTo(employeeDto);
    }

    @Test
    public void testUpdate(){
        EmployeeDto employeeDto = new EmployeeDto(1L, "Egor", Gender.MALE.toString());
        employeeDto.setDepartmentId(1L);
        employeeDto.setJobTitle("Not a worker");
        employeeDto.setSecondName("Pomidorov");
        employeeDto.setDateOfBirth(LocalDate.of(2001, 5, 6));

        Long newId = 1L;
        employeeDto.setSecondName("Tomato");
        template.put("http://localhost:" + port + "/simplewebapp/employee/" + newId, employeeDto);
        EmployeeDto responseEmployeeDto = template.getForObject("http://localhost:" + port + "/simplewebapp/employee/" + newId, EmployeeDto.class);
        Assertions.assertThat(responseEmployeeDto.getSecondName()).isEqualTo("Tomato");
    }

    @Test
    public void testDelete(){
        Long newId = 1L;
        template.delete("http://localhost:" + port + "/simplewebapp/employee/" + newId);
        Assertions.assertThat(template.getForObject("http://localhost:" + port + "/simplewebapp/employee/" + newId, String.class)).contains("404");
    }

    @Test
    public void testAllTypesOfGets(){
        EmployeeDto employeeDto = new EmployeeDto(1L, "Egor", Gender.MALE.toString());
        employeeDto.setDepartmentId(1L);
        employeeDto.setJobTitle("Not a worker");
        employeeDto.setSecondName("Pomidorov");
        employeeDto.setDateOfBirth(LocalDate.of(2001, 5, 6));
        Assertions.assertThat(template.postForObject("http://localhost:" + port + "/simplewebapp/employee", employeeDto, EmployeeDto.class)).isNotNull();

        List list = template.getForObject("http://localhost:" + port + "/simplewebapp/employee", List.class);

        Assertions.assertThat(list.toArray()).isNotEmpty();
        Object rawElement = list.iterator().next();
        Assertions.assertThat(rawElement instanceof LinkedHashMap).isTrue();
        HashMap<String, String> element = (LinkedHashMap<String, String>) rawElement;
        Assertions.assertThat(element.get("firstName")).isNotNull();

        //Do i need to change method from "getForObject(url, class)" to "getForObject(url, class, params)"?
        List list2 = template.getForObject("http://localhost:" + port +
                "/simplewebapp/employee?firstName=Egor&secondName=Pomidorov", List.class);

        Assertions.assertThat(list2.toArray()).isNotEmpty();
        Object egor = list2.iterator().next();
        Assertions.assertThat(egor instanceof LinkedHashMap).isTrue();
        HashMap<String, Object> employeeEgor = (LinkedHashMap<String, Object>) egor;
        Long egorId = (long) (int) employeeEgor.get("employeeId");
        Assertions.assertThat(employeeEgor.get("secondName")).isEqualTo("Pomidorov");

        EmployeeDto responseEmployee = template.getForObject("http://localhost:" + port + "/simplewebapp/employee/"
                + egorId, EmployeeDto.class);
        Assertions.assertThat(responseEmployee.getFirstName()).isEqualTo(employeeEgor.get("firstName"));
        Assertions.assertThat(responseEmployee.getSecondName()).isEqualTo(employeeEgor.get("secondName"));
        Assertions.assertThat(responseEmployee.getGender().toString()).isEqualTo(employeeEgor.get("gender"));
        Assertions.assertThat(responseEmployee.getJobTitle()).isEqualTo(employeeEgor.get("jobTitle"));
        Assertions.assertThat(responseEmployee.getDepartmentId()).isEqualTo((long) (int) employeeEgor.get("departmentId"));
        Assertions.assertThat(responseEmployee.getDateOfBirth().toString()).isEqualTo(employeeEgor.get("dateOfBirth"));

        Assertions.assertThat(template.getForObject("http://localhost:" + port +
                "/simplewebapp/employee/" + (egorId + 1), String.class)).contains("404");

        template.delete("http://localhost:" + port + "/simplewebapp/employee/" + egorId);
    }

}
