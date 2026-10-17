CREATE DATABASE IF NOT EXISTS employee_management;

USE employee_management;

CREATE TABLE IF NOT EXISTS employees (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    phone VARCHAR(30),
    department VARCHAR(80) NOT NULL,
    job_title VARCHAR(100) NOT NULL,
    salary DECIMAL(12, 2) NOT NULL,
    hire_date DATE NOT NULL
);
