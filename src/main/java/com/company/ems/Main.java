package com.company.ems;

import com.company.ems.dao.EmployeeDao;
import com.company.ems.model.Employee;
import com.company.ems.service.EmployeeService;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/** Starts the console app and displays its menu. */
public class Main {
    private final Scanner scanner = new Scanner(System.in);
    private final EmployeeService employeeService = new EmployeeService(new EmployeeDao());

    public static void main(String[] args) {
        new Main().run();
    }

    private void run() {
        System.out.println("========================================");
        System.out.println("     EMPLOYEE MANAGEMENT SYSTEM");
        System.out.println("========================================");

        boolean running = true;
        while (running) {
            printMenu();
            String choice = readLine("Choose an option: ").trim();
            try {
                switch (choice) {
                    case "1" -> listEmployees();
                    case "2" -> addEmployee();
                    case "3" -> findEmployee();
                    case "4" -> updateEmployee();
                    case "5" -> deleteEmployee();
                    case "6" -> searchEmployees();
                    case "0" -> running = false;
                    default -> System.out.println("Please choose one of the listed numbers.");
                }
            } catch (IllegalArgumentException | DateTimeParseException exception) {
                System.out.println("Input error: " + exception.getMessage());
            } catch (SQLException exception) {
                System.out.println("Database error: " + exception.getMessage());
                System.out.println("Check that MySQL is running and db.properties has the correct login.");
            }
        }
        scanner.close();
        System.out.println("Goodbye!");
    }

    private void printMenu() {
        System.out.println("\n1. List all employees");
        System.out.println("2. Add an employee");
        System.out.println("3. Find employee by ID");
        System.out.println("4. Update an employee");
        System.out.println("5. Delete an employee");
        System.out.println("6. Search employees by name");
        System.out.println("0. Exit");
    }

    private void listEmployees() throws SQLException {
        printEmployees(employeeService.listEmployees());
    }

    private void addEmployee() throws SQLException {
        Employee employee = readEmployeeDetails(0);
        long id = employeeService.addEmployee(employee);
        System.out.println("Employee added successfully. New ID: " + id);
    }

    private void findEmployee() throws SQLException {
        long id = readLong("Employee ID: ");
        Optional<Employee> employee = employeeService.findEmployee(id);
        employee.ifPresentOrElse(System.out::println, () -> System.out.println("No employee found with that ID."));
    }

    private void updateEmployee() throws SQLException {
        long id = readLong("ID of the employee to update: ");
        Optional<Employee> existing = employeeService.findEmployee(id);
        if (existing.isEmpty()) {
            System.out.println("No employee found with that ID.");
            return;
        }
        System.out.println("Enter the employee's new details:");
        Employee updated = readEmployeeDetails(id);
        if (employeeService.updateEmployee(updated)) {
            System.out.println("Employee updated successfully.");
        } else {
            System.out.println("Employee was not found.");
        }
    }

    private void deleteEmployee() throws SQLException {
        long id = readLong("ID of the employee to delete: ");
        if (employeeService.deleteEmployee(id)) {
            System.out.println("Employee deleted successfully.");
        } else {
            System.out.println("No employee found with that ID.");
        }
    }

    private void searchEmployees() throws SQLException {
        String name = readLine("Enter all or part of a name: ");
        printEmployees(employeeService.searchEmployees(name));
    }

    private Employee readEmployeeDetails(long id) {
        String fullName = readLine("Full name: ").trim();
        String email = readLine("Email: ").trim();
        String phone = readLine("Phone (optional): ").trim();
        String department = readLine("Department: ").trim();
        String jobTitle = readLine("Job title: ").trim();
        BigDecimal salary = new BigDecimal(readLine("Salary (example: 45000.00): ").trim());
        LocalDate hireDate = LocalDate.parse(readLine("Hire date (YYYY-MM-DD): ").trim());
        return new Employee(id, fullName, email, phone, department, jobTitle, salary, hireDate);
    }

    private long readLong(String prompt) {
        try {
            return Long.parseLong(readLine(prompt).trim());
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Please enter a whole number for the ID.");
        }
    }

    private String readLine(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    private static void printEmployees(List<Employee> employees) {
        if (employees.isEmpty()) {
            System.out.println("No employees found.");
            return;
        }
        employees.forEach(System.out::println);
    }
}
