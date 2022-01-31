package com.mastery.java.task.jms;

import com.mastery.java.task.dto.EmployeeDto;
import com.mastery.java.task.exceptions.EmployeeNotFoundException;
import com.mastery.java.task.service.EmployeeService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

@Component
public class JmsConsumer implements MessageListener {

    @Autowired
    private EmployeeService service;
    private Logger log = LogManager.getLogger(JmsConsumer.class);

    @Override
    @JmsListener(destination = "${active-mq.topic}")
    public void onMessage(Message message) {
        EmployeeDto employee;
        try{
            log.info("Mapping received object to EmployeeDto class");
            ObjectMessage objMessage = (ObjectMessage) message;
            employee = (EmployeeDto) objMessage.getObject();
        } catch (Exception ex){
            log.error("Error while handling received message: ", ex);
            return;
        }
        //just for checking if information is valid
        log.info("Employee's name is {}", employee.getFirstName());
        try{
            if(employee.getEmployeeId() == null){
                service.createEmployee(employee);
                log.info("New employee was created");
            } else {
                service.updateEmployee(employee.getEmployeeId(), employee);
                log.info("Existing employee was updated");
            }
        } catch (EmployeeNotFoundException ex){
            log.error("An error occurred while updating database: ", ex);
        }
    }
}
