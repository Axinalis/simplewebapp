package com.mastery.java.task.jms;

import com.mastery.java.task.dto.EmployeeDto;
import com.mastery.java.task.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import java.time.LocalDate;

import static org.mockito.Mockito.*;

public class JmsConsumerTest {

    private JmsConsumer consumer;
    private EmployeeService service;

    @BeforeEach
    public void setup(){
        service = mock(EmployeeService.class);
        consumer = new JmsConsumer(service);
    }

    @Test
    public void testPassingNull(){
        consumer.onMessage(null);
    }

    @Test
    public void testPassingInvalidObject() throws JMSException {
        ObjectMessage message = mock(ObjectMessage.class);
        EmployeeDto employee = new EmployeeDto();
        employee.setFirstName("");
        employee.setSecondName("Johnson");
        employee.setDepartmentId(4L);
        employee.setJobTitle("");
        employee.setGender("A MALE");
        employee.setDateOfBirth(LocalDate.of(2007, 2, 13));
        when(message.getObject()).thenReturn(employee);

        consumer.onMessage(message);

        verify(service, times(0)).updateEmployee(any(), any());
        verify(service, times(0)).createEmployee(any());
    }

    @Test
    public void testCreatingEmployee() throws JMSException {
        ObjectMessage message = mock(ObjectMessage.class);
        EmployeeDto employee = createEmployeeWithoutEmployeeId();
        when(message.getObject()).thenReturn(employee);

        consumer.onMessage(message);

        verify(service).createEmployee(any());
    }

    @Test
    public void testUpdatingEmployee() throws JMSException {
        ObjectMessage message = mock(ObjectMessage.class);
        EmployeeDto employee = createEmployeeWithoutEmployeeId();
        employee.setEmployeeId(1L);
        when(message.getObject()).thenReturn(employee);

        consumer.onMessage(message);

        verify(service).updateEmployee(anyLong(), any());
    }

    private EmployeeDto createEmployeeWithoutEmployeeId(){
        EmployeeDto employee = new EmployeeDto();
        employee.setFirstName("Peter");
        employee.setSecondName("Johnson");
        employee.setDepartmentId(4L);
        employee.setJobTitle("Some job title");
        employee.setGender("MALE");
        employee.setDateOfBirth(LocalDate.of(2000, 2, 13));
        return employee;
    }
}
