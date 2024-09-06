package com.organizationstructure.service;

import com.organizationstructure.entity.Employee;
import com.organizationstructure.exception.EmployeeNotFoundException;
import com.organizationstructure.exception.InvalidCSVFormatException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrganizationStructure {
    private Map<String, Employee> employees = new HashMap<>();
    private Map<String, List<Employee>> managerSubordinates = new HashMap<>();
    private String ceoId;

    // Method to load employees from the CSV
    public void loadEmployeesFromCSV(String filePath) throws IOException, InvalidCSVFormatException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        int lineNumber = 0;

        line = reader.readLine();  // This reads and discards the header line
        if (line == null) {
            throw new InvalidCSVFormatException("CSV file is empty.");
        }

        while ((line = reader.readLine()) != null) {
            lineNumber++;
            String[] data = line.split(",");
            if (data.length == 4) {
                String id = data[0].trim();        // Employee ID
                String firstName = data[1].trim();
                String lastName = data[2].trim();
                double salary;

                // Parse salary (3rd field)
                try {
                    salary = Double.parseDouble(data[3].trim());
                } catch (NumberFormatException e) {
                    throw new InvalidCSVFormatException("Invalid salary format at line " + lineNumber);
                }

                // Since this is the CEO (no manager), managerId is null
                Employee emp = new Employee(id,firstName,lastName, null, salary);
                employees.put(id, emp);
                ceoId = id;  // Mark this employee as the CEO
            }
            // Handle other employees (4 fields: id, name, managerId, salary)
            else if (data.length == 5) {
                String id = data[0].trim();        // Employee ID
                String firstName = data[1].trim();
                String lastName = data[2].trim();
                String managerId = data[4].trim(); // Manager ID
                double salary;

                // Parse salary (4th field)
                try {
                    salary = Double.parseDouble(data[3].trim());
                } catch (NumberFormatException e) {
                    throw new InvalidCSVFormatException("Invalid salary format at line " + lineNumber);
                }

                // Create the employee and assign manager
                Employee emp = new Employee(id, firstName, lastName, managerId.isEmpty() ? null : managerId, salary);
                employees.put(id, emp);

                // Add the employee to the manager's subordinate list
                managerSubordinates.computeIfAbsent(managerId, k -> new ArrayList<>()).add(emp);
            } else {
                throw new InvalidCSVFormatException("Invalid CSV format at line " + lineNumber + ". Expected 4 or 5 fields, got " + data.length);
            }
        }

        reader.close();

        if (ceoId == null) {
            throw new InvalidCSVFormatException("No CEO (employee without a manager) found in the CSV file.");
        }
    }

    // Method to check salary ranges for managers
    public void checkManagerSalaries() throws EmployeeNotFoundException {
        for (String managerId : managerSubordinates.keySet()) {
            Employee manager = employees.get(managerId);
            if (manager == null) {
                throw new EmployeeNotFoundException("Manager with ID " + managerId + " not found.");
            }

            List<Employee> subordinates = managerSubordinates.get(managerId);
            double averageSubordinateSalary = subordinates.stream()
                    .mapToDouble(Employee::getSalary)
                    .average()
                    .orElse(0);

            double minSalary = averageSubordinateSalary * 1.2;
            double maxSalary = averageSubordinateSalary * 1.5;

            if (manager.getSalary() < minSalary) {
                System.out.println(manager.getFirstName() + manager.getLastName() + " earns less than they should by "
                        + (minSalary - manager.getSalary()));
            } else if (manager.getSalary() > maxSalary) {
                System.out.println(manager.getFirstName() + manager.getLastName() + " earns more than they should by "
                        + (manager.getSalary() - maxSalary));
            }
        }
    }

    // Method to check for employees with too many managers
    public void checkReportingLines() throws EmployeeNotFoundException {
        for (Employee emp : employees.values()) {
            int levels = getNumberOfManagers(emp.getId());
            if (levels > 4) {
                System.out.println(emp.getFirstName() + emp.getLastName() + " has too many managers above them (" + levels + ")");
            }
        }
    }

    // Recursive method to count the number of managers above an employee
    private int getNumberOfManagers(String employeeId) throws EmployeeNotFoundException {
        Employee emp = employees.get(employeeId);
        if (emp == null) {
            throw new EmployeeNotFoundException("Employee with ID " + employeeId + " not found.");
        }

        if (emp.getManagerId() == null || emp.getManagerId().isEmpty()) {
            return 0;  // CEO has no manager
        }
        return 1 + getNumberOfManagers(emp.getManagerId());
    }
}
