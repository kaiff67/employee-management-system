package com.company.ems.dao;

import com.company.ems.model.Employee;
import com.company.ems.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/** DAO means Data Access Object: this class contains the SQL used for employees. */
public class EmployeeDao {
    private static final String SELECT_COLUMNS =
            "id, full_name, email, phone, department, job_title, salary, hire_date";

    public List<Employee> findAll() throws SQLException {
        String sql = "SELECT " + SELECT_COLUMNS + " FROM employees ORDER BY id";
        List<Employee> employees = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet results = statement.executeQuery()) {
            while (results.next()) {
                employees.add(mapRow(results));
            }
        }
        return employees;
    }

    public Optional<Employee> findById(long id) throws SQLException {
        String sql = "SELECT " + SELECT_COLUMNS + " FROM employees WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            try (ResultSet results = statement.executeQuery()) {
                return results.next() ? Optional.of(mapRow(results)) : Optional.empty();
            }
        }
    }

    public List<Employee> searchByName(String name) throws SQLException {
        String sql = "SELECT " + SELECT_COLUMNS + " FROM employees WHERE full_name LIKE ? ORDER BY full_name";
        List<Employee> employees = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, "%" + name + "%");
            try (ResultSet results = statement.executeQuery()) {
                while (results.next()) {
                    employees.add(mapRow(results));
                }
            }
        }
        return employees;
    }

    public long add(Employee employee) throws SQLException {
        String sql = "INSERT INTO employees (full_name, email, phone, department, job_title, salary, hire_date) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            setEmployeeFields(statement, employee);
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getLong(1);
                }
            }
        }
        throw new SQLException("Employee was saved, but MySQL did not return its new ID.");
    }

    public boolean update(Employee employee) throws SQLException {
        String sql = "UPDATE employees SET full_name = ?, email = ?, phone = ?, department = ?, "
                + "job_title = ?, salary = ?, hire_date = ? WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            setEmployeeFields(statement, employee);
            statement.setLong(8, employee.getId());
            return statement.executeUpdate() == 1;
        }
    }

    public boolean delete(long id) throws SQLException {
        String sql = "DELETE FROM employees WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            return statement.executeUpdate() == 1;
        }
    }

    private static void setEmployeeFields(PreparedStatement statement, Employee employee) throws SQLException {
        statement.setString(1, employee.getFullName());
        statement.setString(2, employee.getEmail());
        statement.setString(3, employee.getPhone());
        statement.setString(4, employee.getDepartment());
        statement.setString(5, employee.getJobTitle());
        statement.setBigDecimal(6, employee.getSalary());
        statement.setDate(7, Date.valueOf(employee.getHireDate()));
    }

    private static Employee mapRow(ResultSet results) throws SQLException {
        return new Employee(
                results.getLong("id"),
                results.getString("full_name"),
                results.getString("email"),
                results.getString("phone"),
                results.getString("department"),
                results.getString("job_title"),
                results.getBigDecimal("salary"),
                results.getDate("hire_date").toLocalDate());
    }
}
