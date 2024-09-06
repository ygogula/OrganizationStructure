import com.salaryanalyzer.exception.EmployeeNotFoundException;
import com.salaryanalyzer.exception.InvalidCSVFormatException;
import com.salaryanalyzer.service.OrganizationStructure;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertNotNull;

public class OrganizationStructureTest {
    private OrganizationStructure org;

    private  String path= "C:\\Users\\gogula\\Amrutha\\Java\\eclipse-workspace\\SalaryAnalyzer\\src\\test\\resources\\employee.csv";

    @Before
    public void setup() throws InvalidCSVFormatException, IOException {
        org = new OrganizationStructure();
        org.loadEmployeesFromCSV(path);
    }

    @Test
    public void testValidCSVLoad()  {
        assertNotNull(org);
    }

    @Test(expected = EmployeeNotFoundException.class)
    public void testManagerNotFound() throws  EmployeeNotFoundException {
        org.checkManagerSalaries();  // If manager doesn't exist, this should throw an exception
    }

    @Test
    public void testCheckReportingLines() throws EmployeeNotFoundException {
        org.checkReportingLines();
    }
}
