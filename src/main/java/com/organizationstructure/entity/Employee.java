package com.organizationstructure.entity;


public class Employee {
    private String id;
    private String firstName;
    private String lastName;
    private String managerId;  // null or empty for CEO
    private double salary;

    public Employee(String id, String firstName, String lastName, String managerId, double salary) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.managerId = managerId;
        this.salary = salary;
    }

    // Getters and setters
    public String getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getManagerId() { return managerId; }
    public double getSalary() { return salary; }
}

