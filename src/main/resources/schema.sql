CREATE TABLE employee (
employee_id INT NOT NULL,
first_name VARCHAR(50) NOT NULL,
second_name VARCHAR(50),
department_id INT NOT NULL,
job_title VARCHAR(100),
gender VARCHAR(15),
date_of_birth DATE,
PRIMARY KEY (employee_id)
);