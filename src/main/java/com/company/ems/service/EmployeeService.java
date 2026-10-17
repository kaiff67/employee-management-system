package com.company.ems.service;

import com.company.ems.dao.EmployeeDao;
import com.company.ems.model.Employee;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/** Keeps basic business rules separate from the menu and SQL code. */
public class EmployeeService {
    private final EmployeeDao employeeDao;

    public EmployeeService(EmployeeDao employeeDao) {
        this.employeeDao = employeeDao;
    }

    public List<Employee> listEmployees() throws SQLException {
        return employeeDao.findAll();
    }

    public Optional<Employee> findEmployee(long id) throws SQLException {
        if (id <= 0) throw new IllegalArgumentException("ID must be a positive number.");
        return employeeDao.findById(id);
    }

    public List<Employee> searchEmployees(String name) throws SQLException {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Search name cannot be empty.");
        return employeeDao.searchByName(name.trim());
    }

    public long addEmployee(Employee employee) throws SQLException {
        validate(employee);
        return employeeDao.add(employee);
    }

    public boolean updateEmployee(Employee employee) throws SQLException {
        if (employee.getId() <= 0) throw new IllegalArgumentException("ID must be a positive number.");
        validate(employee);
        return employeeDao.update(employee);
    }

    public boolean deleteEmployee(long id) throws SQLException {
        if (id <= 0) throw new IllegalArgumentException("ID must be a positive number.");
        return employeeDao.delete(id);
    }

    private static void validate(Employee employee) {
        if (employee.getFullName() == null || employee.getFullName().isBlank())
            throw new IllegalArgumentException("Name cannot be empty.");
        if (employee.getEmail() == null || !employee.getEmail().matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$"))
            throw new IllegalArgumentException("Enter a valid email address.");
        if (employee.getDepartment() == null || employee.getDepartment().isBlank())
            throw new IllegalArgumentException("Department cannot be empty.");
        if (employee.getJobTitle() == null || employee.getJobTitle().isBlank())
            throw new IllegalArgumentException("Job title cannot be empty.");
        if (employee.getSalary() == null || employee.getSalary().signum() <= 0)
            throw new IllegalArgumentException("Salary must be greater than zero.");
        if (employee.getHireDate() == null)
            throw new IllegalArgumentException("Hire date is required.");
    }
}
