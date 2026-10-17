package com.company.ems.model;

import java.math.BigDecimal;
import java.time.LocalDate;

/** Holds the information for one employee. */
public class Employee {
    private long id;
    private String fullName;
    private String email;
    private String phone;
    private String department;
    private String jobTitle;
    private BigDecimal salary;
    private LocalDate hireDate;

    public Employee(long id, String fullName, String email, String phone,
                    String department, String jobTitle, BigDecimal salary, LocalDate hireDate) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.department = department;
        this.jobTitle = jobTitle;
        this.salary = salary;
        this.hireDate = hireDate;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getDepartment() { return department; }
    public String getJobTitle() { return jobTitle; }
    public BigDecimal getSalary() { return salary; }
    public LocalDate getHireDate() { return hireDate; }

    @Override
    public String toString() {
        return "ID: " + id + " | " + fullName + " | " + email + " | " + department
                + " | " + jobTitle + " | Salary: " + salary + " | Hired: " + hireDate;
    }
}
