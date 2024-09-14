package com.organizationstructure;

import com.organizationstructure.exception.EmployeeNotFoundException;
import com.organizationstructure.exception.InvalidCSVFormatException;
import com.organizationstructure.service.OrganizationStructure;

import java.io.IOException;

public class EmployeeStructureApplication {
    public static void main(String[] args) {

        if (args.length != 1) {
            System.err.println("Usage: java salary analyzer.jar <file-path>");
            System.exit(1);
        }

        String filePath = args[0];  // CSV file path from the command line argument

        OrganizationStructure org = new OrganizationStructure();
        try {
            // Load employees from CSV
            org.loadEmployeesFromCSV(filePath);

            // Check manager salaries
            System.out.println("Checking manager salaries...");
            org.checkManagerSalaries();

            // Check reporting lines
            System.out.println("Checking reporting lines...");
            org.checkReportingLines();

        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        } catch (InvalidCSVFormatException e) {
            System.err.println("CSV Format Error: " + e.getMessage());
        } catch (EmployeeNotFoundException e) {
            System.err.println("Error in hierarchy: " + e.getMessage());
        }
    }
}